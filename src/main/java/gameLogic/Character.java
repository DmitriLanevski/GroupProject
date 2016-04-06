package gameLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Character {
    private HashMap<String, Status> status = new HashMap<>();
    private List<Skill> skills = new ArrayList<>();
    private List<Buff> activeBuffs = new ArrayList<>();

    void addBuff(Buff addedBuff, Character otherCharacter) {
        addedBuff.onApplied(this, otherCharacter);
        if (!addedBuff.isExpired(this, otherCharacter)) {
            activeBuffs.add(addedBuff);
        }
    }

    List<Buff> getBuffs() {
        return activeBuffs;
    }

    void eventChangeStatusBy(double value, String name, Character otherCharacter) {
        double change = status.get(name).changeValueBy(value);
        activeBuffs.forEach((Buff buff)->buff.onStatusChange(this, otherCharacter, name, change));
    }

    void dealDamage(double amount) {
        status.get("Health").changeValueBy(-amount);
    }
    void eventDealDamage(double amount, Character otherCharacter) {
        dealDamage(amount);
        activeBuffs.forEach((Buff buff)->buff.onDamageTaken(this, otherCharacter, amount));
    }
    void eventDealtDamage(double amount, Character otherCharacter) {
        activeBuffs.forEach((Buff buff)->buff.onDamageDealt(this, otherCharacter, amount));
    }
    void eventSharedDealDamage(double amount, Character otherCharacter) {
        eventDealDamage(amount, otherCharacter);
        otherCharacter.eventDealtDamage(amount, this);
    }
}
