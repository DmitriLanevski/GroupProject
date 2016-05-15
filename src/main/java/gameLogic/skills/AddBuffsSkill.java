package gameLogic.skills;

import gameLogic.buffs.Buff;
import gameLogic.buffs.BuffApplier;
import gameLogic.buffs.SimpleStatBuff;
import gameLogic.characters.Character;
import gameLogic.skills.Skill;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lanev_000 on 14.05.2016.
 */
public class AddBuffsSkill extends SingleCostSkill {
    private BuffApplier buffList;

    public AddBuffsSkill(String skillNameOrType, int cost, String stat, int cooldown, BuffApplier buffList) {
        super(skillNameOrType, cooldown, cost, stat);
        this.buffList = buffList;
    }

    @Override
    public void use(Character user, Character opponent) {
        super.use(user, opponent);
        buffList.applyBuffs(user, opponent, getSkillNameOrType());
        applyCost(user);
    }
}
