package gameLogic.buffs;

import gameLogic.characters.Character;

/**
 * Buff that cannot recursively call itself in an infinite loop.
 */
public class NonRecursiveBuff extends Buff {
    private boolean recursionLock = false;

    public NonRecursiveBuff(Character user, Character opponent, int durationInTicks) {
        super(user, opponent, durationInTicks);
    }

    // Done this way so someone doesn't accidentally overwrite the lock. If lock is unneccessary, just use Buff instead.
    @Override
    final public void onDamageTaken(double amount) {
        if (recursionLock) return;
        recursionLock = true;
        onDamageTakenBody(amount);
        recursionLock = false;
    }

    private void onDamageTakenBody(double amount) {

    }

    @Override
    final public void onStatusChange(String statusName, double amount) {
        if (recursionLock) return;
        recursionLock = true;
        onStatusChangeBody(statusName, amount);
        recursionLock = false;
    }

    private void onStatusChangeBody(String statusName, double amount) {

    }
}
