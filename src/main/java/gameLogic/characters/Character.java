package gameLogic.characters;

import gameLogic.attributes.Stat;
import gameLogic.buffs.Buff;
import gameLogic.skills.Skill;
import gameLogic.skills.Skills;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Character {
    private HashMap<String, Stat> status;
    private HashMap<String, Integer> skills;
    private List<Buff> activeBuffs = new ArrayList<>();

    private Character opponent;

    public Character(HashMap<String, Integer> skills, HashMap<String, Stat> status) {
        this.skills = skills;
        this.status = status;

        System.out.println(this);
    }

    public void setOpponent(Character opponent) {
        this.opponent = opponent;
    }

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

    public double getStatusValue(String statusName) {
        return status.get(statusName).getValue();
    }

    public double changeStatus(String statusName, double amount) {
        return status.get(statusName).changeValueBy(amount);
    }

    public void eventChangeStatusBy(String statusName, double amount) {
        double change = changeStatus(statusName, amount);
        activeBuffs.forEach((buff)->buff.onStatusChange(statusName, change));
    }

    public void eventDealDamage(double amount) {
        changeStatus("Health", -amount);
        activeBuffs.forEach((buff)->buff.onDamageTaken(amount));
    }

    public void eventSkillUsed(String skillNameOrType) {
        activeBuffs.forEach((buff)->buff.onSkillUse(skillNameOrType));
    }

    public void eventEnemySkillUsed(String skillNameOrType) {
        activeBuffs.forEach((buff)->buff.onOpponentSkillUse(skillNameOrType));
    }

    public void eventAttack() {
        activeBuffs.forEach(Buff::onAttack);
    }

    public void eventDefend() {
        activeBuffs.forEach(Buff::onDefend);
    }

    public void tick() {
        skills.replaceAll((s,ticksSinceLastUse)->ticksSinceLastUse+1);

        activeBuffs.forEach(Buff::onTick);
    }

    public void useSkill(String skillName) {
        if (!canUseSkill(skillName)) return;

        Skills.getSkillByName(skillName).use(this, opponent);
        skills.replace(skillName, 0);
    }

    public boolean canUseSkill(String skillName) {
        if (skills.containsKey(skillName))
            return Skills.getSkillByName(skillName).canUse(this, opponent, skills.get(skillName));
        else
            return false;
    }

    public Map<String, Boolean> getSkillStates() {
        Map<String, Boolean> states = new HashMap<>();
        for (String skillName : skills.keySet()) {
            states.put(skillName, canUseSkill(skillName));
        }
        return states;
    }

    public void initPassives() {
        for (String skillName : skills.keySet()) {
            Skills.getSkillByName(skillName).onGameStart(this, opponent);
        }
    }

    public HashMap<String, Stat> getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Character{" +
                "skills=" + skills +
                ", status=" + status +
                '}';
    }
}
