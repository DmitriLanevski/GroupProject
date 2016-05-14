package client.ui;

import client.ClientServerConnection;
import server.Message;
import server.MessageTypes;

/**
 * Created by Madis on 11.05.2016.
 */
public class PrimaryUI extends UIManager {

    public PrimaryUI(ClientServerConnection toServer) throws Exception {
        super(toServer);

        addNamedManager("Login", new LoginScreen(this));
        addNamedManager("MainMenu", new MainMenu(this));
        // TODO: ServerBrowser
        addNamedManager("CharacterCreator", new CharacterCreateScreen(this));
        // TODO: CharacterSelect
        addNamedManager("BattleScreen", new BattleScreen(this));
    }

    @Override
    public void handleMessage(Message message) {
        super.handleMessage(message);
        switch (message.getMessageType()) {
            case MessageTypes.GAME_START:
                activateByName("BattleScreen");
        }
    }
}
