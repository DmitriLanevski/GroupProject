package serverDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Stas on 17/04/2016.
 */
public class CharacterStatDatabase {
    /*protected static boolean CheckCharStatExistence(Connection conn, int charID, String statName) throws SQLException {
        String checkCommand = "SELECT StatName FROM CharData WHERE " +
                "CharacterID = ? AND StatName = ?;";
        PreparedStatement statement = conn.prepareStatement(checkCommand);
        statement.setInt(1,charID);
        statement.setString(1,statName);
        ResultSet rs =statement.executeQuery();
        while (rs.next()) return true;
        return false;
    }*/
    public static boolean addStat(Connection conn, int charID, String[] statNames, int[] statValues) throws SQLException {
        String checkCommand = "DELETE FROM CharData WHERE CharacterID = ?;";
        PreparedStatement statement = conn.prepareStatement(checkCommand);
        statement.setInt(1,charID);
        statement.executeQuery();
        if (statNames.length != statValues.length) return false;
        for (int i = 0; i<statNames.length; i++) {
            checkCommand = "INSERT INTO CharData(CharacterID,StatName,StatValue) VALUES (?,?,?);";
            statement = conn.prepareStatement(checkCommand);
            statement.setInt(1,charID);
            statement.setString(2,statNames[i]);
            statement.setInt(3,statValues[i]);
            statement.executeQuery();
        }
        return true;
    }
}
