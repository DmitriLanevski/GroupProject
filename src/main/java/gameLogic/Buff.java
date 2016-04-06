package gameLogic;

public abstract class Buff {
    void onApplied(Character buffedCharacter, Character otherCharacter) {

    }
    void onRemoved(Character buffedCharacter, Character otherCharacter) {

    }
    boolean isExpired(Character buffedCharacter, Character otherCharacter) {
        return true;
    }

    void onAttack(Character buffedCharacter, Character otherCharacter, Skill usedSkill) {

    }
    void onDefend(Character buffedCharacter, Character otherCharacter, Skill usedSkill) {

    }

    void onDamageDealt(Character buffedCharacter, Character otherCharacter, double Amount) {

    }
    void onDamageTaken(Character buffedCharacter, Character otherCharacter, double Amount) {

    }

    void onStatusChange(Character buffedCharacter, Character otherCharacter, String statusName, double change) {

    }
}
