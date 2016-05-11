package client;

import client.ui.MenuManager;
import server.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Madis on 10.05.2016.
 */
public class ClientServerConnection {
    Socket socket = null;
    DataInputStream in = null;
    DataOutputStream out = null;
    MenuManager primaryHandler = null;
    BlockingQueue<Message> messages = new LinkedBlockingQueue<>();

    public void sendMessage(int messageType, Object message) {
        System.out.println(new Message(messageType, message));
        messages.add(new Message(messageType, message));
    }

    public boolean connectToServer(String host, String port)  {
        if (socket != null) return true;
        try {
            socket = new Socket(InetAddress.getByName(host), Integer.parseInt(port));
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(()->{
                try {
                    while (!socket.isClosed()) {
                        Message message = messages.take();
                        out.writeInt(message.getMessageType());
                        out.writeUTF(message.getGsonObject());
                    }
                }
                catch (Exception e) {
                    System.out.println("Well, shit");
                }
            }).start();

            new Thread(()->{
                try {
                    while (!socket.isClosed()) {
                        int messageType = in.readInt();
                        String gsonMessage = in.readUTF();
                        handleMessage(Message.directlyComposeMessage(messageType, gsonMessage));
                    }
                }
                catch (Exception e) {
                    System.out.println("Well, shit");
                }
            }).start();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void setPrimaryHandler(MenuManager primaryHandler) {
        this.primaryHandler = primaryHandler;
    }

    public void handleMessage(Message message) {
        if (primaryHandler != null) {
            primaryHandler.handleMessage(message);
        }
    }

    public void close() throws IOException {
        if (socket != null) socket.close();
    }
}
