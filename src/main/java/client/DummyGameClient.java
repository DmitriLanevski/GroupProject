package client;

import server.ServerPlayerData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Madis on 25.03.2016.
 */

public class DummyGameClient {
    static void sendMessage(ObjectOutputStream oos, String messageType, Object message) throws IOException {
        oos.writeUTF(messageType);
        oos.writeObject(message);
    }

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket(InetAddress.getLocalHost(), 1337);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.flush(); // DO NOT REMOVE
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

        ServerPlayerData data = new ServerPlayerData();
        data.spectator = false;
        data.name = "Dude";
        oos.writeObject(data);
        data = (ServerPlayerData)ois.readObject();

        Scanner s = new Scanner(System.in);
        while (true) {
            if (System.in.available() > 0) {
                sendMessage(oos, "text", s.nextLine());
            }
            if (ois.available() > 0) {
                String messageType = ois.readUTF();
                switch(messageType) {
                    case "text": {
                        System.out.println(ois.readObject());
                        break;
                    }
                    case "server_message": {
                        System.out.println(ois.readObject());
                        break;
                    }
                    default: {
                        throw new RuntimeException("Invalid message of type '" + messageType + "'");
                    }
                }
            }
            Thread.sleep(10);
        }
    }
}
