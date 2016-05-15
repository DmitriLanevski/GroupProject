package gameLogic.skills;

import gameLogic.buffs.Buff;
import gameLogic.characters.Character;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lanev_000 on 15.05.2016.
 */
public class AttackSkill extends SingleCostSkill {
    private final int damage;

    public AttackSkill(String skillNameOrType, int cooldown, int cost, String stat, int damage) {
        super(skillNameOrType, cooldown, cost, stat);
        this.damage = damage;
    }

    @Override
    public void use(Character user, Character opponent) {
        super.use(user, opponent);
        user.eventAttack();
        opponent.eventDefend();

        opponent.eventDealDamage(damage);

        applyCost(user);
    }

    public int getDamage() {
        return damage;
    }
}
