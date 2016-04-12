package client;

import com.google.gson.Gson;
import javafx.application.Platform;
import server.MessageTypes;
import server.ServerPlayerData;
import javafx.animation.AnimationTimer;
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

/**
 * Created by Madis on 27.03.2016.
 */
public class GUIDummyGameClient extends Application {

    public static Gson gson = new Gson();

    private Socket socket;
    private DataOutputStream output;
    private DataInputStream input;
    private ServerPlayerData data;

    void sendMessage(int messageType, Object message) throws IOException {
        output.writeInt(messageType);
        output.writeUTF(gson.toJson(message));
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        socket = new Socket(InetAddress.getLocalHost(), 1337);
        output = new DataOutputStream(socket.getOutputStream());
        input = new DataInputStream(socket.getInputStream());

        data = new ServerPlayerData();
        data.spectator = false;
        data.name = "Dude";

        output.writeUTF(gson.toJson(data));
        data = gson.fromJson(input.readUTF(), ServerPlayerData.class);


        Group root = new Group();
        Scene scene = new Scene(root, 235, 335, Color.SNOW);

        TextField inputTextField = new TextField();
        inputTextField.setLayoutX(50);
        inputTextField.setLayoutY(300);
        root.getChildren().add(inputTextField);

        inputTextField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    sendMessage(MessageTypes.TEXT, inputTextField.getText());
                    inputTextField.clear();
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });

        TextArea textDisplay = new TextArea();
        textDisplay.setEditable(false);
        textDisplay.setWrapText(true);
        textDisplay.setPrefWidth(235);
        root.getChildren().add(textDisplay);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!socket.isClosed()) {
                        int messageType = input.readInt();
                        switch(messageType) {
                            case MessageTypes.TEXT: {
                                String s = gson.fromJson(input.readUTF(), String.class);
                                Platform.runLater(()->textDisplay.appendText("\n"+s));
                                System.out.println(s);
                                break;
                            }
                            case MessageTypes.SERVER_MESSAGE: {
                                String s = gson.fromJson(input.readUTF(), String.class);
                                Platform.runLater(()->textDisplay.appendText("\n"+s));
                                System.out.println(s);
                                break;
                            }
                            default: {
                                throw new RuntimeException("Invalid message of type '" + messageType + "'");
                            }
                        }
                    }
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
