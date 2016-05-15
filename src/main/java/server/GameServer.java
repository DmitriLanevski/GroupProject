package server;

import com.google.gson.Gson;
import gameLogic.BattleInstance;
import gameLogic.Game;
import gameLogic.attributes.CharacterGenerationStatData;
import gameLogic.attributes.Stats;
import gameLogic.characters.Character;
import gameLogic.skills.Skills;
import serverDatabase.CharacterData;
import serverDatabase.UserDatabase;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.*;

public class GameServer implements Runnable {

    public static Gson gson = new Gson();

    private final int port;
    private final List<ServerPlayerInfo> players = Collections.synchronizedList(new ArrayList<>());
    private final Object messageOrderlock = new Object();

    private ServerPlayerInfo userAwaitingGame = null;

    private UserDatabase userDatabase;

    public GameServer(int port) throws SQLException, IOException {
        this.port = port;
    }

    private void sendToAll(Message message) {
        synchronized (messageOrderlock) { // ensures all players get all the messages in the same order
            for (ServerPlayerInfo player : players) {
                player.sendMessage(message);
            }
        }
    }
    private void sendToAll(int messageType, Object object) {
        sendToAll(new Message( messageType, object ));
    }

    private void handleMessage(int messageType, String message, ServerPlayerInfo sender) throws Exception {
        switch(messageType) {
            case MessageTypes.TEXT: {
                String s = gson.fromJson(message, String.class);
                sendToAll( MessageTypes.TEXT, sender.getUserName() + ": " + s );
                System.out.println(s);
                break;
            }
            case MessageTypes.LOGIN: {
                LoginData data = gson.fromJson(message, LoginData.class);
                attemptLogin(data, sender);
                break;
            }
            case MessageTypes.REGISTER_USER: {
                LoginData data = gson.fromJson(message, LoginData.class);
                if (attemptRegister(data, sender)) {
                    attemptLogin(data, sender);
                }
            }
            case MessageTypes.NEW_CHARACTER: {
                CharacterData data = gson.fromJson(message, CharacterData.class);
                sender.getGameData().getCharacters().put(data.getCharName(), data);
                sender.sendMessage(MessageTypes.CHARACTER_CREATE_SUCCESS, "");
                break;
            }
            case MessageTypes.REQUEST_CHARACTERS: {
                sender.sendMessage(MessageTypes.REQUEST_CHARACTERS, sender.getGameData().getCharacters().values());
                break;
            }
            case MessageTypes.REQUEST_GAME_START: {
                attemptStartGame(sender, gson.fromJson(message, String.class));
                break;
            }
            case MessageTypes.REQUEST_ALL_SKILLS: {
                sender.sendMessage(MessageTypes.REQUEST_ALL_SKILLS, Skills.getAllSkillDescriptions());
                break;
            }
            case MessageTypes.REQUEST_SKILLS_ALTERABLE_STATS: {
                List<String> skills = gson.fromJson(message, ArrayList.class);
                Set<String> statNames = new HashSet<>(Stats.getUniversals().keySet());
                for (String skillName : skills) {
                    statNames.addAll(Skills.getSkillByName(skillName).getRequiredStats());
                }
                Map<String, CharacterGenerationStatData> requestedData = new HashMap<>();
                for (String statName : statNames) {
                    if (Stats.getGrowthRateOf(statName) != 0) {
                        requestedData.put(statName, new CharacterGenerationStatData(Stats.getDefaultValueOf(statName).getMax(), Stats.getGrowthRateOf(statName)));
                    }
                }
                sender.sendMessage(MessageTypes.REQUEST_SKILLS_ALTERABLE_STATS, requestedData);
                break;
            }
            case MessageTypes.REQUEST_FULL_GAME_STATE: {
                HashMap<String, String> skills = new HashMap<>();
                for (String skillName : sender.getGameData().getChosenCharacter().getSkillIDs()) {
                    skills.put(skillName, Skills.getSkillDescByName(skillName));
                }
                sender.sendMessage(MessageTypes.SELF_SKILLS, skills);
                sendBattleStats(sender);
                break;
            }
            case MessageTypes.SKILL_USE: {
                BattleInstance battleInstance = sender.getGameData().getActiveBattle();
                synchronized (battleInstance) {
                    battleInstance.skillUse( sender.getGameData().getPlayerID(), gson.fromJson(message, String.class) );
                    for (ServerPlayerInfo user : battleInstance.getInformedUsers()) {
                        sendBattleStats(user);
                    }
                    if (battleInstance.isOver())
                        endGame(battleInstance);
                }
                break;
            }
            default: {
                throw new RuntimeException("Invalid message of type '" + messageType + "'");
            }
        }
    }

    private void sendBattleStats(ServerPlayerInfo user) {
        user.sendMessage(MessageTypes.SKILL_STATES, user.getGameData().getActiveBattle().getSkillStatesOfCharacter(user.getGameData().getPlayerID()));
        user.sendMessage(MessageTypes.SELF_CHARACTER_STATUSES, user.getGameData().getActiveBattle().getStatsOfCharacter(user.getGameData().getPlayerID()));
        user.sendMessage(MessageTypes.OPPOSING_CHARACTER_STATUSES, user.getGameData().getActiveBattle().getStatsOfCharacter(BattleInstance.opponentOf(user.getGameData().getPlayerID())));
    }

