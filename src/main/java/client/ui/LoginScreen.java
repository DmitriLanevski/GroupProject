package client.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import server.LoginData;
import server.Message;
import server.MessageTypes;
import serverDatabase.Bcrypt;

import java.net.InetAddress;

/**
 * Created by Madis on 10.05.2016.
 */
public class LoginScreen extends UIManager {
    TextField usernameField = new TextField();
    TextField passwordField = new TextField();
    TextField IPField = new TextField();
    TextField portField = new TextField();
    Button loginButton = new Button();
    Text errorText = new Text();

    public LoginScreen(UIManager parentMenu) throws Exception {
        super(parentMenu);

        addChild(usernameField);
        usernameField.setLayoutX(0);
        usernameField.setLayoutY(0);
        usernameField.setPromptText("Username");

        addChild(passwordField);
        passwordField.setLayoutX(0);
        passwordField.setLayoutY(30);
        passwordField.setPromptText("Password");

        addChild(IPField);
        IPField.setLayoutX(0);
        IPField.setLayoutY(60);
        IPField.setPrefWidth(100);
        IPField.setText(InetAddress.getLocalHost().getHostAddress());

        addChild(portField);
        portField.setLayoutX(100);
        portField.setLayoutY(60);
        portField.setPrefWidth(50);
        portField.setText("1337");

        addChild(errorText);
        errorText.setLayoutX(60);
        errorText.setLayoutY(105);

        addChild(loginButton);
        loginButton.setText("Login");
        loginButton.setLayoutX(0);
        loginButton.setLayoutY(90);

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (usernameField.getText().isEmpty())
                    Platform.runLater(()->errorText.setText("Enter an username"));
                else if (passwordField.getText().isEmpty())
                    Platform.runLater(()->errorText.setText("Enter password"));
                else if (IPField.getText().isEmpty())
                    Platform.runLater(()->errorText.setText("Enter valid IP"));
                else if (IPField.getText().isEmpty())
                    Platform.runLater(()->errorText.setText("Enter valid port"));
                else {
                    if (getToServer().connectToServer(IPField.getText(), portField.getText()) ) {
                        IPField.setEditable(false);
                        portField.setEditable(false);
                        getToServer().sendMessage(MessageTypes.LOGIN, new LoginData(usernameField.getText(), Bcrypt.hashpw(passwordField.getText(),Bcrypt.gensalt())));
                        Platform.runLater(()->errorText.setText("Please Wait"));
                    }
                    else
                        Platform.runLater(()->errorText.setText("Server not found"));
                }


                // FAKE RESPONSE
                //handleMessage(new Message(MessageTypes.LOGIN_SUCCESS, "Wrong Password"));
            }
        });
    }

    @Override
    public void handleMessage(Message message) {
        switch (message.getMessageType()) {
            case MessageTypes.LOGIN_SUCCESS:
                getParentManager().activateByName("MainMenu");
                break;
            case MessageTypes.LOGIN_FAILURE:
                errorText.setText(message.readAs(String.class));
                break;
        }
    }

    @Override
    protected void onActivate() {
        usernameField.clear();
        passwordField.clear();
        errorText.setText("");
        getToServer().close();
    }
}
