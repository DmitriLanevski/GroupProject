package client.ui;

import com.google.gson.Gson;
import gameLogic.attributes.Stat;
import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;
import server.Message;
import server.MessageTypes;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by madis_000 on 12/05/2016.
 */
public class BattleScreen extends UIManager {
    private static DecimalFormat df = new DecimalFormat("#.#");

    HBox skillButtonsRoot = new HBox();
    HashMap<String, Button> skillButtons = new HashMap<>();
    Map<String, StatDisplay> selfstats = new HashMap<>();
    VBox selfStatsRoot = new VBox();
    Map<String, StatDisplay> enemystats = new HashMap<>();
    VBox enemyStatsRoot = new VBox();

    Queue<String> battleLog = new LinkedBlockingQueue<>();
    AnimationTimer timer;

    public BattleScreen(UIManager parentManager) {
        super(parentManager);

        addChild(skillButtonsRoot);
        skillButtonsRoot.setLayoutY(500);


        addChild(selfStatsRoot);

        addChild(enemyStatsRoot);
        enemyStatsRoot.setLayoutX(660);

        timer = new AnimationTimer() {
            long sinceLastWrite = 0;
            long lastNow;

            @Override
            public void handle(long now) {

                sinceLastWrite += now-lastNow;
                lastNow = now;
                double seconds = sinceLastWrite /  1000000000.0;
                if (seconds > 0.05) {
                    String event = battleLog.poll();
                    if (event != null) {
                        PathTransition path = new PathTransition(new Duration(1500), new Line(500, 300, 500, 0));

                        Text eventText = new Text(event);
                        addChild(eventText);
                        path.setNode(eventText);
                        path.play();

                        path.setOnFinished((e)->getRoot().getChildren().remove(eventText));

                        sinceLastWrite = 0;
                    }
                }
            }
        };
        timer.start();
    }

    @Override
    protected void onActivate() {
        skillButtons.clear();
        selfstats.clear();
        selfStatsRoot.getChildren().clear();
        enemystats.clear();
        enemyStatsRoot.getChildren().clear();
        skillButtons.clear();
        skillButtonsRoot.getChildren().clear();

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
            case MessageTypes.GAME_OVER: {
                getParentManager().activate(new GameOverScreen(getParentManager(), message.readAs(String.class)));
                break;
            }
            case MessageTypes.BATTLE_EVENT_LOG: {
                battleLog.addAll(message.readAs(List.class));
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
            getChildren().add(dispText);
            update(stat);
        }

        void update(Stat stat) {
            disp.setProgress(stat.getValue()/stat.getMax());
            dispText.setText( df.format(stat.getValue())+"/"+df.format(stat.getMax()) );
        }
    }
}
