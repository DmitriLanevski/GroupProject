package gameLogic;

import gameLogic.attributes.Stat;
import gameLogic.characters.Character;

import java.util.HashMap;
import java.util.Map;

public class BattleInstance {
    private final Character[] players = new Character[2];

    public BattleInstance(Character player0, Character player1) {
        players[0] = player0;
        players[1] = player1;

        players[0].setOpponent(players[1]);
        players[1].setOpponent(players[0]);

        players[0].initPassives();
        players[1].initPassives();
    }

    public synchronized void tick() {
        players[0].tick();
        players[1].tick();
    }

    public synchronized void skillUse(int playerID, String skillName) {
        players[playerID].useSkill(skillName);
    }

    public synchronized HashMap<String, Stat> getStatsOfCharacter(int ID) {
        HashMap<String, Stat> status = players[ID].getStatus();
        status.replaceAll( (n, stat)->new Stat(stat) );
        return status;
    }

    public synchronized Map<String, Boolean> getSkillStatesOfCharacter(int ID) {
        return players[ID].getSkillStates();
    }

    public static int opponentOf(int playerID) {
        if (playerID == 0) return 1;
        else return 0;
    }
}
