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

    public static final int NEW_CHARACTER = 5;
    public static final int REQUEST_CHARACTERS = 6;

    public static final int REQUEST_GAME_START = 7;
    public static final int GAME_START = 8;
    public static final int REQUEST_FULL_GAME_STATE = 9;
    public static final int REQUEST_ALL_SKILLS = 10;

    public static final int CHARACTER_STATUSES = 11;
    public static final int SKILL_STATES = 12;
    public static final int SKILL_USE = 13;

    public static final int CLOSE_THREAD = 10000;
}
