package server;

import com.google.gson.Gson;
import serverDatabase.UserDatabase;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameServer implements Runnable {

    public static Gson gson = new Gson();

    private final int port;
    private final List<ServerPlayerInfo> players = Collections.synchronizedList(new ArrayList<>());
    private final Object messageOrderlock = new Object();

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
                if (verifyLogin(data.userName, data.password)) {
                    sender.setUserName(data.userName);
                    sender.setLoggedIn(true);
                    sender.sendMessage(MessageTypes.LOGIN_SUCCESS, "");
                } else {
                    sender.sendMessage(MessageTypes.LOGIN_FAILURE, "Invalid Username or Password");
                }
                break;
            }
            case MessageTypes.NEW_CHARACTER:{

                break;
            }
            default: {
                throw new RuntimeException("Invalid message of type '" + messageType + "'");
            }
        }
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