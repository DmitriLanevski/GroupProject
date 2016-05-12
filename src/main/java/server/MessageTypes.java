package server;

/**
 * Created by Madis on 12.04.2016.
 */
public class MessageTypes {
    public static final int TEXT = 0;
    public static final int SERVER_MESSAGE = 1;
    public static final int LOGIN = 2;
    public static final int LOGIN_SUCCESS = 3;
    public static final int LOGIN_FAILURE = 4;
    public static final int TO_GAME_ENGINE_NEW_CHARACTER = 5;
    public static final int GAME_START = 6;

    public static final int CLOSE_THREAD = 10000;
}
