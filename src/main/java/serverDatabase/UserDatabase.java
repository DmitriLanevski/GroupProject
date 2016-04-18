package serverDatabase;

import org.h2.tools.RunScript;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Stas on 17/04/2016.
 */
public class UserDatabase implements AutoCloseable {

    private final Connection conn;

    public UserDatabase() throws SQLException, IOException {
        conn = DriverManager.getConnection("jdbc:h2:./Database");
        loadInitialData();
    }

    @Override
    public void close() throws SQLException {
        conn.close();
    }

    public static void main(String[] args) throws Exception {
        UserDatabase database = new UserDatabase();
        System.out.println("checkUserExistence:"+ database.checkUserExistence("Alpha"));
        System.out.println("registerUser:"+database.registerUser("Alpha","12345"));
        System.out.println("Password Change:"+database.changePassword("Alpha","12345"));
        System.out.println("checkUserExistence:"+ database.checkUserExistence("Alpha"));
        System.out.println("logIn:"+database.logIn("Alpha","12345"));
        database.dumpTable();
        database.close();
    }
    public boolean checkUserExistence(String username) throws SQLException {
        String checkCommand = "SELECT LoginName FROM UserDatabase WHERE " +
                "LoginName = ?;";
        PreparedStatement statement = conn.prepareStatement(checkCommand);
        statement.setString(1,username);
        ResultSet rs =statement.executeQuery();
        return rs.next();

    }
    public boolean registerUser(String username, String password) throws Exception {
        if (!checkUserExistence(username)) {
            String addUser = "INSERT INTO UserDatabase(LoginName,Password) VALUES (?,?);";
            PreparedStatement statement = conn.prepareStatement(addUser);
            statement.setString(1,username);
            statement.setString(2,Encryption.Encrypt(password));
            statement.execute();
            return true;
        }
        return false;
    }
    public boolean changePassword(String username, String password) throws Exception {
        if (checkUserExistence(username)) {
            String addUser = "UPDATE UserDatabase SET password=? WHERE loginName=?;";
            PreparedStatement statement = conn.prepareStatement(addUser);
            statement.setString(1,Encryption.Encrypt(password));
            statement.setString(2,username);
            statement.execute();
            return true;
        }
        return false;
    }
    public boolean logIn (String username, String password) throws Exception {
        if (checkUserExistence(username)) {

            String getPassword = "SELECT Password FROM UserDatabase WHERE LoginName = ?;";
            PreparedStatement statement = conn.prepareStatement(getPassword);
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String CharName = rs.getString("Password");
                if (Encryption.Check(password, CharName)) return true;
            }
        }
        return false;
    }

    public boolean createSkill(String skillName, String skillDesc) throws SQLException {
        if (!skillName.equals("")) {
            String command = "INSERT INTO Skills(SkillName,SkillDesc) VALUES (?,?);";
            PreparedStatement statement = conn.prepareStatement(command);
            statement.setString(1,skillName);
            statement.setString(2,skillDesc);
            statement.execute();
            return true;
        }
        return false;
    }
    public Map<String,String> showAllSkills() throws SQLException {
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

    public boolean addStat(int charID, String[] statNames, int[] statValues) throws SQLException {
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

    public boolean checkCharExistence(String userName, String charName) throws SQLException {

        if (checkUserExistence(userName)) {
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

    public boolean createChar(String userName,  String charName) throws SQLException {
        if (!checkCharExistence(userName, charName)) {
            String addCharacter = "INSERT INTO CharacterDatabase VALUES (?,?,0);";
            PreparedStatement statement = conn.prepareStatement(addCharacter);
            statement.setString(1,userName);
            statement.setString(2,charName);
            statement.execute();
            return true;
        }
        return false;
    }

    public Map<String,Integer> showExistingChars(String userName) throws SQLException {
        Map<String,Integer> theUltimateString = new HashMap<>();
        if (checkUserExistence(userName)) {
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

    public boolean checkSkillExistence(String skillName) throws SQLException {
        String checkCommand = "SELECT SkillID FROM Skills WHERE " +
                "SkillName = ?;";
        PreparedStatement statement = conn.prepareStatement(checkCommand);
        statement.setString(1,skillName);
        ResultSet rs =statement.executeQuery();
        while (rs.next()) return true;
        return false;
    }

    public void assignSkill(int charID, int[] skillIDs) throws SQLException {
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



    private void loadInitialData() throws SQLException, IOException {
        try (Reader reader = new InputStreamReader(
                UserDatabase.class.getClassLoader().getResourceAsStream("serverDatabase.sql"), "UTF-8")) {
            RunScript.execute(conn, reader);
        }
    }

    public void dumpTable() throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * from UserDatabase")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("LoginName") + "\t" + rs.getString("Password"));
            }
        }
    }
}
