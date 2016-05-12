package gameLogic.skills;

import gameLogic.characters.Character;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Skill {
    public void use(Character user, Character opponent) {
        user.eventSkillUsed();
        opponent.eventEnemySkillUsed();
    }

    public void onGameStart(Character user, Character opponent) {

    }

    public abstract boolean canUse(Character user, Character opponent, int ticksSinceLastUse);

    public abstract List<String> requiredStats();
}
