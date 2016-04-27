package gameLogic.buffs;

import gameLogic.characters.Character;

/**
 * Buff that changes a single stat by a flat value each tick.
 */
public class StatOverTimeBuff extends Buff {
    private String statusName;
    private double changePerTick;

    public StatOverTimeBuff(Character user, Character opponent, int durationInTicks, double changePerTick, String statusName) {
        super(user, opponent, durationInTicks);
        this.changePerTick = changePerTick;
        this.statusName = statusName;
    }

    @Override
    public void onTick() {
        getUser().eventChangeStatusBy(statusName, changePerTick);
    }
}