package gameLogic.skills;

import gameLogic.buffs.BuffApplier;
import gameLogic.characters.Character;

/**
 * Created by Madis on 19.05.2016.
 */
public class PassiveSkill extends Skill {
    private final BuffApplier buffs;

    public PassiveSkill(String skillNameOrType, BuffApplier buffs) {
        super(skillNameOrType);
        this.buffs = buffs;
    }

    @Override
    public void onGameStart(Character user, Character opponent) {
        buffs.applyBuffs(user, opponent, getSkillNameOrType());
    }

    @Override
    public boolean canUse(Character user, Character opponent, int ticksSinceLastUse) {
        return false;
    }
}
