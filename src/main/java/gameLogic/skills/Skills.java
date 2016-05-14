package gameLogic.skills;

import java.util.HashMap;

public class Skills {
    static private HashMap<String, Skill> skillCollection = new HashMap<>();
    static private HashMap<String, String> skillDescriptions = new HashMap<>();

    static {
        addSkill("Ferocious Strike","PLACEHOLDER_DESC", new SimpleAttack(10, 20, -10, "Stamina"));
        addSkill("Berserker Strike","PLACEHOLDER_DESC", new SimpleAttack(10, 50, -10, "Health"));

    }

    private static void addSkill(String name, String desc, Skill skill) {
        skillCollection.put(name, skill);
        skillDescriptions.put(name, desc);
    }

    public static Skill getSkillByName(String name) {
        return skillCollection.get(name);
    }

    public static String getSkillDescByName(String name) {
        return skillDescriptions.get(name);
    }

    public static HashMap<String, String> getAllSkillDescriptions() { return skillDescriptions; }
}
