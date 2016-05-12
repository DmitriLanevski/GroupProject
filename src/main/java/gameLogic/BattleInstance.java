package gameLogic;

import gameLogic.characters.Character;

public class BattleInstance {
    private final Character[] players = new Character[2];

    public BattleInstance(Character player0, Character player1) {
        players[0] = player0;
        players[1] = player1;
    }

    public synchronized void tick() {
        players[0].tick();
        players[1].tick();
    }

    public synchronized void skillUse(int playerID, String skillName) {
        players[playerID].useSkill(skillName);
    }
}
