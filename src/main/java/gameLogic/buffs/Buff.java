package gameLogic.buffs;

import gameLogic.characters.Character;

public abstract class Buff {

    private Character user;
    private Character opponent;

    public Buff(Character user, Character opponent) {
        this.user = user;
        this.opponent = opponent;
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

    public void onStatusChange(String statusName, double amount) {

    }

    public void onTick(){
    }

    public Character getUser() {
        return user;
    }

    public Character getOpponent() {
        return opponent;
    }

    public void setUser(Character user) {
        this.user = user;
    }

    public void setOpponent(Character opponent) {
        this.opponent = opponent;
    }
}
