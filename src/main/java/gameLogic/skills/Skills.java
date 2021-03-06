package gameLogic.skills;

import gameLogic.buffs.*;
import gameLogic.characters.Character;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Skills {
    static private HashMap<String, Skill> skillCollection = new HashMap<>();
    static private HashMap<String, String> skillDescriptions = new HashMap<>();

    static private Character dummyChar = new Character("", new HashMap<>(), new HashMap<>());

    static {

        addSkill("Ferocious Strike", "PLACEHOLDER_DESC",
                new AttackSkill("Ferocious Strike.phys", 30, -10, "Stamina", 30));

        addSkill("Berserk's Strike", "PLACEHOLDER_DESC",
                new AttackSkill("Berserker Strike.phys", 50, -10, "Health", 30));

        addSkill("Rest to heal", "PLACEHOLDER_DESC",
                new ChangeStatusSkill("Rest to heal", "Health", 35, -10, "Stamina", 30, true));

        addSkill("Magical acupuncture", "PLACEHOLDER_DESC",
                new ChangeStatusSkill("Magical acupuncture", "Health", 35, -10, "Mana", 30, true));

        addSkill("Willpower", "PLACEHOLDER_DESC",
                new ChangeStatusSkill("Willpower", "Stamina", 100, -40, "Mana", 30, true));

        addSkill("Brace yourself", "PLACEHOLDER_DESC",
                new AddBuffsSkill("Brace yourself", -10, "Stamina", 30,
                        new BuffApplier(Arrays.asList(
                                new SimpleStatBuff(true, dummyChar, dummyChar, "", "Strength", 20, 20)))));

        addSkill("Fortification", "PLACEHOLDER_DESC",
                new AddBuffsSkill("Fortification", -10, "Stamina", 30,
                        new BuffApplier(Arrays.asList(
                                new SimpleStatBuff(true, dummyChar, dummyChar,  "", "Strength", 20, 20)))));

        addSkill("Mind blessing", "PLACEHOLDER_DESC",
                new AddBuffsSkill("Mind blessing", -10, "Stamina", 30,
                        new BuffApplier(Arrays.asList(
                                new SimpleStatBuff(true, dummyChar, dummyChar,  "", "Intelligence", 20, 20)))));

        addSkill("Force shield", "PLACEHOLDER_DESC",
                new AddBuffsSkill("Force shield", -10, "Mana", 30,
                        new BuffApplier(Arrays.asList(
                                new SimpleStatBuff(true, dummyChar, dummyChar,  "", "Defence", 20, 20)))));

        addSkill("[Passive] Regeneration", "Gain 0.1 health, mana and stamina each tick.",
                new PassiveSkill("Regeneration",
                        new BuffApplier( Arrays.asList(
                                new StatOverTimeBuff(true, dummyChar, dummyChar, "", "Health", -1, 0.1),
                                new StatOverTimeBuff(true, dummyChar, dummyChar, "", "Stamina", -1, 0.1),
                                new StatOverTimeBuff(true, dummyChar, dummyChar, "", "Mana", -1, 0.1)
                        ))
                )
        );

        addSkill("[Passive] Berserker's endurance", "Adds 100% of damage taken as stamina.",
                new PassiveSkill("Berserker's endurance",
                        new BuffApplier( Arrays.asList(
                                new DamageConversionBuff(true, dummyChar, dummyChar, "", -1, 1, "Stamina")
                        ))
                )
        );

        addSkill("[Passive] Static charge", "Adds 100% of mana used as static charge. Static charge occasionally unloads itself.",
                new PassiveSkill("Berserker's endurance",
                        new BuffApplier( Arrays.asList(
                                new LossConversionBuff(true, dummyChar, dummyChar, "", -1, 1, "Mana", "Static charge"),
                                new StaticStormBuff(true, dummyChar, dummyChar, "", -1, 0.1, 0.1, 1)
                        ))
                )
        );
        getSkillByName("[Passive] Static charge").addRequiredStat("Static charge");


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

    public static HashMap<String, String> getAllSkillDescriptions() {
        return skillDescriptions;
    }
}
