package gameLogic.buffs;

import gameLogic.characters.Character;

import java.util.ArrayList;

public abstract class Buff {

    private Character user;
    private Character opponent;
    protected int durationInTicks;
    private boolean isSelf;
    private String skillNameOrType;
    protected int timePassedInTicks = 0;

    /**
     * Creates new buff.
     * @param user The character the buff will be applied to.
     * @param opponent The opponent of the buffed character.
     * @param durationInTicks Duration of the buff. -1 indicates a permanent buff.
     */
    public Buff(Character user, Character opponent, int durationInTicks, String skillNameOrType, boolean isSelf) {
        this.user = user;
        this.opponent = opponent;
        this.durationInTicks = durationInTicks;
        this.skillNameOrType = skillNameOrType;
        this.isSelf = isSelf;
    }

    /**
     *  Called on initial application of buff.
     */
    public void onApplied() {

    }

    /**
     *  Called right before the buff is removed from the list of active buffs.
     */
    public void onRemoved() {

    }

    /**
     * @return A boolean indicating if the buff should be removed from the list of active buffs.
     */
    public boolean isExpired() {
        if (durationInTicks == -1) return false;
        else return timePassedInTicks >= durationInTicks;
    }

    /**
     * Called when buffed character uses a skill
     */
    public void onSkillUse(String skillNameOrType) {

    }

    /**
     * Called when the opponent uses a skill
     */
    public void onOpponentSkillUse(String skillNameOrType) {

    }

    /**
     * Called when buffed character uses an attack skill.
     */
    public void onAttack() {

    }

    /**
     * Called when the opponent uses an attack skill.
     */
    public void onDefend() {

    }

    /**
     * Called when buffed character takes damage.
     * @param amount The amount of damage taken.
     */
    public void onDamageTaken(double amount) {
    }

    /**
     * Called when the value of a stat is changed.
     * @param statusName Name of changed stat e.g.: "Mana" or "Stamina"
     * @param amount The amount of change.
     */
    public void onStatusChange(String statusName, double amount) {

    }

    /**
     * Called each time tick.
     */
    public void onTick(){
        timePassedInTicks++;
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

    public boolean isSelf() {
        return isSelf;
    }

    public void setOpponent(Character opponent) {
        this.opponent = opponent;
    }

    public int getDurationInTicks() {
        return durationInTicks;
    }

    public int getTimePassedInTicks() {
        return timePassedInTicks;
    }

    public int getTicksLeft() {
        if (durationInTicks == -1) return -1;
        return durationInTicks-timePassedInTicks;
    }

    public String getSkillNameOrType() {
        return skillNameOrType;
    }

    public void setSkillNameOrType(String skillNameOrType) {
        this.skillNameOrType = skillNameOrType;
    }

    public ArrayList<String> AnalyzeSkillNameOrType(String skillNameOrType){
        ArrayList<String> nameAndType = new ArrayList<>();
        if ((skillNameOrType.split("\\.")).length > 1){
            nameAndType.add(skillNameOrType.split("\\.")[0]);
            nameAndType.add(skillNameOrType.split("\\.")[1]);
            return nameAndType;
        } else {
            nameAndType.add(skillNameOrType.split("\\.")[0]);
            return nameAndType;
        }

    }
}
