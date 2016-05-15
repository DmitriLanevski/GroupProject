package gameLogic.skills;

import gameLogic.buffs.Buff;
import gameLogic.characters.Character;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lanev_000 on 15.05.2016.
 */
public class ChangeStatusSkill extends SingleCostSkill {
    private final String statusName;
    private final int change;

    public ChangeStatusSkill(String skillNameOrType, String statusName, int change, int cost, String stat, int cooldown) {
        super(skillNameOrType, cooldown, cost, stat);
        this.statusName = statusName;
        this.change = change;
    }

    @Override
    public void use(Character user, Character opponent) {
        super.use(user, opponent);
        user.eventAttack();
        opponent.eventDefend();

        opponent.eventChangeStatusBy(statusName, change);
        user.eventChangeStatusBy(super.getStat(), super.getCost());
    }

    public String getStatusName() {
        return statusName;
    }

    public int getChange() {
        return change;
    }
}
