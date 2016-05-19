package gameLogic.characters;

import gameLogic.attributes.Stat;
import gameLogic.attributes.Stats;
import gameLogic.buffs.Buff;
import gameLogic.skills.Skill;
import gameLogic.skills.Skills;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Character {
    private static DecimalFormat df = new DecimalFormat("#.#");

    private String name;
    private Map<String, Stat> status;
    private Map<String, Integer> skills;
    private List<Buff> activeBuffs = new ArrayList<>();

    private Character opponent;

    private List<String> eventLog;

    public Character(String name, Map<String, Integer> skills, Map<String, Stat> status) {
        this.name = name;
        this.skills = skills;
        this.status = status;
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
        if (!status.containsKey(statusName)) {
            status.put(statusName, Stats.getDefaultValueOf(statusName));
        }

        return status.get(statusName).getValue();
    }

    public double changeStatus(String statusName, double amount) {
        if (!status.containsKey(statusName)) {
            status.put(statusName, Stats.getDefaultValueOf(statusName));
        }

        System.out.println(name + ": " + statusName + " changed by " + amount);

        double change = status.get(statusName).changeValueBy(amount);

        if (change > 0.5) writeToLog(name + " gained " + df.format(change) + " " + statusName);
        else if (change < -0.5) writeToLog(name + " lost " + df.format(-change) + " " + statusName);

        return change;
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

        writeToLog(name + " used skill " + skillName);
        System.out.println(name + " used skill " + skillName);

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

    public Map<String, Stat> getStatus() {

        return status;
    }

    public String getName() {
        return name;
    }

    public List<String> getEventLog() {
        return eventLog;
    }

    public void writeToLog(String entry) {
        if (eventLog != null) {
            eventLog.add(entry);
        }
    }

    public void setEventLog(List<String> eventLog) {
        this.eventLog = eventLog;
    }

    @Override
    public String toString() {
        return "Character{" +
                "skills=" + skills +
                ", status=" + status +
                '}';
    }
}
