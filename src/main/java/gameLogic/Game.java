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

        for (String skillID : rawData.SkillIDs) {
            skills.put(skillID, 0);
        }

        // Initializes all the stored stats and adds allocated additional points.
        for (String statID : rawData.StatIDs.keySet()) {
            if (!stats.containsKey(statID)) stats.put(statID, Stats.getDefaultValueOf(statID));

            stats.get(statID).setMax(Stats.getGrowthRateOf(statID)*rawData.StatIDs.get(statID));
        }

        return new Character(skills, stats);
    }
}
