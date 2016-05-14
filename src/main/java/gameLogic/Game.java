package gameLogic;

import gameLogic.attributes.Stat;
import gameLogic.attributes.Stats;
import gameLogic.characters.Character;
import serverDatabase.CharacterData;

import java.util.HashMap;

public class Game {
    public static Character createCharacter(CharacterData rawData) {
        HashMap<String, Integer> skills = new HashMap<>();
        HashMap<String, Stat> stats = Stats.getUniversals();

        for (String skillID : rawData.getSkillIDs()) {
            skills.put(skillID, 0);
        }

        System.out.println("Universals = " + stats);
        // Initializes all the stored stats and adds allocated additional points.
        for (String statID : rawData.getStatIDs().keySet()) {
            if (!stats.containsKey(statID)) stats.put(statID, Stats.getDefaultValueOf(statID));

            stats.get(statID).increaseMax(Stats.getGrowthRateOf(statID)*rawData.getStatIDs().get(statID));
        }

        return new Character(skills, stats);
    }
}
