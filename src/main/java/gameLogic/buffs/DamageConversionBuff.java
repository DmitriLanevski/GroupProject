package gameLogic.buffs;

import gameLogic.characters.Character;

/**
 * Created by Madis on 19.05.2016.
 */
public class DamageConversionBuff extends NonRecursiveBuff {
    private final String convertTo;
    private final double conversionRatio;

    public DamageConversionBuff(boolean selfApply, Character user, Character opponent, String skillNameOrType, int durationInTicks, double conversionRatio, String convertTo) {
        super(selfApply, user, opponent, skillNameOrType, durationInTicks);
        this.conversionRatio = conversionRatio;
        this.convertTo = convertTo;
    }

    @Override
    protected void onDamageTakenBody(double amount) {
        getUser().eventChangeStatusBy(convertTo, amount*conversionRatio);
    }
}
