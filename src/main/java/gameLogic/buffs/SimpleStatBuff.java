package gameLogic.buffs;

import gameLogic.characters.Character;

/**
 * A simple buff that changes a stat for its duration.
 */
public class SimpleStatBuff extends Buff {
    private String statusName;
    private double amount;
    private boolean isSelf;

    public SimpleStatBuff(Character user, Character opponent, int durationInTicks, double amount, String statusName, boolean isSelf) {
        super(user, opponent, durationInTicks);
        this.amount = amount;
        this.statusName = statusName;
        this.isSelf = isSelf;
    }

    @Override
    public void onApplied() {
        if (isSelf){
            getUser().eventChangeStatusBy(statusName, amount);
        }
        else {
            getOpponent().eventChangeStatusBy(statusName, amount);
        }
    }

    @Override
    public void onRemoved() {
        if (isSelf){
            getUser().eventChangeStatusBy(statusName, -amount);
        } else {
            getOpponent().eventChangeStatusBy(statusName, -amount);
        }
    }

    public String getStatusName() {
        return statusName;
    }

    public double getAmount() {
        return amount;
    }
}
