package gameLogic.Skills;
import gameLogic.buffs.BasicAtack;

import java.util.HashMap;

/**
 * Created by lanev_000 on 16.04.2016.
 */
public class SkillCollection {
    private HashMap<String, Skill> skillCollection = new HashMap<>();

    public synchronized HashMap<String, Skill> load() {
        Skill basicAtack = new Skill();
        basicAtack.getBuffs().add(new BasicAtack());
        skillCollection.put("BasicAttack", basicAtack);
        return skillCollection;
    }
}
