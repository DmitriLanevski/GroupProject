package gameLogic.buffs;

import gameLogic.characters.Character;

public abstract class Buff {

    private Character user;
    private Character oponent;

    public Buff(Character user, Character opponent) {
        this.user = user;
        this.oponent = opponent;
    }

    public void onApplied() {

    }
    public void onRemoved() {

    }
    public boolean isExpired() {
        return true;
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
