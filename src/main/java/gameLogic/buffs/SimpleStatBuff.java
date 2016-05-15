package gameLogic.buffs;

import gameLogic.characters.Character;

/**
 * A simple buff that changes a stat for its duration.
 */
public class SimpleStatBuff extends Buff {
    private String statusName;
    private double amount;

    public SimpleStatBuff(Character user, Character opponent, int durationInTicks, String skillNameOrType, String statusName, double amount) {
        super(user, opponent, durationInTicks, skillNameOrType);
        this.statusName = statusName;
        this.amount = amount;
    }

    @Override
    public void onApplied() {
        getUser().eventChangeStatusBy(statusName, amount);
    }

    @Override
    public void onRemoved() {
        getUser().eventChangeStatusBy(statusName, -amount);
    }

    public String getStatusName() {
        return statusName;
    }

    public double getAmount() {
        return amount;
    }
}
