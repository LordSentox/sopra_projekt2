package de.sopra.javagame.view.abstraction;

/**
 * <h1>Projekt2</h1>
 *
 * @author Julius Korweck
 * @version 17.09.2019
 * @since 17.09.2019
 */
public final class Notifications {
    {
    }

    public static Notification info(String info) {
        return () -> info;
    }

    public static Notification error(String errorMessage) {
        return new Notification() {
            @Override
            public String message() {
                return errorMessage;
            }

            @Override
            public boolean isError() {
                return true;
            }
        };
    }

    public static Notification error(String message, Exception exception) {
        return error(message + " (" + exception.toString() + ")");
    }

    public static Notification gameWon(String message) {
        return new Notification() {
            @Override
            public String message() {
                return message;
            }

            @Override
            public boolean isGameWon() {
                return true;
            }
        };
    }

    public static Notification gameLost(String message) {
        return new Notification() {
            @Override
            public String message() {
                return message;
            }

            @Override
            public boolean isGameLost() {
                return true;
            }
        };
    }

}