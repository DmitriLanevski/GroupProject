package gameLogic.skills;

import gameLogic.characters.Character;

import java.util.ArrayList;
import java.util.List;

public class SimpleAttack extends Skill {
    private final int damage;
    private final int cooldown;
    private final int cost;
    private final String stat;

    private final List<String> reqs = new ArrayList<>();

    public SimpleAttack(int cooldown, int damage, int cost, String stat) {
        this.cooldown = cooldown;
        this.damage = damage;
        this.cost = cost;
        this.stat = stat;
        reqs.add(stat);
    }

    @Override
    public boolean canUse(Character user, Character opponent, int ticksSinceLastUse) {
        return user.getStatusValue(stat) >= cost & ticksSinceLastUse >= cooldown;
    }

    @Override
    public void use(Character user, Character opponent) {
        super.use(user, opponent);
        user.eventAttack();
        opponent.eventDefend();

        opponent.eventDealDamage(damage);
        user.eventChangeStatusBy(stat, cost);
    }

    @Override
    public List<String> requiredStats() {
        return reqs;
    }
}
