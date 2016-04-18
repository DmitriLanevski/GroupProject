package client;

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

    public static Gson gson = new Gson();

    private Socket socket;
    private DataOutputStream output;
    private DataInputStream input;
    private ServerPlayerInfo data;

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
                    int messageType;
                    String messageText;
                    String message = inputTextField.getText();
                    System.out.println(message);
                    if (message.contains(",")){
                        String[] messageParts = message.split(",");
                        messageType = Integer.parseInt(messageParts[0]);
                        messageText = messageParts[1];
                    }
                    else {
                        messageType = MessageTypes.TEXT;
                        messageText = message;
                    }
                    sendMessage(messageType, messageText);
                    inputTextField.clear();
                }
                catch (SocketException e) {
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

        Thread thread = new Thread(new Runnable() {
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
                catch (SocketException e) {
                    Platform.runLater(()->textDisplay.appendText("\n"+"Connection lost."));
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        socket.close();
    }
}
