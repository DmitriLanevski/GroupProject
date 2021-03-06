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
    private final boolean selfApply;

    public ChangeStatusSkill(String skillNameOrType, String statusName, int change, int cost, String stat, int cooldown, boolean selfApply) {
        super(skillNameOrType, cooldown, cost, stat);
        this.statusName = statusName;
        this.change = change;
        this.selfApply = selfApply;
    }

    @Override
    public void use(Character user, Character opponent) {
        super.use(user, opponent);
        if (selfApply) {
            user.eventChangeStatusBy(statusName, change);
        } else {
            opponent.eventChangeStatusBy(statusName, change);
        }
        applyCost(user);
    }

    public String getStatusName() {
        return statusName;
    }

    public int getChange() {
        return change;
    }
}
