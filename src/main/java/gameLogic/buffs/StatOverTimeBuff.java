package gameLogic.buffs;

import gameLogic.characters.Character;

/**
 * Buff that changes a single stat by a flat value each tick.
 */
public class StatOverTimeBuff extends Buff {
    private String statusName;
    private double changePerTick;

    public StatOverTimeBuff(Character user, Character opponent, int durationInTicks, String skillNameOrType,
                            boolean isSelf, String statusName, double changePerTick) {
        super(user, opponent, durationInTicks, skillNameOrType);
        this.statusName = statusName;
        this.changePerTick = changePerTick;
    }

    @Override
    public void onTick() {
        getUser().eventChangeStatusBy(statusName, changePerTick);
    }
}