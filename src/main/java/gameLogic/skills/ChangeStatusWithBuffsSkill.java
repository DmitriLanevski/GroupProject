package gameLogic.skills;

import gameLogic.buffs.Buff;
import gameLogic.buffs.BuffApplier;
import gameLogic.characters.Character;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lanev_000 on 15.05.2016.
 */
public class ChangeStatusWithBuffsSkill extends ChangeStatusSkill{
    private BuffApplier buffList;

    public ChangeStatusWithBuffsSkill(String skillNameOrType, String statusName, int change, int cost, String stat,
                                      int cooldown, boolean selfApply, BuffApplier buffList) {
        super(skillNameOrType, statusName, change, cost, stat, cooldown, selfApply);
        this.buffList = buffList;
    }

    @Override
    public void use(Character user, Character opponent) {
        super.use(user, opponent);
        buffList.applyBuffs(user, opponent, getSkillNameOrType());
    }
}
