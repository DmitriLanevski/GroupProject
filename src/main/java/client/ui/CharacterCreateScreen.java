package client.ui;

import com.google.gson.Gson;
import gameLogic.attributes.CharacterGenerationStatData;
import javafx.application.Platform;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import server.Message;
import server.MessageTypes;
import serverDatabase.CharacterData;

import java.util.*;

/**
 * Created by Madis on 14.05.2016.
 */
public class CharacterCreateScreen extends UIManager {
    TextField nameField = new TextField();

    HashMap<String, Button> skillButtons = new HashMap<>();
    VBox skillList = new VBox();
    VBox chosenSkillList = new VBox();
    List<String> chosenSkills = new ArrayList<>();

    HashMap<String, StatButtons> statButtons = new HashMap<>();
    VBox statsList = new VBox();

    Text errorMessage = new Text();
    Text skillsLeft = new Text();
    Text skillPointsLeft = new Text();

    public CharacterCreateScreen(UIManager parentManager) {
        super(parentManager);

        addChild(nameField);
        addChild(skillList);
        addChild(chosenSkillList);
        addChild(statsList);

        skillList.setLayoutX(150);
        skillList.setLayoutY(30);
        chosenSkillList.setLayoutX(270);
        chosenSkillList.setLayoutY(30);
        statsList.setLayoutX(400);
        statsList.setLayoutY(30);

        Button cancelButton = new Button("Cancel");
        addChild(cancelButton);
        cancelButton.setLayoutY(100);
        cancelButton.setOnAction((e)->Platform.runLater(()->activateByName("MainMenu")));

        Button acceptButton = new Button("Create");
        addChild(acceptButton);
        acceptButton.setLayoutY(100);
        acceptButton.setLayoutX(80);

        addChild(errorMessage);
        errorMessage.setLayoutY(150);

        addChild(skillsLeft);
        skillsLeft.setLayoutX(150);
        skillsLeft.setLayoutY(15);

        addChild(skillPointsLeft);
        skillPointsLeft.setLayoutX(400);
        skillPointsLeft.setLayoutY(15);

        acceptButton.setOnAction((e)->Platform.runLater(this::createdCharacter));
    }

    private void createdCharacter() {
        Map<String, Long> stats = new HashMap<>();
        for (String statName : statButtons.keySet()) {
            stats.put(statName, (long)statButtons.get(statName).getSpentPoints());
        }
        CharacterData data = new CharacterData(-1, nameField.getText(), chosenSkills, stats,0);
        getToServer().sendMessage(MessageTypes.NEW_CHARACTER, data);
    }

    private void pressedSkill(String skillName) {
        Button button = skillButtons.get(skillName);

        if (skillList.getChildren().contains(button)) {
            skillList.getChildren().remove(button);
            chosenSkillList.getChildren().add(button);
            chosenSkills.add(skillName);
            getToServer().sendMessage(MessageTypes.REQUEST_SKILLS_ALTERABLE_STATS, chosenSkills);
        } else {
            chosenSkillList.getChildren().remove(button);
            chosenSkills.remove(skillName);
            skillList.getChildren().add(button);
            getToServer().sendMessage(MessageTypes.REQUEST_SKILLS_ALTERABLE_STATS, chosenSkills);
        }

        skillsLeft.setText( 5 - chosenSkills.size() + " skills left to choose." );
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
            case MessageTypes.REQUEST_SKILLS_ALTERABLE_STATS: {
                Map<String, com.google.gson.internal.LinkedTreeMap> alterableStatsJSON = message.readAs(Map.class);
                Gson gson = new Gson();
                Map<String, CharacterGenerationStatData> alterableStats = new HashMap<>();
                for (String statName : alterableStatsJSON.keySet()) {
                    alterableStats.put(statName, gson.fromJson(alterableStatsJSON.get(statName).toString(), CharacterGenerationStatData.class));
                }

                System.out.println(alterableStats);

                for (String statName : new ArrayList<>(statButtons.keySet())) { // new list to make removal possible
                    if (!alterableStats.containsKey(statName)) {
                        statsList.getChildren().remove(statButtons.get(statName));
                        statButtons.remove(statName);
                    }
                }

                for (String statName : alterableStats.keySet()) {
                    if (!statButtons.containsKey(statName)) {
                        StatButtons buttons = new StatButtons(statName, alterableStats.get(statName).defaultMax, alterableStats.get(statName).growthRate);
                        statButtons.put(statName, buttons);
                        statsList.getChildren().add(buttons);
                    }
                }
                updateSkillPointsLeftText();

                break;
            }
            case MessageTypes.CHARACTER_CREATE_SUCCESS: {
                activateByName("MainMenu");
            }
            case MessageTypes.CHARACTER_CREATE_FAILURE: {
                errorMessage.setText(message.readAs(String.class));
            }
        }
    }

    private void updateSkillPointsLeftText() {
        int sum = 0;
        for (StatButtons buttons : statButtons.values()) {
            sum += buttons.getSpentPoints();
        }
        skillPointsLeft.setText(20 - sum + " skill points left to allocate.");
    }

    @Override
    protected void onActivate() {
        skillButtons.clear();
        skillList.getChildren().clear();
        chosenSkillList.getChildren().clear();
        chosenSkills.clear();

        statButtons.clear();
        statsList.getChildren().clear();

        errorMessage.setText("");
        skillsLeft.setText("5 skills left to choose.");


        getToServer().sendMessage(MessageTypes.REQUEST_ALL_SKILLS, "");
        getToServer().sendMessage(MessageTypes.REQUEST_SKILLS_ALTERABLE_STATS, chosenSkills);
    }

    private class StatButtons extends HBox {
        final Text name;
        final TextField amount;

        final Button addButton = new Button("+");
        final TextField skillPoints = new TextField("0");
        final Button minusButton = new Button("-");

        double defaultMax;
        double growthRate;
        int skillPointsAllocated = 0;

        public StatButtons(String statName, double defaultMax, double growthRate) {
            name = new Text(statName);

            this.defaultMax = defaultMax;
            this.growthRate = growthRate;
            amount = new TextField(Double.toString(defaultMax));

            getChildren().add(name);
            getChildren().add(amount);
            getChildren().add(minusButton);
            getChildren().add(skillPoints);
            getChildren().add(addButton);

            updateText();

            amount.setEditable(false);
            amount.setPrefWidth(80);
            skillPoints.setPrefWidth(40);

            minusButton.setOnAction((e)->Platform.runLater(()->addPoints(-1)));
            addButton.setOnAction((e)->Platform.runLater(()->addPoints(+1)));
        }

        private void addPoints(int amount) {
            skillPointsAllocated += amount;
            updateText();
        }

        public int getSpentPoints() {
            return skillPointsAllocated;
        }

        private void updateText(){
            amount.setText(Double.toString(defaultMax+growthRate*skillPointsAllocated));
            skillPoints.setText(Integer.toString(skillPointsAllocated));
            updateSkillPointsLeftText();
        }
    }
}
