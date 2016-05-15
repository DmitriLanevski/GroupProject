package gameLogic.buffs;

import gameLogic.characters.Character;

/**
 * Buff that deals a flat amount of damage each tick.
 */
public class DamageOverTimeBuff extends Buff {
    private double damagePerTick;

    public DamageOverTimeBuff(Character user, Character opponent, int durationInTicks, String skillNameOrType, double damagePerTick) {
        super(user, opponent, durationInTicks, skillNameOrType);
        this.damagePerTick = damagePerTick;
    }

    @Override
    public void onTick() {
        getUser().eventDealDamage(damagePerTick);
    }
}
