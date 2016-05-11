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
        return null;
    }

    public Character loadCharacter(){
        HashMap<String, Status> status = new StatusCollection().load();
        return  null;
    }

    public void upgradeCharacter(){
        HashMap<String, Status> status = new StatusCollection().load();
    }
}
