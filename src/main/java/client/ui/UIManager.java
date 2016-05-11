package client.ui;

import client.ClientServerConnection;
import javafx.scene.Group;
import javafx.scene.Node;
import server.Message;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Madis on 10.05.2016.
 */
public class UIManager {
    private final ClientServerConnection toServer;

    private UIManager parentManager = null;
    private UIManager activeManager = null;
    private Map<String, UIManager> namedMenus = new HashMap<>();

    private Group childRoot = new Group();
    private Group root = new Group(childRoot);

    public UIManager(ClientServerConnection toServer) {
        this.toServer = toServer;
    }

    public UIManager(UIManager parentManager) {
        this.parentManager = parentManager;
        toServer = parentManager.getToServer();
    }

    public void activate(UIManager newManager) {
        childRoot.getChildren().clear();
        childRoot.getChildren().add(newManager.getRoot());
        activeManager = newManager;
        newManager.onActivate();
    }
    public void activateByName(String managerName) {
        if (namedMenus.containsKey(managerName)) {
            activate(namedMenus.get(managerName));
        }
        else if (parentManager != null)
            parentManager.activateByName(managerName);
    }

    protected void onActivate() {

    }

    public void addNamedManager(String name, UIManager manager) {
        namedMenus.put(name, manager);
    }

    public void handleMessage(Message message) {
        if (activeManager != null) activeManager.handleMessage(message);
    }

    public UIManager getActiveManager() {
        return activeManager;
    }

    public Group getChildRoot() {
        return childRoot;
    }

    public UIManager getParentManager() {
        return parentManager;
    }

    public Group getRoot() {
        return root;
    }

    public void addChild(Node node) {
        getRoot().getChildren().add(node);
    }

    public ClientServerConnection getToServer() {
        return toServer;
    }
}