    private boolean attemptRegister(LoginData data, ServerPlayerInfo user) throws Exception {
        if (userDatabase.checkUserExistence(data.userName)) {
            user.sendMessage(MessageTypes.REGISTER_FAILURE, "User already exists.");
            return false;
        }
        userDatabase.registerUser(data.userName, data.password);
        return true;
    }

    private void attemptLogin(LoginData data, ServerPlayerInfo user) throws Exception {
        if (verifyLogin(data.userName, data.password)) {
            user.setUserName(data.userName);
            user.setLoggedIn(true);
            loadUserFromDatabase(user);
            user.sendMessage(MessageTypes.LOGIN_SUCCESS, "");

        } else {
            user.sendMessage(MessageTypes.LOGIN_FAILURE, "Invalid Username or Password");
        }
    }

    private synchronized void attemptStartGame(ServerPlayerInfo player, String charName) {
        player.getGameData().setChosenCharacter(player.getGameData().getCharacters().get(charName));

        if (userAwaitingGame != null & userAwaitingGame != player) {
            BattleInstance battle = new BattleInstance(Game.createCharacter(userAwaitingGame.getGameData().getChosenCharacter()), Game.createCharacter(player.getGameData().getChosenCharacter()));
            userAwaitingGame.getGameData().setActiveBattle(battle);
            player.getGameData().setActiveBattle(battle);

            userAwaitingGame.getGameData().setPlayerID(0);
            userAwaitingGame.sendMessage(MessageTypes.GAME_START, 0);

            player.getGameData().setPlayerID(1);
            player.sendMessage(MessageTypes.GAME_START, 1);

            battle.addInformedUser(userAwaitingGame);
            battle.addInformedUser(player);

            userAwaitingGame = null;

            battle.setUpTicking(()->{
                synchronized (battle) {
                    battle.tick();
                    for (ServerPlayerInfo user : battle.getInformedUsers()) {
                        sendBattleStats(user);
                    }
                }
            });
        }
        else
            userAwaitingGame = player;
    }

    private void endGame(BattleInstance battleInstance) {
        String winnerName;

        Character character = battleInstance.getWinner();
        if (character == null) winnerName = "nobody";
        else winnerName = character.getName();

        for (ServerPlayerInfo player : players) {
            player.sendMessage(MessageTypes.GAME_OVER, winnerName);
            player.getGameData().setActiveBattle(null);
            player.getGameData().setPlayerID(-1);
        }
    }

    private void loadUserFromDatabase(ServerPlayerInfo user) throws Exception {
        List<CharacterData> characters = userDatabase.getAllChars(user.getUserName());
        Map<String, CharacterData> characterDataMap = new HashMap<>();
        for (CharacterData character : characters) {
            characterDataMap.put(character.getCharName(), character);
        }
        user.setGameData(new UserGameData(characterDataMap));
    }

    private void saveUserToDatabase(ServerPlayerInfo user) throws Exception {
        // TODO: FINISH THIS
    }

    boolean verifyLogin(String username, String password) throws Exception {
        return userDatabase.logIn(username, password);
    }

    @Override
    public void run() {
        System.out.println("Starting server.");

        try (
                ServerSocket serverSocket = new ServerSocket(port);
                UserDatabase userDatabase = new UserDatabase()
        ) {
            this.userDatabase = userDatabase; // kinda silly, but automatically closes the database

            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();

                System.out.println("New connection");
                ServerPlayerInfo newPlayer = new ServerPlayerInfo(socket.hashCode(), socket);
                players.add(newPlayer);
                new Thread(new PlayerReciever(newPlayer)).start();
                new Thread(new PlayerMessager(newPlayer)).start();


                System.out.println(newPlayer.getUserName() + " has joined.");
                sendToAll(MessageTypes.SERVER_MESSAGE, newPlayer.getUserName() + " has joined.");
            }

        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    class PlayerReciever implements Runnable {
        private final Socket playerSocket;
        private final DataInputStream input;
        private final ServerPlayerInfo data;

        public PlayerReciever(ServerPlayerInfo data) {
            this.data = data;
            this.input = data.getInput();
            this.playerSocket = data.getConnectionSocket();
        }

        @Override
        public void run() {
            try {
                while (!playerSocket.isClosed()) {
                    int messageType = input.readInt();
                    handleMessage(messageType, input.readUTF(), data);
                }
            }
            catch (EOFException | SocketException e) {
                // Connection lost, nothing to worry about.
                System.out.println(data.getUserName() + " has disconnected.");
                sendToAll(MessageTypes.SERVER_MESSAGE, data.getUserName() + " has disconnected.");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    class PlayerMessager implements Runnable {
        private final Socket playerSocket;
        private final ServerPlayerInfo data;
        private final DataOutputStream output;

        public PlayerMessager(ServerPlayerInfo data) {
            this.data = data;
            this.playerSocket = data.getConnectionSocket();
            this.output = data.getOutput();
        }

        @Override
        public void run() {
            try {
                while (!playerSocket.isClosed()) {
                    Message sendingMessage = data.getMessageQueue().take();

                    output.writeInt(sendingMessage.getMessageType());
                    output.writeUTF(sendingMessage.getGsonObject());
                }

            }
            catch (SocketException e) {
                // Connection lost, nothing to worry about.
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
            finally {
                players.remove(data);
            }
        }
    }
}