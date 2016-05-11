package client;

import client.ui.LoginMenu;
import client.ui.MenuManager;
import client.ui.PrimaryUI;
import com.google.gson.Gson;
import javafx.application.Platform;
import server.MessageTypes;
import server.ServerPlayerInfo;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

public class GUIDummyGameClient extends Application {
    ClientServerConnection conn = new ClientServerConnection();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Group root = new Group();
        Scene scene = new Scene(root, 235, 335, Color.SNOW);

        MenuManager rootMenu = new PrimaryUI(conn);
        root.getChildren().add(rootMenu.getRoot());
        conn.setPrimaryHandler(rootMenu);

        rootMenu.activateByName("Login");

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        conn.close();
    }
}
