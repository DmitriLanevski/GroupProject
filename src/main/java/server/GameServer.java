package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GameServer implements Runnable {

    public static Gson gson = new Gson();
    private final int port;

    private final Object lock = new Object();

    private final List<ServerPlayerData> players = Collections.synchronizedList(new ArrayList<>());
    private final List<BlockingQueue<Message>> playerMessageQueues = Collections.synchronizedList(new ArrayList<>());

    private int numberOfPlayers = 0;

    public GameServer(int port) {
        this.port = port;
    }

    private void sendToAll(Message message) {
        synchronized (playerMessageQueues) { // ensures all players get all the messages in the same order
            for (BlockingQueue<Message> queue : playerMessageQueues) {
                queue.add(message);
            }
        }
    }
    private void sendToAll(int messageType, Object object) {
        sendToAll(new Message( messageType, object ));
    }

    @Override
    public void run() {
        System.out.println("Starting server.");

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();

                System.out.println("New connection");

                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                DataInputStream input = new DataInputStream(socket.getInputStream());

                ServerPlayerData newPlayer = gson.fromJson(input.readUTF(), ServerPlayerData.class);
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
                output.writeUTF(gson.toJson(newPlayer));
                

                BlockingQueue<Message> queue = new LinkedBlockingQueue<>();
                playerMessageQueues.add(queue);
                players.add(newPlayer);
                new Thread(new PlayerReciever(newPlayer, input, socket)).start();
                new Thread(new PlayerMessager(newPlayer, socket, output, queue)).start();


                System.out.println(newPlayer.name + " has joined as a " + (newPlayer.spectator ? "spectator." : "player."));
                sendToAll(MessageTypes.SERVER_MESSAGE, newPlayer.name + " has joined as a " + (newPlayer.spectator ? "spectator." : "player."));
            }

        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    class PlayerReciever implements Runnable {
        private final Socket playerSocket;
        private final DataInputStream input;
        private final ServerPlayerData data;

        public PlayerReciever(ServerPlayerData data, DataInputStream input, Socket playerSocket) {
            this.data = data;
            this.input = input;
            this.playerSocket = playerSocket;
        }

        @Override
        public void run() {
            try {

                while (!playerSocket.isClosed()) {
                    int messageType = input.readInt();

                    switch(messageType) {
                        case MessageTypes.TEXT: {
                            String s = gson.fromJson(input.readUTF(), String.class);
                            sendToAll( MessageTypes.TEXT, data.name + ": " + s );
                            System.out.println(s);
                            break;
                        }
                        default: {
                            throw new RuntimeException("Invalid message of type '" + messageType + "'");
                        }
                    }
                }
            }
            catch (EOFException | SocketException e) {
                // Connection lost, nothing to worry about.
                sendToAll(MessageTypes.SERVER_MESSAGE, data.name + " has disconnected.");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    class PlayerMessager implements Runnable {
        private final Socket playerSocket;
        private final ServerPlayerData data;
        private final DataOutputStream output;
        private final BlockingQueue<Message> queue;

        public PlayerMessager(ServerPlayerData data, Socket playerSocket, DataOutputStream output, BlockingQueue<Message> queue) {
            this.data = data;
            this.playerSocket = playerSocket;
            this.output = output;
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                while (!playerSocket.isClosed()) {
                    Message sendingMessage = queue.take();

                    output.writeInt(sendingMessage.getMessageType());
                    output.writeUTF(sendingMessage.getGsonObject());
                }

            }
            catch (SocketException e) {
                // Connection lost, nothing to worry about.
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
            finally {
                players.remove(data);
                playerMessageQueues.remove(queue);
            }
        }
    }
}