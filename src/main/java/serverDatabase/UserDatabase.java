package serverDatabase;

import gameLogic.characters.Character;
import org.h2.tools.RunScript;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Stas on 17/04/2016.
 */
public class UserDatabase implements AutoCloseable {

    private final Connection conn;

    // getAllChars returns all Characters associated with this user.
    public synchronized List<CharacterData> getAllChars(String userName) throws Exception {
        List<CharacterData> charList = new ArrayList<>();
        String checkCommand = "SELECT CharacterId FROM CharacterDatabase WHERE " +
                "LoginName = ?;";
        PreparedStatement statement = conn.prepareStatement(checkCommand);
        statement.setString(1, userName);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            int charID = rs.getInt("CharacterId");
            CharacterData theChar = getCharData(charID);
            charList.add(theChar);
        }
        return charList;
    }

    // getCharData return a single Character data based on ID of a character
    public synchronized CharacterData getCharData(int charID) throws SQLException {
        List<String> charSkills = showCharSkills(charID);
        Map<String,Long> charStats = showAllStats(charID);
        String checkCommand = "SELECT CharacterName FROM CharacterDatabase WHERE " +
                "CharacterId = ?;";
        PreparedStatement statement = conn.prepareStatement(checkCommand);
        statement.setInt(1, charID);
        ResultSet rs = statement.executeQuery();
        String charName = rs.getString("CharacterName");
        CharacterData theChar = new CharacterData(charID,charName,charSkills,charStats);
        return theChar;
    }


    //TODO: finish the character saving
    public synchronized boolean saveCharData(CharacterData characterData, String userName) throws SQLException {
        String checkCommand = "INSERT INTO CharacterName (CharacterId,LoginName,CharacterName,CharacterEXP) VALUES " +
                "(?,?,?,0);";
        PreparedStatement statement = conn.prepareStatement(checkCommand);
        statement.setInt(1,characterData.getCharID());
        statement.setString(2, userName);
        statement.setString(3,characterData.getCharName());
        ResultSet rs = statement.executeQuery();
        saveChar(characterData);
        return rs.first();
    }
    public synchronized boolean saveChar(CharacterData characterData) throws SQLException {
        String checkCommand = "INSERT INTO CharData (CharacterId,StatName,StatValue) VALUES " +
                "(?,?,?);";
        PreparedStatement statement = conn.prepareStatement(checkCommand);
        statement.setInt(1,characterData.getCharID());
        Map<String,Long> theMap = characterData.getStatIDs();
        for ( String key : theMap.keySet() ) {
            statement.setString(2, key);
            statement.setLong(3,theMap.get(key));
            ResultSet rs = statement.executeQuery();
        }
        assignSkill(characterData.getCharID(),characterData.getSkillIDs());
        return true;
    }

    public UserDatabase() throws SQLException, IOException {
        conn = DriverManager.getConnection("jdbc:h2:./Database");
        loadInitialData();
    }

    @Override
    public synchronized void close() throws SQLException {
        conn.close();
    }

    public synchronized static void main(String[] args) throws Exception {
        UserDatabase database = new UserDatabase();
        //System.out.println("checkUserExistence:"+ database.checkUserExistence("Alpha"));
        System.out.println("registerUser:"+database.registerUser("Admin",Bcrypt.hashpw("GrandAdmin",Bcrypt.gensalt())));
        //System.out.println("Password Change:"+database.changePassword("Alpha","12345"));
        //System.out.println("checkUserExistence:"+ database.checkUserExistence("Alpha"));
        //System.out.println("logIn:"+database.logIn("Alpha","12345"));
        database.dumpTable();
        database.close();
    }

    // Checking if a given user even exists.
    public synchronized boolean checkUserExistence(String username) throws SQLException {
        String checkCommand = "SELECT LoginName FROM UserDatabase WHERE " +
                "LoginName = ?;";
        PreparedStatement statement = conn.prepareStatement(checkCommand);
        statement.setString(1,username);
        ResultSet rs =statement.executeQuery();
        return rs.next();

    }

    // Obvious. Registers a user with given Nickname+PW
    public synchronized boolean registerUser(String username, String password) throws Exception {
        if (!checkUserExistence(username)) {
            String addUser = "INSERT INTO UserDatabase(LoginName,Password) VALUES (?,?);";
            PreparedStatement statement = conn.prepareStatement(addUser);
            statement.setString(1,username);
            statement.setString(2,password);
            statement.execute();
            return true;
        }
        return false;
    }

    // Alters password. Needs further security checks on Server side
    public synchronized boolean changePassword(String username, String password) throws Exception {
        if (checkUserExistence(username)) {
            String addUser = "UPDATE UserDatabase SET password=? WHERE loginName=?;";
            PreparedStatement statement = conn.prepareStatement(addUser);
            statement.setString(1,password);
            statement.setString(2,username);
            statement.execute();
            return true;
        }
        return false;
    }

    // Login method. returns true if a given user exists and the Passwords match, false otherwise.
    public synchronized boolean logIn (String username, String password) throws Exception {
        if (checkUserExistence(username)) {

            String getPassword = "SELECT Password FROM UserDatabase WHERE LoginName = ?;";
            PreparedStatement statement = conn.prepareStatement(getPassword);
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String thePassword = rs.getString("Password");
                if (password.equals(thePassword)) return true;
            }
        }
        return false;
    }

    // Method to add skills into the database
    public synchronized boolean createSkill(String skillName, String skillDesc) throws SQLException {
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

    // Returns a map with skills and their descriptions. ALL skills.
    public synchronized Map<String,String> showAllSkills() throws SQLException {
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

    // Returns skills that are used by a certain character
    public synchronized List<String> showCharSkills(int CharID) throws SQLException {
        List<String> skillsOfCharN = new ArrayList<>();
        String allCharSkills = "SELECT SkillName FROM SkillAssign WHERE CharID = ?";
        PreparedStatement stmt = conn.prepareStatement(allCharSkills);
        stmt.setInt(1,CharID);
        ResultSet rs = stmt.executeQuery(allCharSkills);
        while (rs.next()) {
            String SkillName = rs.getString("SkillName");
            skillsOfCharN.add(SkillName);
        }
        return skillsOfCharN;
    }

    // Possibility to add stats to a charcter. Requires input of all Stats.
    // Impossible to add stats later (look at the checkCommand).
    public boolean addStats(int charID, String[] statNames, int[] statValues) throws SQLException {
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

    // Similiar to checkUserExistence. Controls if given user has given character.
    public synchronized boolean checkCharExistence(String userName, String charName) throws SQLException {

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

    // creates an empty Statless and Skillless character bound to a given user.
    public synchronized boolean createChar(String userName,  String charName) throws SQLException {
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

    // In testing. Possibly redundant.
    public synchronized Map<String,Integer> showExistingChars(String userName) throws SQLException {
        Map<String,Integer> theUltimateString = new HashMap<>();
        if (checkUserExistence(userName)) {
            String allChars = "SELECT CharacterName,CharacterEXP FROM CharacterDatabase WHERE LoginName = ?";
            Statement stmt = conn.createStatement();
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

    // Controls if a skill exists. Prevents Players from assigning non-existing skills to themselves
    public synchronized boolean checkSkillExistence(String skillName) throws SQLException {
        String checkCommand = "SELECT SkillName FROM Skills WHERE " +
                "SkillName = ?;";
        PreparedStatement statement = conn.prepareStatement(checkCommand);
        statement.setString(1,skillName);
        ResultSet rs =statement.executeQuery();
        while (rs.next()) return true;
        return false;
    }

    public synchronized boolean assignSkill(int charID, List<String> skillName) throws SQLException {
        String checkCommand = "DELETE FROM SkillAssign WHERE CharacterID = ?;";
        PreparedStatement statement = conn.prepareStatement(checkCommand);
        statement.setInt(1,charID);
        statement.executeQuery();
        for (int i = 0; i<skillName.size(); i++) {
            if(checkSkillExistence(skillName.get(i))) {
                checkCommand = "INSERT INTO SkillAssign(CharacterID,SkillName) VALUES (?,?);";
                statement = conn.prepareStatement(checkCommand);
                statement.setInt(1, charID);
                statement.setString(2, skillName.get(i));
                statement.executeQuery();
            }
            else return false;
        }
        return true;
    }
    //TODO
    public synchronized Map<String,Long> showAllStats (int charID) throws SQLException {
        String theCommand = "SELECT StatName,StatValue FROM CharData WHERE CharacterID = ?";
        PreparedStatement statement = conn.prepareStatement(theCommand);
        statement.setInt(1,charID);
        ResultSet rs =statement.executeQuery();
        Map<String,Long> statList = new HashMap<>();
        while (rs.next()) {
            String statName = rs.getString("StatName");
            Long skillID = rs.getLong("StatValue");
            statList.put(statName,skillID);
        }
        return statList;
    }



    // Starts the database
    private synchronized void loadInitialData() throws SQLException, IOException {
        try (Reader reader = new InputStreamReader(
                UserDatabase.class.getClassLoader().getResourceAsStream("serverDatabase.sql"), "UTF-8")) {
            RunScript.execute(conn, reader);
        }
    }

    // prints out certain requested data. Currently used to control if stuff we add is actually added.
    public synchronized void dumpTable() throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * from UserDatabase")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("LoginName") + "\t" + rs.getString("Password"));
            }
        }
    }
}
