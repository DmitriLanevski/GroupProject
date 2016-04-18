package gameLogic.buffs;

import gameLogic.characters.Character;

public abstract class Buff {
    public boolean isExpired = true;

    public synchronized void onApplied(Character user, Character target, String usedSkillName) {

    }
    public synchronized void onRemoved(Character user, Character target, String usedSkillName) {

    }
    public synchronized boolean isExpired() {
        return isExpired;
    }

    public synchronized void onAttack(Character user, Character target, String usedSkillName) {

    }
    public synchronized void onDefend(Character user, Character target, String usedSkillName) {

    }

    public synchronized void onDamageDealt(Character user, Character target, String usedSkillName, double Amount) {

    }
    public synchronized void onDamageTaken(Character user, Character target, String usedSkillName, double Amount) {
    }

    public synchronized void onStatusChange(Character user, Character target, String usedSkillName, String statusName, double change) {

    }
}
