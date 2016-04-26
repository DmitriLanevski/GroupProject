package gameLogic.buffs;

import gameLogic.characters.Character;

/**
 * A simple buff that changes a stat for its duration.
 */
public class SimpleStatBuff extends Buff {
    private String statusName;
    private double amount;

    public SimpleStatBuff(Character user, Character opponent, int durationInTicks, double amount, String statusName) {
        super(user, opponent, durationInTicks);
        this.amount = amount;
        this.statusName = statusName;
    }

    @Override
    public void onApplied() {
        getUser().changeStatus(statusName, amount);
    }

    @Override
    public void onRemoved() {
        getUser().changeStatus(statusName, -amount);
    }
}
