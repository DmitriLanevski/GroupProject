package gameLogic;

import gameLogic.attributes.Stat;
import gameLogic.attributes.Stats;
import gameLogic.characters.Character;
import gameLogic.skills.Skills;
import serverDatabase.CharacterData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Game {
    public static Character createCharacter(CharacterData rawData) {
        HashMap<String, Integer> skills = new HashMap<>();
        HashMap<String, Stat> stats = Stats.getUniversals();

        for (String skillName : rawData.getSkillIDs()) {
            skills.put(skillName, 0);
        }

        // Initializes all the stored stats and adds allocated additional points.
        Set<String> neccessaryStats = new HashSet<>();

        neccessaryStats.addAll(rawData.getStatIDs().keySet());
        for (String skillName : rawData.getSkillIDs()) {
            neccessaryStats.addAll( Skills.getSkillByName(skillName).getRequiredStats() );
        }

        for (String neccessaryStat : neccessaryStats) {
            if (!stats.containsKey(neccessaryStat)) stats.put(neccessaryStat, Stats.getDefaultValueOf(neccessaryStat));
        }

        for (String statID : rawData.getStatIDs().keySet()) {
            stats.get(statID).increaseMax(Stats.getGrowthRateOf(statID)*rawData.getStatIDs().get(statID));
            if (Stats.getDefaultValueOf(statID).getValue() == 0) stats.get(statID).setValue(0);
        }

        return new Character(rawData.getCharName(), skills, stats);
    }
}
