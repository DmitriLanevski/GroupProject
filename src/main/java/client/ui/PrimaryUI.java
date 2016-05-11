package client.ui;

import client.ClientServerConnection;

/**
 * Created by Madis on 11.05.2016.
 */
public class PrimaryUI extends MenuManager {

    public PrimaryUI(ClientServerConnection toServer) throws Exception {
        super(toServer);

        addNamedMenu("Login", new LoginMenu(this));
        // TODO: FrontPage
        // TODO: ServerBrowser
        // TODO: CharacterCreator
        // TODO: CharacterSelect
        // TODO: BattleMenu
    }
}
