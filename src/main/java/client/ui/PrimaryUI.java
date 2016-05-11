package client.ui;

import client.ClientServerConnection;

/**
 * Created by Madis on 11.05.2016.
 */
public class PrimaryUI extends UIManager {

    public PrimaryUI(ClientServerConnection toServer) throws Exception {
        super(toServer);

        addNamedManager("Login", new LoginScreen(this));
        // TODO: FrontPage
        // TODO: ServerBrowser
        // TODO: CharacterCreator
        // TODO: CharacterSelect
        // TODO: BattleMenu
    }
}
