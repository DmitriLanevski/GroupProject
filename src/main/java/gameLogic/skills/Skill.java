package gameLogic.skills;

import gameLogic.buffs.Buff;
import gameLogic.characters.Character;

import java.util.*;

public abstract class Skill {
    private String skillNameOrType;
    private Set<String> requiredStats = new HashSet<>();

    private final int cooldown;
    private final int cost;
    private final String stat;

    public Skill(String skillNameOrType, int cooldown, int cost, String stat) {
        this.skillNameOrType = skillNameOrType;
        this.cooldown = cooldown;
        this.cost = cost;
        this.stat = stat;
        addRequiredStat(stat);
    }

    public void use(Character user, Character opponent) {
        user.eventSkillUsed(skillNameOrType);
        opponent.eventEnemySkillUsed(skillNameOrType);
    }

    public void onGameStart(Character user, Character opponent) {

    }

    public boolean canUse(Character user, Character opponent, int ticksSinceLastUse) {
        return user.getStatusValue(stat) >= cost & ticksSinceLastUse >= cooldown;
    }

    public Set<String> getRequiredStats() {
        return requiredStats;
    }

    public void addRequiredStat(String statName) {
        requiredStats.add(statName);
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getCost() {
        return cost;
    }

    public String getStat() {
        return stat;
    }

    public String getSkillNameOrType() {
        return skillNameOrType;
    }
}
