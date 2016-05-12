package server;

import gameLogic.BattleInstance;
import serverDatabase.CharacterData;

import java.util.Map;

public class UserGameData {
    private Map<String, CharacterData> characters = null;


    private BattleInstance activeBattle = null;
    private CharacterData chosenCharacter = null;
    private int playerID = -1;

    public UserGameData(Map<String, CharacterData> characters) {
        this.characters = characters;
    }

    public synchronized Map<String, CharacterData> getCharacters() {
        return characters;
    }

    public synchronized void setCharacters(Map<String, CharacterData> characters) {
        this.characters = characters;
    }

    public synchronized BattleInstance getActiveBattle() {
        return activeBattle;
    }

    public synchronized void setActiveBattle(BattleInstance activeBattle) {
        this.activeBattle = activeBattle;
    }

    public synchronized int getPlayerID() {
        return playerID;
    }

    public synchronized void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public CharacterData getChosenCharacter() {
        return chosenCharacter;
    }

    public void setChosenCharacter(CharacterData chosenCharacter) {
        this.chosenCharacter = chosenCharacter;
    }
}
