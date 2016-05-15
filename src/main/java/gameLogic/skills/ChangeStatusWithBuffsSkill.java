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
                                      int cooldown, BuffApplier buffList) {
        super(skillNameOrType, statusName, change, cost, stat, cooldown);
        this.buffList = buffList;
    }

    @Override
    public void use(Character user, Character opponent) {
        super.use(user, opponent);
        user.eventAttack();
        opponent.eventDefend();

        opponent.eventChangeStatusBy(super.getStatusName(), super.getChange());
        buffList.applyBuffs(user, opponent, getSkillNameOrType());

        user.eventChangeStatusBy(super.getStat(), super.getCost());
    }
}
