package gameLogic.skills;

import java.util.HashMap;

public class Skills {
    static private HashMap<String, Skill> skillCollection = new HashMap<>();

    static {
        skillCollection.put("Ferocious Strike", new SimpleAttack(10, 20, -10, "Stamina"));
        skillCollection.put("Berserker Strike", new SimpleAttack(10, 50, -10, "Health"));
    }

    public static Skill getSkillByName(String name) {
        return skillCollection.get(name);
    }
}
