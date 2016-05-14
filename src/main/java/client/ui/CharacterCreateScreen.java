package client.ui;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import server.Message;
import server.MessageTypes;

import java.util.HashMap;

/**
 * Created by Madis on 14.05.2016.
 */
public class CharacterCreateScreen extends UIManager {
    HashMap<String, Button> skillButtons = new HashMap<>();
    VBox skillList = new VBox();
    VBox chosenSkillList = new VBox();

    public CharacterCreateScreen(UIManager parentManager) {
        super(parentManager);

        addChild(skillList);
        addChild(chosenSkillList);

        chosenSkillList.setLayoutX(200);
    }

    private void pressedSkill(String skillName) {
        Button button = skillButtons.get(skillName);

        if (skillList.getChildren().contains(button)) {
            skillList.getChildren().remove(button);
            chosenSkillList.getChildren().add(button);
        } else {
            chosenSkillList.getChildren().remove(button);
            skillList.getChildren().add(button);
        }
    }

    @Override
    public void handleMessage(Message message) {
        super.handleMessage(message);

        switch (message.getMessageType()) {
            case MessageTypes.REQUEST_ALL_SKILLS: {
                HashMap<String, String> skills = message.readAs(HashMap.class);

                System.out.println(skills);

                for (String skillName : skills.keySet()) {
                    Button button = new Button(skillName);
                    skillButtons.put(skillName, button);
                    skillList.getChildren().add(button);

                    button.setTooltip(new Tooltip(skills.get(skillName)));
                    button.setOnAction((e)-> Platform.runLater(()->pressedSkill(skillName)));
                }
                break;
            }
        }
    }

    @Override
    protected void onActivate() {
        skillList.getChildren().clear();
        getToServer().sendMessage(MessageTypes.REQUEST_ALL_SKILLS, "");
    }
}
