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
public class MenuManager {
    private final ClientServerConnection toServer;

    private MenuManager parentMenu = null;
    private MenuManager activeMenu = null;
    private Map<String, MenuManager> namedMenus = new HashMap<>();

    private Group childRoot = new Group();
    private Group root = new Group(childRoot);

    public MenuManager(ClientServerConnection toServer) {
        this.toServer = toServer;
    }

    public MenuManager(MenuManager parentMenu) {
        this.parentMenu = parentMenu;
        toServer = parentMenu.getToServer();
    }

    public void activate(MenuManager newMenu) {
        childRoot.getChildren().clear();
        childRoot.getChildren().add(newMenu.getRoot());
        activeMenu = newMenu;
        newMenu.onActivate();
    }
    public void activateByName(String menuName) {
        if (namedMenus.containsKey(menuName)) {
            activate(namedMenus.get(menuName));
        }
        else if (parentMenu != null)
            parentMenu.activateByName(menuName);
    }

    protected void onActivate() {

    }

    public void addNamedMenu(String name, MenuManager menu) {
        namedMenus.put(name, menu);
    }

    public void handleMessage(Message message) {
        if (activeMenu != null) activeMenu.handleMessage(message);
    }

    public MenuManager getActiveMenu() {
        return activeMenu;
    }

    public Group getChildRoot() {
        return childRoot;
    }

    public MenuManager getParentMenu() {
        return parentMenu;
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
