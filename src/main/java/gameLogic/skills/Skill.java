package gameLogic.skills;

import gameLogic.buffs.Buff;
import gameLogic.characters.Character;

import java.util.*;

public abstract class Skill {
    private String skillNameOrType;
    private Set<String> requiredStats = new HashSet<>();


    public Skill(String skillNameOrType) {
        this.skillNameOrType = skillNameOrType;
    }

    public void use(Character user, Character opponent) {
        user.eventSkillUsed(skillNameOrType);
        opponent.eventEnemySkillUsed(skillNameOrType);
    }

    public void onGameStart(Character user, Character opponent) {

    }

    public abstract boolean canUse(Character user, Character opponent, int ticksSinceLastUse);

    public Set<String> getRequiredStats() {
        return requiredStats;
    }

    public void addRequiredStat(String statName) {
        requiredStats.add(statName);
    }

    public String getSkillNameOrType() {
        return skillNameOrType;
    }
}
