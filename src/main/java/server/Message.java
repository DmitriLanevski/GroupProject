package server;

import com.google.gson.Gson;

/**
 * Created by Madis on 12.04.2016.
 */
public class Message {

    private final int messageType;
    private final String gsonObject;

    private Message(String gsonObject, int messageType) {
        this.gsonObject = gsonObject;
        this.messageType = messageType;
    }

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

    public <T> T readAs(Class<T> type) {
        return new Gson().fromJson(gsonObject, type);
    }

    @Override
    public String toString() {
        return "Message{" +
                "gsonObject='" + gsonObject + '\'' +
                ", messageType=" + messageType +
                '}';
    }

    public static Message directlyComposeMessage(int messageType, String gsonObject) {
        return new Message(gsonObject, messageType);
    }
}
