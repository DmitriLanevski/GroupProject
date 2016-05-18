package gameLogic.buffs;

import gameLogic.characters.Character;

/**
 * Created by Madis on 19.05.2016.
 */
public class LossConversionBuff extends NonRecursiveBuff {
    private final String convertFrom;
    private final String convertTo;
    private final double conversionRatio;

    public LossConversionBuff(boolean selfApply, Character user, Character opponent, String skillNameOrType, int durationInTicks, double conversionRatio, String convertFrom, String convertTo) {
        super(selfApply, user, opponent, skillNameOrType, durationInTicks);
        this.conversionRatio = conversionRatio;
        this.convertFrom = convertFrom;
        this.convertTo = convertTo;
    }

    @Override
    public void onStatusChangeBody(String statusName, double amount) {
        if (statusName.equals(convertFrom) & amount < 0) {
            getUser().eventChangeStatusBy(convertTo, -amount*conversionRatio);
        }
    }
}
