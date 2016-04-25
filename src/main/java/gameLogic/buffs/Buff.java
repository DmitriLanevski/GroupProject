package gameLogic.buffs;

import gameLogic.characters.Character;

public abstract class Buff {

    private Character user;
    private Character oponent;

    public Buff(Character user, Character oponent) {
        this.user = user;
        this.oponent = oponent;
    }

    public void onApplied() {

    }
    public void onRemoved() {

    }
    public boolean isExpired() {

    }

    public void onAttack() {

    }
    public void onDefend() {

    }

    public void onDamageTaken(double amount) {
    }

    public void onStatusChange(double amount) {

    }

    public void onTick(){
    }

    public Character getUser() {
        return user;
    }

    public Character getOponent() {
        return oponent;
    }

    public void setUser(Character user) {
        this.user = user;
    }

    public void setOponent(Character oponent) {
        this.oponent = oponent;
    }
}
