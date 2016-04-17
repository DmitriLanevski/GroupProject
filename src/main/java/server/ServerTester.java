package server;

import java.io.IOException;
import java.sql.SQLException;

public class ServerTester {
    public static void main(String[] args) throws IOException, SQLException {
        Thread thread = new Thread(new GameServer(1337));
        thread.start();
    }
}
