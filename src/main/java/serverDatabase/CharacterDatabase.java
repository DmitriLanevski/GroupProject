package serverDatabase;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Stas on 17/04/2016.
 */
public class CharacterDatabase extends UserDatabase {
    protected static boolean checkCharExistence(Connection conn, String userName, String charName) throws SQLException {
        if (CheckUserExistence(conn,userName)) {
            String checkCommand = "SELECT CharacterId FROM CharacterDatabase WHERE " +
                    "CharacterName = ? AND UserID = " +
                    "(SELECT UserID FROM UserDatabase WHERE LoginName = ?);";
            PreparedStatement statement = conn.prepareStatement(checkCommand);
            statement.setString(1,charName);
            statement.setString(2,userName);
            return statement.execute();
        }
        return false;
    }
    public static boolean createChar(Connection conn, String userName,  String charName) throws SQLException {
        if (!checkCharExistence(conn,userName, charName)) {
            String addCharacter = "INSERT INTO CharacterDatabase VALUES (?,?,0);";
            PreparedStatement statement = conn.prepareStatement(addCharacter);
            statement.setString(1,userName);
            statement.setString(2,charName);
            statement.execute();
            return true;
        }
        return false;
    }
    public static Map<String,Integer> showExistingChars(Connection conn, String userName) throws SQLException {
        Map<String,Integer> theUltimateString = new HashMap<>();
        if (CheckUserExistence(conn,userName)) {
            Statement stmt = null;
            String allChars = "SELECT CharacterName,CharacterEXP FROM CharacterDatabase WHERE LoginName = ?";
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(allChars);
            while (rs.next()) {
                String CharName = rs.getString("CharacterName");
                Integer CharEXP = rs.getInt("CharacterEXP");
                System.out.println(CharName + ":\t" + CharEXP);
                theUltimateString.put(CharName,CharEXP);
            }
        }
        return theUltimateString;
    }
}
