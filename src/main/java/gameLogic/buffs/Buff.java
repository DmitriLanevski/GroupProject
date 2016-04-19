package gameLogic.buffs;

import gameLogic.characters.Character;

public abstract class Buff {
    private boolean isExpired;
    private String statusName;
    private double effectValue;
    private int durationInTicks;
    private String skillName; //Maybe it isn't necessary, but this parameter will exist anyway within the skill and
                              // we can track skill origins and invent some measure against or for. It can have great purpose.
    private Character user;
    private Character oponent;

    public Buff(Character user, Character oponent, String statusName, double effectValue, int durationInTicks, String skillName) {
        this.oponent = oponent;
        this.user = user;
        this.statusName = statusName;
        this.effectValue = effectValue;
        this.durationInTicks = durationInTicks;
        this.skillName = skillName;
        if (durationInTicks == 0){
            this.isExpired = false;
        } else {this.isExpired = true;}
    }

    public void onApplied() {

    }
    public void onRemoved() {

    }
    public boolean isExpired() {
        return isExpired;
    }

    public void onAttack() {

    }
    public void onDefend() {

    }

    public void onDamageDealt() {

    }
    public void onDamageTaken() {
    }

    public void onStatusChange() {

    }

    public void onTick(){
        if (durationInTicks > 0){
            durationInTicks--;
        } else {
            isExpired = true;
        }
    }

    public String getStatusName() {
        return statusName;
    }

    public double getEffectValue() {
        return effectValue;
    }

    public int getDurationInTicks() {
        return durationInTicks;
    }

    public String getSkillName() {
        return skillName;
    }

    public Character getUser() {
        return user;
    }

    public Character getOponent() {
        return oponent;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public void setEffectValue(double effectValue) {
        this.effectValue = effectValue;
    }

    public void setDurationInTicks(int durationInTicks) {
        this.durationInTicks = durationInTicks;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public void setUser(Character user) {
        this.user = user;
    }

    public void setOponent(Character oponent) {
        this.oponent = oponent;
    }
}
