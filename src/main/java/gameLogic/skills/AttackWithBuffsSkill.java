package gameLogic.skills;

import gameLogic.buffs.Buff;
import gameLogic.characters.Character;
import gameLogic.skills.Skill;

import java.util.ArrayList;
import java.util.List;

public class AttackWithBuffsSkill extends AttackSkill {
    private ArrayList<Buff> buffList;

    public AttackWithBuffsSkill(String skillNameOrType, int cooldown, int cost, String stat, int damage, ArrayList<Buff> buffList) {
        super(skillNameOrType, cooldown, cost, stat, damage);
        this.buffList = buffList;
    }

    @Override
    public void use(Character user, Character opponent) {
        super.use(user, opponent);
        user.eventAttack();
        opponent.eventDefend();
        opponent.eventDealDamage(super.getDamage());
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
        user.eventChangeStatusBy(super.getStat(), super.getCost());
    }

}
