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
    public static final int CHARACTER_CREATE_SUCCESS = 6;
    public static final int CHARACTER_CREATE_FAILURE = 7;
    public static final int REQUEST_CHARACTERS = 8;

    public static final int REQUEST_GAME_START = 9;
    public static final int GAME_START = 10;
    public static final int REQUEST_FULL_GAME_STATE = 11;
    public static final int REQUEST_ALL_SKILLS = 12;
    public static final int REQUEST_SKILLS_ALTERABLE_STATS = 13;

    public static final int SELF_CHARACTER_STATUSES = 14;
    public static final int OPPOSING_CHARACTER_STATUSES = 15;
    public static final int SELF_SKILLS = 16;
    public static final int SKILL_STATES = 17;
    public static final int SKILL_USE = 18;

    public static final int GAME_OVER = 19;

    public static final int CLOSE_THREAD = 10000;
}
