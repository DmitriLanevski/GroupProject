package gameLogic.skills;

import gameLogic.characters.Character;

/**
 * Created by Madis on 15.05.2016.
 */
public class SingleCostSkill extends Skill {
    private final int cooldown;
    private final int cost;
    private final String stat;

    public SingleCostSkill(String skillNameOrType, int cooldown, int cost, String stat) {
        super(skillNameOrType);
        this.cooldown = cooldown;
        this.cost = cost;
        this.stat = stat;
        addRequiredStat(stat);
    }

    public boolean canUse(Character user, Character opponent, int ticksSinceLastUse) {
        return user.getStatusValue(stat) >= -cost & ticksSinceLastUse >= cooldown;
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
}
