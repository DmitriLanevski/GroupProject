package serverDatabase;


import org.h2.tools.RunScript;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.*;

/**
 * Created by Stas on 17/04/2016.
 */
public class UserDatabase {
    public static void main(String[] args) throws Exception {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:./src/main/resources/Database")) {
            loadInitialData(connection);
            System.out.println("CheckUserExistence:"+CheckUserExistence(connection,"Alpha"));
            System.out.println("registerUser:"+registerUser(connection,"Alpha","12345"));
            System.out.println("Password Change:"+changePassword(connection,"Alpha","12345"));
            System.out.println("CheckUserExistence:"+CheckUserExistence(connection,"Alpha"));
            connection.close();
            Connection connection1 = DriverManager.getConnection("jdbc:h2:./src/main/resources/Database");
            System.out.println("CheckUserExistence:"+CheckUserExistence(connection1,"Alpha"));
            System.out.println("logIn:"+logIn(connection1,"Alpha","12345"));
            dumpTable(connection1);
        }
    }
    protected static boolean CheckUserExistence(Connection conn, String username) throws SQLException {
        String checkCommand = "SELECT LoginName FROM UserDatabase WHERE " +
                "LoginName = ?;";
        PreparedStatement statement = conn.prepareStatement(checkCommand);
        statement.setString(1,username);
        ResultSet rs =statement.executeQuery();
        return rs.next();

    }
    public static boolean registerUser(Connection conn, String username, String password)throws Exception{
        if (!CheckUserExistence(conn, username)) {
            String addUser = "INSERT INTO UserDatabase(LoginName,Password) VALUES (?,?);";
            PreparedStatement statement = conn.prepareStatement(addUser);
            statement.setString(1,username);
            statement.setString(2,Encryption.Encrypt(password));
            statement.execute();
            return true;
        }
        return false;
    }
    public static boolean changePassword(Connection conn, String username, String password)throws Exception{
        if (CheckUserExistence(conn, username)) {
            String addUser = "UPDATE UserDatabase SET password=? WHERE loginName=?;";
            PreparedStatement statement = conn.prepareStatement(addUser);
            statement.setString(1,Encryption.Encrypt(password));
            statement.setString(2,username);
            statement.execute();
            return true;
        }
        return false;
    }
    public static boolean logIn (Connection conn, String username, String password) throws Exception{
        if (CheckUserExistence(conn,username)) {


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





    private static void loadInitialData(Connection connection) throws SQLException, IOException {
        try (Reader reader = new InputStreamReader(
                UserDatabase.class.getClassLoader().getResourceAsStream("serverDatabase.sql"), "UTF-8")) {
            RunScript.execute(connection, reader);
        }
    }

    private static void dumpTable(Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * from UserDatabase")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("LoginName") + "\t" + rs.getString("Password"));
            }
        }
    }
}
