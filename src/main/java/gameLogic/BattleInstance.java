package gameLogic;

import gameLogic.attributes.Stat;
import gameLogic.characters.Character;
import server.ServerPlayerInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BattleInstance {
    private boolean over = false;

    private final Character[] players = new Character[2];
    private final List<ServerPlayerInfo> users = new ArrayList<>();

    private final ScheduledExecutorService ticker = new ScheduledThreadPoolExecutor(1);

    public BattleInstance(Character player0, Character player1) {
        players[0] = player0;
        players[1] = player1;

        players[0].setOpponent(players[1]);
        players[1].setOpponent(players[0]);

        players[0].initPassives();
        players[1].initPassives();

        ticker.scheduleAtFixedRate(this::tick, 5, 100, TimeUnit.MILLISECONDS);
    }

    public synchronized void tick() {
        if (over) return;

        players[0].tick();
        players[1].tick();

        checkForDeaths();
    }

    public synchronized void setUpTicking(Runnable runnable) {
        ticker.scheduleAtFixedRate(runnable, 5, 100, TimeUnit.MILLISECONDS);
    }

    public synchronized void addInformedUser(ServerPlayerInfo user) {
        users.add(user);
    }

    public synchronized List<ServerPlayerInfo> getInformedUsers() {
        return users;
    }

    public synchronized void skillUse(int playerID, String skillName) {
        if (over) return;

        players[playerID].useSkill(skillName);

        checkForDeaths();
    }

    public synchronized HashMap<String, Stat> getStatsOfCharacter(int ID) {
        HashMap<String, Stat> status = players[ID].getStatus();
        status.replaceAll( (n, stat)->new Stat(stat) );
        return status;
    }

    public synchronized Map<String, Boolean> getSkillStatesOfCharacter(int ID) {
        return players[ID].getSkillStates();
    }

    public synchronized void checkForDeaths() {
        if (someoneDied()) {
            over = true;
            ticker.shutdown();
        }
    }

    public synchronized boolean someoneDied() {
        for (Character player : players) {
            if (player.getStatusValue("Health") < 0) return true;
        }
        return false;
    }

    public static int opponentOf(int playerID) {
        if (playerID == 0) return 1;
        else return 0;
    }

    public boolean isOver() {
        return over;
    }

    public synchronized Character getWinner() {
        if (players[0].getStatusValue("Health") < 0 & players[1].getStatusValue("Health") < 0) {
            return null;
        } else if (players[0].getStatusValue("Health") < 0) {
            return players[1];
        } else {
            return players[0];
        }
    }
}
