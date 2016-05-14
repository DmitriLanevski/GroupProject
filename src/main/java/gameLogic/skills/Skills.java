package gameLogic.skills;

import java.util.HashMap;

public class Skills {
    static private HashMap<String, Skill> skillCollection = new HashMap<>();
    static private HashMap<String, String> skillDescriptions = new HashMap<>();

    static {
        addSkill("Ferocious Strike","PLACEHOLDER_DESC", new SimpleAttack(10, 20, -10, "Stamina"));
        addSkill("Berserker Strike","PLACEHOLDER_DESC", new SimpleAttack(10, 50, -10, "Health"));
        addSkill("Rest to heal", "PLACEHOLDER_DESC", new SimpleBuffDeBuffSkill(35,"Health",-1,true,-20,"Stamina",10));
        addSkill("Magical acupuncture", "PLACEHOLDER_DESC", new SimpleBuffDeBuffSkill(35,"Health",-1,true,-20,"Mana",10));
        addSkill("Willpower", "PLACEHOLDER_DESC", new SimpleBuffDeBuffSkill(100,"Stamina",-1,true,-40,"Mana",30));
        addSkill("Brace yourself", "PLACEHOLDER_DESC", new SimpleBuffDeBuffSkill(20,"Strength",30,true,-10,"Stamina",20));
        addSkill("Fortification", "PLACEHOLDER_DESC", new SimpleBuffDeBuffSkill(20,"Defence",30,true,-10,"Stamina",20));
        addSkill("Mind blessing", "PLACEHOLDER_DESC", new SimpleBuffDeBuffSkill(35,"Intelligence",30,true,-10,"Mana",20));
        addSkill("Force shield", "PLACEHOLDER_DESC", new SimpleBuffDeBuffSkill(35,"Defence",30,true,-10,"Mana",20));

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
