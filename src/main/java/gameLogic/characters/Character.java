package gameLogic.characters;

import gameLogic.attributes.Status;
import gameLogic.buffs.Buff;
import gameLogic.skills.Skill;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Character {
    private HashMap<String, Status> status = new HashMap<>();
    private HashMap<String, Skill> skills = new HashMap<>();
    private List<Buff> activeBuffs = new ArrayList<>();

    public void addBuff(Buff addedBuff) {
        addedBuff.onApplied();
        if (!addedBuff.isExpired()) {
            activeBuffs.add(addedBuff);
        }
    }

    public List<Buff> getActiveBuffs() {
        return activeBuffs;
    }

    public void cleanBuffList(){
         activeBuffs.removeIf((Buff buff)->{
             if (buff.isExpired()) {
               buff.onRemoved();
               return true;
           } else return false;
        });
    }

    public void changeStatus(String statusName, double amount){
        status.get(statusName).changeValueBy(amount);
    }

    void eventChangeStatusBy(String statusName, double amount) {
        changeStatus(statusName, amount);
        activeBuffs.forEach((Buff buff)->buff.onStatusChange(amount));
    }

    void eventDealDamage(double amount) {
        changeStatus("Health", amount);
        activeBuffs.forEach((Buff buff)->buff.onDamageTaken(amount));
    }
}
