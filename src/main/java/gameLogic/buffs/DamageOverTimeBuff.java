package gameLogic.buffs;

import gameLogic.characters.Character;

/**
 * Buff that deals a flat amount of damage each tick.
 */
public class DamageOverTimeBuff extends Buff {
    private double damagePerTick;

    public DamageOverTimeBuff(boolean selfApply, Character user, Character opponent, String skillNameOrType,
                              int durationInTicks, double damagePerTick) {
        super(selfApply, user, opponent, skillNameOrType, durationInTicks);
        this.damagePerTick = damagePerTick;
    }

    @Override
    public void onTick() {
        getUser().eventDealDamage(damagePerTick);
    }
}
