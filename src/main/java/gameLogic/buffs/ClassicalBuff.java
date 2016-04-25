package gameLogic.buffs;

import gameLogic.characters.Character;

/**
 * Created by lanev_000 on 21.04.2016.
 */
public class ClassicalBuff extends Buff{
    private boolean isExpired;
    private String statusName;
    private double effectValue;
    private int durationInTicks;
    private String skillName; //Maybe it isn't necessary, but this parameter will exist anyway within the skill and
    // we can track skill origins and invent some measure against or for. It can have great purpose.


    public ClassicalBuff(Character user, Character opponent, boolean isExpired, String statusName, double effectValue, int durationInTicks, String skillName) {
        super(user, opponent);
        if (durationInTicks == 0){
            this.isExpired = false;
        } else {this.isExpired = true;}
        this.statusName = statusName;
        this.effectValue = effectValue;
        this.durationInTicks = durationInTicks;
        this.skillName = skillName;
    }

    @Override
    public void onApplied(){

    }

    @Override
    public boolean isExpired(){
        return isExpired;
    }

    @Override
    public void onTick(){
        if (durationInTicks > 0){
            durationInTicks--;
        } else {
            isExpired = true;
        }
    }
}
