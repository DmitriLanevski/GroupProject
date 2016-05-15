package gameLogic.buffs;

import gameLogic.characters.Character;

import java.util.List;

/**
 * Created by Madis on 15.05.2016.
 */
public class BuffApplier {
    private boolean selfApply;
    private List<Buff> buffs;

    public BuffApplier(boolean selfApply, List<Buff> buffs) {
        this.selfApply = selfApply;
        this.buffs = buffs;
    }

    public void applyBuffs(Character user, Character opponent, String skillNameOrType) {
        for (Buff buff : buffs) {
            Buff newBuff = buff.clone();
            if (selfApply) {
                newBuff.setUser(user);
                newBuff.setOpponent(opponent);
                newBuff.setSkillNameOrType(skillNameOrType);
                user.addBuff(newBuff);
            } else {
                newBuff.setUser(opponent);
                newBuff.setOpponent(user);
                newBuff.setSkillNameOrType(skillNameOrType);
                opponent.addBuff(newBuff);
            }
        }
    }
}
