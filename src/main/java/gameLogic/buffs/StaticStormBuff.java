package gameLogic.buffs;

import gameLogic.characters.Character;

import java.util.Random;

/**
 * Created by Madis on 19.05.2016.
 */
public class StaticStormBuff extends Buff {
    static final Random random = new Random();

    private final double ratioUsed;
    private final double procChance;
    private final double damageRatio;

    public StaticStormBuff(boolean selfApply, Character user, Character opponent, String skillNameOrType, int durationInTicks, double ratioUsed, double procChance, double damageRatio) {
        super(selfApply, user, opponent, skillNameOrType, durationInTicks);
        this.ratioUsed = ratioUsed;
        this.procChance = procChance;
        this.damageRatio = damageRatio;
    }

    @Override
    public void onTick() {
        super.onTick();
        if (random.nextDouble() < procChance) {
            double charge = getUser().getStatusValue("Static charge")*ratioUsed;
            getUser().eventChangeStatusBy("Static charge", -charge);
            getOpponent().eventDealDamage(charge*damageRatio);
        }
    }
}
