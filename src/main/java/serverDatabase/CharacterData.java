package serverDatabase;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Stas on 25/04/2016.
 */
public class CharacterData{
    private int CharID;
    private String CharName;
    private List<String> SkillIDs;
    private Map<String,Long> StatIDs;
    private int CharEXP;

    public CharacterData(int charID, String charName, List<String> skillIDs, Map<String, Long> statIDs, int charEXP) {
        CharID = charID;
        CharName = charName;
        SkillIDs = skillIDs;
        StatIDs = statIDs;
        CharEXP = charEXP;
    }

    public void setCharID(int charID) {
        CharID = charID;
    }
    public int getCharID() {
        return CharID;
    }

    public String getCharName() {
        return CharName;
    }

    public List<String> getSkillIDs() {
        return SkillIDs;
    }

    public Map<String, Long> getStatIDs() {
        return StatIDs;
    }

    public int getCharEXP() {
        return CharEXP;
    }
}
