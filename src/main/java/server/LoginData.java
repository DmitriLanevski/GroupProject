package server;

public class LoginData {
    String userName;
    String password;

    public LoginData(String userName, String password) {
        this.password = password;
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "LoginData{" +
                "password='" + password + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
