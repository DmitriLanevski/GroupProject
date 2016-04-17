package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Madis on 25.03.2016.
 */
public class ServerPlayerInfo implements Serializable {
    private final int connectionID;
    private final BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>();
    private final Socket connectionSocket;
    private final DataOutputStream output;
    private final DataInputStream input;

    private boolean spectator;
    private boolean loggedIn;
    private String userName;

    public ServerPlayerInfo(int connectionID, Socket connectionSocket) throws IOException {
        this.connectionID = connectionID;
        this.connectionSocket = connectionSocket;
        output = new DataOutputStream(connectionSocket.getOutputStream());
        input = new DataInputStream(connectionSocket.getInputStream());

        spectator = true;
        loggedIn = false;
        userName = "guest" + connectionID;
    }

    public synchronized boolean isLoggedIn() {
        return loggedIn;
    }

    public synchronized void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public synchronized boolean isSpectator() {
        return spectator;
    }

    public synchronized void setSpectator(boolean spectator) {
        this.spectator = spectator;
    }

    public synchronized String getUserName() {
        return userName;
    }

    public synchronized void setUserName(String userName) {
        this.userName = userName;
    }

    public synchronized int getConnectionID() {
        return connectionID;
    }

    public synchronized Socket getConnectionSocket() {
        return connectionSocket;
    }

    public synchronized DataInputStream getInput() {
        return input;
    }

    public synchronized BlockingQueue<Message> getMessageQueue() {
        return messageQueue;
    }

    public void sendMessage(Message message) {
        messageQueue.add(message);
    }
    public void sendMessage(int messageType, Object message) {
        sendMessage(new Message(messageType, message));
    }

    public synchronized DataOutputStream getOutput() {
        return output;
    }
}
