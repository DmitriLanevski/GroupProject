package client.ui;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import server.Message;
import server.MessageTypes;
import serverDatabase.CharacterData;

import java.util.List;

/**
 * Created by Madis on 14.05.2016.
 */
public class CharacterSelectScreen extends UIManager {
    VBox characterSelect = new VBox();

    public CharacterSelectScreen(UIManager parentManager) {
        super(parentManager);

        addChild(characterSelect);
        characterSelect.setLayoutX(100);

        Button cancelButton = new Button("Cancel");
        addChild(cancelButton);
        cancelButton.setOnAction((e)->activateByName("MainMenu"));
    }

    @Override
    protected void onActivate() {
        characterSelect.getChildren().clear();
        getToServer().sendMessage(MessageTypes.REQUEST_CHARACTERS, "");
    }

    @Override
    public void handleMessage(Message message) {
        super.handleMessage(message);

        switch (message.getMessageType()) {
            case MessageTypes.REQUEST_CHARACTERS: {
                CharacterData[] characterDataList = message.readAs(CharacterData[].class);
                for (CharacterData characterData : characterDataList) {
                    addCharacter(characterData);
                }
            }
        }
    }

    private void addCharacter(CharacterData data) {
        HBox root = new HBox();
        root.getChildren().add(new Text(data.getCharName()));

        Button selectButton = new Button("Select");
        root.getChildren().add(selectButton);

        selectButton.setOnAction((e)->getToServer().sendMessage(MessageTypes.REQUEST_GAME_START, data.getCharName()));

        characterSelect.getChildren().add(root);
    }
}
