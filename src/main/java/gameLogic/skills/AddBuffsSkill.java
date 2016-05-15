package gameLogic.skills;

import gameLogic.buffs.Buff;
import gameLogic.buffs.SimpleStatBuff;
import gameLogic.characters.Character;
import gameLogic.skills.Skill;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lanev_000 on 14.05.2016.
 */
public class AddBuffsSkill extends SingleCostSkill {
    private ArrayList<Buff> buffList;

    public AddBuffsSkill(String skillNameOrType, int cost, String stat, int cooldown, ArrayList<Buff> buffList) {
        super(skillNameOrType, cooldown, cost, stat);
        this.buffList = buffList;
    }

    @Override
    public void use(Character user, Character opponent) {
        super.use(user, opponent);
        for (Buff buff : buffList) {
            buff.setUser(user);
            buff.setOpponent(opponent);
            buff.setSkillNameOrType(super.getSkillNameOrType());
            if (buff.isSelf()){
                user.addBuff(buff);
            } else {
                opponent.addBuff(buff);
            }
        }
    }
}
