package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameServer implements Runnable {

    private final ServerSocket serverSocket;
    private final Object lock = new Object();
    private final List<Thread> threads = new ArrayList<>();
    private final List<ObjectOutputStream> connections = new ArrayList<>();
    private final List<ServerPlayerData> players = new ArrayList<>();
    private int numberOfPlayers = 0;

    public GameServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    private void sendToAll(String messageType, Object message) throws IOException {
        System.out.println("Sending message " + messageType + " " + message);
        for (ObjectOutputStream connection : connections) {
            connection.writeUTF(messageType);
            connection.writeObject(message);
        }
    }

    @Override
    public void run() {
        System.out.println("Starting server.");
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                synchronized (lock) {
                    System.out.println("New connection");
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.flush(); // DO NOT REMOVE
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                    ServerPlayerData newPlayer = (ServerPlayerData)ois.readObject();
                    if (!newPlayer.spectator) {
                        if (numberOfPlayers >= 2 )
                            newPlayer.spectator = true;
                        else
                            numberOfPlayers++;
                    }
                    for (ServerPlayerData player : players) {
                        if (player.name.equals(newPlayer.name))
                            newPlayer.name += "!";
                    }
                    oos.writeObject(newPlayer);

                    Thread thread = new Thread(new PlayerConnector(newPlayer, ois, socket));
                    connections.add(oos);
                    players.add(newPlayer);
                    thread.start();

                    System.out.println(newPlayer.name + " has joined as a " + (newPlayer.spectator ? "spectator." : "player."));
                    sendToAll("server_message", newPlayer.name + " has joined as a " + (newPlayer.spectator ? "spectator." : "player.") );
                }
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    class PlayerConnector implements Runnable {
        private final Socket playerSocket;
        private final ObjectInputStream input;
        private final ServerPlayerData data;

        public PlayerConnector(ServerPlayerData data, ObjectInputStream input, Socket playerSocket) {
            this.data = data;
            this.input = input;
            this.playerSocket = playerSocket;
        }

        @Override
        public void run() {
            try {

                while (!playerSocket.isClosed()) {
                    String messageType = input.readUTF();
                    System.out.println(messageType);
                    synchronized (lock) {
                        switch(messageType) {
                            case "text": {
                                sendToAll(messageType, data.name + ": " + input.readObject());
                                break;
                            }
                            default: {
                                throw new RuntimeException("Invalid message of type '" + messageType + "'");
                            }
                        }
                    }
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}