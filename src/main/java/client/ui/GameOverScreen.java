package client.ui;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

/**
 * Created by Madis on 15.05.2016.
 */
public class GameOverScreen extends UIManager {
    public GameOverScreen(UIManager parentManager, String winnerName) {
        super(parentManager);

        Text gameOverText = new Text(winnerName+" won!");
        addChild(gameOverText);
        gameOverText.setLayoutY(50);

        Button continueButton = new Button("Continue");
        addChild(continueButton);
        continueButton.setLayoutY(100);
        continueButton.setOnAction((e)-> Platform.runLater(()->activateByName("MainMenu")));
    }


}
