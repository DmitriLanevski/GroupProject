package server;

import com.google.gson.Gson;

/**
 * Created by Madis on 12.04.2016.
 */
public class Message {

    private final int messageType;
    private final String gsonObject;

    public Message(int messageType, Object object) {
        this.messageType = messageType;
        gsonObject = new Gson().toJson(object);
    }

    public String getGsonObject() {
        return gsonObject;
    }

    public int getMessageType() {
        return messageType;
    }
}
