package serverDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Stas on 17/04/2016.
 */
public class Character_Skill_Database {
    protected static boolean CheckSkillExistence(Connection conn, String skillName) throws SQLException {
        String checkCommand = "SELECT SkillID FROM Skills WHERE " +
                "SkillName = ?;";
        PreparedStatement statement = conn.prepareStatement(checkCommand);
        statement.setString(1,skillName);
        ResultSet rs =statement.executeQuery();
        while (rs.next()) return true;
        return false;
    }
    public static void assignSkill(Connection conn, int charID, int[] skillIDs) throws SQLException {
        String checkCommand = "DELETE FROM SkillAssign WHERE CharacterID = ?;";
        PreparedStatement statement = conn.prepareStatement(checkCommand);
        statement.setInt(1,charID);
        statement.executeQuery();
        for (int i = 0; i<skillIDs.length; i++) {
            checkCommand = "INSERT INTO SkillAssign(CharacterID,SkillID) VALUES (?,?);";
            statement = conn.prepareStatement(checkCommand);
            statement.setInt(1,charID);
            statement.setInt(2,skillIDs[i]);
            statement.executeQuery();
        }
    }
}
