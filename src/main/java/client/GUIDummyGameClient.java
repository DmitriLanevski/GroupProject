package client;

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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Madis on 27.03.2016.
 */
public class GUIDummyGameClient extends Application {

    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private ServerPlayerData data;

    void sendMessage(String messageType, Object message) throws IOException {
        oos.writeUTF(messageType);
        oos.writeObject(message);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        socket = new Socket(InetAddress.getLocalHost(), 1337);
        oos = new ObjectOutputStream(socket.getOutputStream());
        oos.flush(); // DO NOT REMOVE
        ois = new ObjectInputStream(socket.getInputStream());

        data = new ServerPlayerData();
        data.spectator = false;
        data.name = "Dude";
        oos.writeObject(data);
        data = (ServerPlayerData)ois.readObject();


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
                    sendMessage("text", inputTextField.getText());
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

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                try {
                    while (ois.available() > 0) {
                        String messageType = ois.readUTF();
                        switch(messageType) {
                            case "text": {
                                String s = (String)ois.readObject();
                                textDisplay.appendText("\n"+s);
                                System.out.println(s);
                                break;
                            }
                            case "server_message": {
                                String s = (String)ois.readObject();
                                textDisplay.appendText("\n"+s);
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
        }.start();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
