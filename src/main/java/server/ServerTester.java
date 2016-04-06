package server;

import java.io.IOException;

/**
 * Created by Madis on 25.03.2016.
 */
public class ServerTester {
    public static void main(String[] args) throws IOException {
        Thread thread = new Thread(new GameServer(1337));
        thread.start();
    }
}
