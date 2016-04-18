package gameLogic.characters;

import gameLogic.attributes.Status;
import gameLogic.buffs.Buff;
import gameLogic.skills.Skill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Character {
    private HashMap<String, Status> status = new HashMap<>();
    private HashMap<String, Skill> skills = new HashMap<>();
    private List<Buff> activeBuffs = new ArrayList<>();

    public synchronized void addBuff(Buff addedBuff, Character target, String usedSkillName) {
        addedBuff.onApplied(this, target, usedSkillName);
        if (!addedBuff.isExpired()) {
            activeBuffs.add(addedBuff);
        }
    }

    public synchronized void cleanBuffList(Character target, String usedSkillName){
        Iterator<Buff> i = this.getActiveBuffs().iterator();
        while (i.hasNext()){
            Buff buff = i.next();
            if (buff.isExpired()){
                buff.onRemoved(this, target, usedSkillName);
                i.remove();
            }
        }
    }

    public synchronized void affectStatus(String statusName, double value){
        status.get(statusName).changeValueBy(value);
    }

    public synchronized void useSkill(String skillName, Character target){
        cleanBuffList(target, skillName);
        skills.get(skillName).getBuffs().forEach((Buff buff)->addBuff(buff, target, skillName));
    }

    public synchronized List<Buff> getActiveBuffs() {
        return activeBuffs;
    }

    public synchronized HashMap<String, Status> getStatus() {
        return status;
    }
}
