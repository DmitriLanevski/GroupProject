package serverDatabase;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Stas on 17/04/2016.
 */
public class SkillDatabase {
    public static boolean createSkill(Connection conn,String skillName, String skillDesc) throws SQLException {
        if (skillName != "") {
            String command = "INSERT INTO Skills(SkillName,SkillDesc) VALUES (?,?);";
            PreparedStatement statement = conn.prepareStatement(command);
            statement.setString(1,skillName);
            statement.setString(2,skillDesc);
            statement.execute();
            return true;
        }
        return false;
    }
    public static Map<String,String> showAllSkills(Connection conn) throws SQLException {
        Map<String, String > theUltimateString = new HashMap<>();
            Statement stmt = null;
            String allChars = "SELECT SkillName,SkillDesc FROM Skills";
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(allChars);
            while (rs.next()) {
                String CharName = rs.getString("SkillName");
                String CharEXP = rs.getString("SkillDesc");
                System.out.println(CharName + ":\t" + CharEXP);
                theUltimateString.put(CharName, CharEXP);
            }
        return theUltimateString;
    }
}
