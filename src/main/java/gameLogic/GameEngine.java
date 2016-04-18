package gameLogic;

import gameLogic.attributes.Status;
import gameLogic.attributes.StatusCollection;
import gameLogic.characters.Character;
import server.GameServer;
import server.Message;
import server.MessageTypes;
import server.ServerPlayerInfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

/**
 * Created by lanev_000 on 16.04.2016.
 */
public class GameEngine {
    public Character createNewCharacter(ServerPlayerInfo data){
        final Socket playerSocket = data.getConnectionSocket();
        final DataInputStream input = data.getInput();
        final DataOutputStream output = data.getOutput();
        boolean statsDistributed = false;
        //Distribute initial SkillPoints
        HashMap<String, Status> status = new StatusCollection().load();
        status.forEach((key,value)-> data.sendMessage(new Message(MessageTypes.TEXT, key + ": " + value)));
        while (!statsDistributed || (status.get("Skillpoints").getValue() > 0)){
            try {
                if (!playerSocket.isClosed()){
                    input.readInt();
                    String message = input.readUTF();
                    if (message.contains(":")){
                        String[] messageParts = message.split(":");
                        String key = messageParts[0];
                        System.out.println(message);
                        int amount = Integer.parseInt(messageParts[1]);
                        if (status.get("Skillpoints").getValue() >= amount){
                            status.get(key).changeValueBy(amount);
                            status.forEach((keys,values)-> data.sendMessage(new Message(MessageTypes.TEXT, keys + ": " + values)));
                        } else {
                            data.sendMessage(new Message(MessageTypes.TEXT, "Not enough Skillpoints for this operations."));
                            data.sendMessage(new Message(MessageTypes.TEXT, "Only " + status.get("Skillpoints").getValue() + "is available."));
                        }
                    }
                    else {
                        System.out.println(message);
                    }
                    if (message.equals("OK")){
                        statsDistributed = true;
                    }

                }
            } catch (EOFException | SocketException e) {
                // Connection lost, nothing to worry about.
                data.sendMessage(MessageTypes.SERVER_MESSAGE, data.getUserName() + " has disconnected.");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


        return new Character();
    }

    /*public Character loadCharacter(){
        HashMap<String, Status> status = new StatusCollection().load();
    }

    public void upgradeCharacter(){
        HashMap<String, Status> status = new StatusCollection().load();
    }*/
}
