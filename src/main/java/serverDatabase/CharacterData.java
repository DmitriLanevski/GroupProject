package serverDatabase;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Stas on 25/04/2016.
 */
public class CharacterData{
    public int CharID;
    public List<Integer> SkillIDs;
    public Map<String,Long> StatIDs;

    public CharacterData(int charID, List<Integer> skillIDs, Map<String,Long> statIDs) throws SQLException, IOException {
        CharID = charID;
        SkillIDs = skillIDs;
        StatIDs = statIDs;
    }
}
