package gameLogic.skills;

import gameLogic.buffs.SimpleStatBuff;
import gameLogic.characters.Character;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lanev_000 on 14.05.2016.
 */
public class SimpleBuffDeBuffSkill extends Skill {
    private final int buffValue;
    private final String affectionStat;
    private final int duration;
    private final boolean isSelf;
    private final int cost;
    private final String costStat;
    private final int cooldown;

    private final List<String> reqs = new ArrayList<>();

    public SimpleBuffDeBuffSkill(int buffValue, String affectionStat, int duration, boolean isSelf, int cost, String costStat, int cooldown) {
        this.buffValue = buffValue;
        this.affectionStat = affectionStat;
        this.duration = duration;
        this.isSelf = isSelf;
        this.cost = cost;
        this.costStat = costStat;
        this.cooldown = cooldown;
    }

    @Override
    public void use(Character user, Character opponent) {
        super.use(user, opponent);
        user.addBuff(new SimpleStatBuff(user,opponent,duration,buffValue,affectionStat,isSelf));
    }

    @Override
    public List<String> requiredStats() {
        return null;
    }

    @Override
    public boolean canUse(Character user, Character opponent, int ticksSinceLastUse) {
        return user.getStatusValue(costStat) >= cost & ticksSinceLastUse >= cooldown;
    }
}
