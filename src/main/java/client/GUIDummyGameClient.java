package client;

import client.ui.UIManager;
import client.ui.PrimaryUI;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GUIDummyGameClient extends Application {
    ClientServerConnection conn = new ClientServerConnection();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Group root = new Group();
        Scene scene = new Scene(root, 900, 600, Color.SNOW);

        UIManager rootManager = new PrimaryUI(conn);
        root.getChildren().add(rootManager.getRoot());
        conn.setPrimaryHandler(rootManager);

        rootManager.activateByName("Login");

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        conn.close();
    }
}
