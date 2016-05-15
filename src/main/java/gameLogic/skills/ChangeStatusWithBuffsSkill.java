package gameLogic.skills;

import gameLogic.buffs.Buff;
import gameLogic.characters.Character;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lanev_000 on 15.05.2016.
 */
public class ChangeStatusWithBuffsSkill extends ChangeStatusSkill{
    private ArrayList<Buff> buffList;

    private final List<String> reqs = new ArrayList<>();

    public ChangeStatusWithBuffsSkill(String skillNameOrType, String statusName, int change, int cost, String stat,
                                      int cooldown, ArrayList<Buff> buffList) {
        super(skillNameOrType, statusName, change, cost, stat, cooldown);
        this.buffList = buffList;
        reqs.add(stat);
    }

    @Override
    public void use(Character user, Character opponent) {
        super.use(user, opponent);
        user.eventAttack();
        opponent.eventDefend();
        opponent.eventChangeStatusBy(super.getStatusName(), super.getChange());
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

    @Override
    public List<String> requiredStats() {
        return reqs;
    }
}
