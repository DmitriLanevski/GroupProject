package gameLogic.attributes;

import java.util.HashMap;

/**
 * Created by lanev_000 on 16.04.2016.
 */
public class StatusCollection {
    private HashMap<String, Status> statusCollection = new HashMap<>();
    private double defaultValue = 0;
    private double defaultMin = 0;
    private double defaultMax = 100;

    public synchronized HashMap<String, Status> load() {
        //Basic game parameters
        statusCollection.put("Health", new Status(defaultValue, defaultMin, defaultMax*100));
        statusCollection.put("Stamina", new Status(defaultValue, defaultMin, defaultMax*100));
        statusCollection.put("Mana", new Status(defaultValue, defaultMin, defaultMax*10));
        statusCollection.put("level", new Status(defaultValue, defaultMin, defaultMax));
        statusCollection.put("Experience", new Status(defaultValue, defaultMin, defaultMax*10000));
        statusCollection.put("Skillpoints", new Status(35, defaultMin, defaultMax*10000));
        //Skills and buffs affecting stats.
        statusCollection.put("Strength", new Status(defaultValue, defaultMin, defaultMax));
        statusCollection.put("Strength", new Status(defaultValue, defaultMin, defaultMax));
        statusCollection.put("Defence", new Status(defaultValue, defaultMin, defaultMax));
        statusCollection.put("Dexterity", new Status(defaultValue, defaultMin, defaultMax));
        statusCollection.put("Wisdom", new Status(defaultValue, defaultMin, defaultMax));
        statusCollection.put("Charisma", new Status(defaultValue, defaultMin, defaultMax));
        statusCollection.put("Luck", new Status(defaultValue, defaultMin, defaultMax));
        return statusCollection;
    }
}
