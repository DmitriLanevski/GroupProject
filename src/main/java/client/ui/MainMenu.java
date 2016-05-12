package client.ui;

import javafx.application.Platform;
import javafx.scene.control.Button;

/**
 * Created by madis_000 on 12/05/2016.
 */
public class MainMenu extends UIManager {
    public MainMenu(UIManager parentManager) {
        super(parentManager);

        Button joinGameButton = new Button("Join Game");
        addChild(joinGameButton);
        joinGameButton.setOnAction((event)->Platform.runLater(()-> getParentManager().activateByName("CharacterSelect")));

        Button characterCreateButton = new Button("Create New Character");
        addChild(characterCreateButton);
        characterCreateButton.setOnAction((event)->Platform.runLater(()-> getParentManager().activateByName("CharacterCreator")));
        characterCreateButton.setLayoutY(30);

        Button logOutButton = new Button("Logout");
        addChild(logOutButton);
        logOutButton.setOnAction((event)->Platform.runLater(()-> getParentManager().activateByName("Login")));
        logOutButton.setLayoutY(60);
    }
}
