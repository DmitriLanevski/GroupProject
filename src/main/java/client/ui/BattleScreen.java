package client.ui;

import com.google.gson.Gson;
import gameLogic.attributes.CharacterGenerationStatData;
import gameLogic.attributes.Stat;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import server.Message;
import server.MessageTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by madis_000 on 12/05/2016.
 */
public class BattleScreen extends UIManager {
    HBox skillButtonsRoot = new HBox();
    HashMap<String, Button> skillButtons = new HashMap<>();
    Map<String, StatDisplay> selfstats = new HashMap<>();
    VBox selfStatsRoot = new VBox();
    Map<String, StatDisplay> enemystats = new HashMap<>();
    VBox enemyStatsRoot = new VBox();


    public BattleScreen(UIManager parentManager) {
        super(parentManager);

        addChild(skillButtonsRoot);
        skillButtonsRoot.setLayoutY(500);


        addChild(selfStatsRoot);

        addChild(enemyStatsRoot);
        enemyStatsRoot.setLayoutX(760);
    }

    @Override
    protected void onActivate() {
        skillButtons.clear();
        selfstats.clear();
        selfStatsRoot.getChildren().clear();
        enemystats.clear();
        enemyStatsRoot.getChildren().clear();

        getToServer().sendMessage(MessageTypes.REQUEST_FULL_GAME_STATE, "");
    }

    @Override
    public void handleMessage(Message message) {
        switch (message.getMessageType()) {
            case MessageTypes.SKILL_STATES: {
                Map<String, Boolean> skillStates = message.readAs(Map.class);
                for (String skillName : skillStates.keySet()) {
                    Button skillButton = skillButtons.get(skillName);
                    if (skillStates.get(skillName))skillButton.setOpacity(1);
                    else skillButton.setOpacity(0.5);
                }
                break;
            }
            case MessageTypes.SELF_SKILLS: {
                Map<String, String> skills = message.readAs(Map.class);
                for (String skillName : skills.keySet()) {
                    Button skillButton = new Button(skillName);
                    skillButton.setTooltip(new Tooltip(skills.get(skillName)));
                    skillButton.setOnAction((e)->Platform.runLater(()->getToServer().sendMessage(MessageTypes.SKILL_USE, skillName)));

                    skillButtons.put(skillName, skillButton);
                    skillButtonsRoot.getChildren().add(skillButton);
                }
                break;
            }
            case MessageTypes.SELF_CHARACTER_STATUSES: {
                Map<String, com.google.gson.internal.LinkedTreeMap> statsJSON = message.readAs(Map.class);
                Gson gson = new Gson();
                Map<String, Stat> stats = new HashMap<>();
                for (String statName : statsJSON.keySet()) {
                    stats.put(statName, gson.fromJson(statsJSON.get(statName).toString(), Stat.class));
                }

                System.out.println(stats);
                for (String statName : stats.keySet()) {
                    if (selfstats.containsKey(statName)) selfstats.get(statName).update(stats.get(statName));
                    else {
                        StatDisplay disp = new StatDisplay(statName, stats.get(statName));
                        selfstats.put(statName, disp);
                        selfStatsRoot.getChildren().add(disp);
                    }
                }
                break;
            }
            case MessageTypes.OPPOSING_CHARACTER_STATUSES: {
                Map<String, com.google.gson.internal.LinkedTreeMap> statsJSON = message.readAs(Map.class);
                Gson gson = new Gson();
                Map<String, Stat> stats = new HashMap<>();
                for (String statName : statsJSON.keySet()) {
                    stats.put(statName, gson.fromJson(statsJSON.get(statName).toString(), Stat.class));
                }

                System.out.println(stats);
                for (String statName : stats.keySet()) {
                    if (enemystats.containsKey(statName)) enemystats.get(statName).update(stats.get(statName));
                    else {
                        StatDisplay disp = new StatDisplay(statName, stats.get(statName));
                        enemystats.put(statName, disp);
                        enemyStatsRoot.getChildren().add(disp);
                    }
                }
                break;
            }
        }
    }


    class StatDisplay extends HBox {
        ProgressBar disp = new ProgressBar(0);
        Text dispText = new Text();

        public StatDisplay(String name, Stat stat) {
            getChildren().add(new Text(name));
            getChildren().add(disp);
            update(stat);
        }

        void update(Stat stat) {
            disp.setProgress(stat.getMax()/stat.getValue());
            dispText.setText( Double.toString(stat.getValue())+"/"+Double.toString(stat.getMax()) );
        }
    }
}
