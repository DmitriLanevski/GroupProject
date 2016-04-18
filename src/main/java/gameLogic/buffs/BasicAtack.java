package gameLogic.buffs;

import gameLogic.characters.Character;

/**
 * Created by lanev_000 on 16.04.2016.
 */
public class BasicAtack extends Buff{
    public boolean isExpired = true;

    @Override
    public synchronized boolean isExpired() {
        return isExpired;
    }

    public synchronized void onApplied(Character user, Character target, String usedSkillName) {
        double value;
        user.getActiveBuffs().forEach((Buff buff)->buff.onAttack(user, target, usedSkillName));
        target.getActiveBuffs().forEach((Buff buff)->buff.onDefend(target, user, usedSkillName));
        value = (user.getStatus().get("Strength").getValue()-0.5*target.getStatus().get("Defence").getValue())*2;
        target.affectStatus("Health", -value);
        if (value > 0){
            target.getActiveBuffs().forEach((Buff buff)->buff.onDamageTaken(target, user, usedSkillName, value));
            user.getActiveBuffs().forEach((Buff buff)->buff.onDamageDealt(user, target, usedSkillName, value));
        }
    }
}
