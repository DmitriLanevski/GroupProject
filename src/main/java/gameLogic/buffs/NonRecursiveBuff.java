package gameLogic.buffs;

import gameLogic.characters.Character;

/**
 * Created by lanev_000 on 20.04.2016.
 */
public class NonRecursiveBuff extends Buff {
    private boolean recursiveLock = false;

    public NonRecursiveBuff(Character user, Character oponent, String statusName, double effectValue, int durationInTicks, String skillName) {
        super(user, oponent, statusName, effectValue, durationInTicks, skillName);
    }

    @Override
    final public void onDamageTaken(double amount) {
        if (recursiveLock) return;
        recursiveLock = true;
        onDamageTakenBody();
        recursiveLock = false;
    }

    private void onDamageTakenBody(){

    }
}
