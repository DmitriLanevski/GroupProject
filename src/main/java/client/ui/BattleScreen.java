package client.ui;

import javafx.scene.control.Button;
import server.Message;
import server.MessageTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by madis_000 on 12/05/2016.
 */
public class BattleScreen extends UIManager {
    List<Button> skillButtons = new ArrayList<>();

    public BattleScreen(UIManager parentManager) {
        super(parentManager);


    }

    @Override
    protected void onActivate() {
        getToServer().sendMessage(MessageTypes.REQUEST_FULL_GAME_STATE, "");
    }

    @Override
    public void handleMessage(Message message) {
        switch (message.getMessageType()) {
            case MessageTypes.SKILL_STATES:
                break;
            case MessageTypes.CHARACTER_STATUSES:
                break;
        }
    }
}
