package gameLogic.attributes;

import java.util.HashMap;

public class Stats {
    private static HashMap<String, Stat> defaultValues = new HashMap<>();
    private static HashMap<String, Stat> universal = new HashMap<>();
    private static HashMap<String, Double> growthRate = new HashMap<>();

    static {
        defaultValues.put("Health", new Stat(100, -100, 100));
        defaultValues.put("Static charge", new Stat(0, 0, 10));

        everyoneHas("Health");
        everyoneHas("Stamina");
        everyoneHas("Mana");
        everyoneHas("Strength");
        everyoneHas("Defence");
        everyoneHas("Intelligence");
    }

    private static void everyoneHas(String statName) {
        universal.put(statName, getDefaultValueOf(statName));
    }

    public static Stat getDefaultValueOf(String statName) {
        if (defaultValues.containsKey(statName))
            return new Stat(defaultValues.get(statName));
        else
            return new Stat(100, 0, 100);
    }

    public static double getGrowthRateOf(String statName) {
        if (growthRate.containsKey(statName)) {
            return growthRate.get(statName);
        }
        else return 10;
    }

    public static HashMap<String, Stat> getUniversals() {
        HashMap<String, Stat> all = new HashMap<>(universal);
        all.replaceAll( (n, stat)->new Stat(stat) ); // creates new instances
        return all;
    }
}
