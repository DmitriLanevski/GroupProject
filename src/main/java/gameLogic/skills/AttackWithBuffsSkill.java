package gameLogic.skills;

import gameLogic.buffs.Buff;
import gameLogic.buffs.BuffApplier;
import gameLogic.characters.Character;
import gameLogic.skills.Skill;

import java.util.ArrayList;
import java.util.List;

public class AttackWithBuffsSkill extends AttackSkill {
    private BuffApplier buffList;

    public AttackWithBuffsSkill(String skillNameOrType, int cooldown, int cost, String stat, int damage, BuffApplier buffList) {
        super(skillNameOrType, cooldown, cost, stat, damage);
        this.buffList = buffList;
    }

    @Override
    public void use(Character user, Character opponent) {
        super.use(user, opponent);
        user.eventAttack();
        opponent.eventDefend();

        opponent.eventDealDamage(super.getDamage());
        buffList.applyBuffs(user, opponent, getSkillNameOrType());

        user.eventChangeStatusBy(super.getStat(), super.getCost());
    }

}
