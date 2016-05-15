package gameLogic.buffs;

import gameLogic.characters.Character;

/**
 * Buff that changes a single stat by a flat value each tick.
 */
public class StatOverTimeBuff extends Buff {
    private String statusName;
    private double changePerTick;

    public StatOverTimeBuff(boolean selfApply, Character user, Character opponent, String skillNameOrType,
                            String statusName, int durationInTicks, double changePerTick) {
        super(selfApply, user, opponent, skillNameOrType, durationInTicks);
        this.statusName = statusName;
        this.changePerTick = changePerTick;
    }

    @Override
    public void onTick() {
        getUser().eventChangeStatusBy(statusName, changePerTick);
    }
}