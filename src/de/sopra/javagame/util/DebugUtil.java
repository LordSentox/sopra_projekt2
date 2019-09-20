package de.sopra.javagame.util;

/**
 * <h1>Projekt2</h1>
 *
 * @author Julius Korweck
 * @version 18.09.2019
 * @since 18.09.2019
 */
public class DebugUtil {

    public static boolean debugEnabled = false;
    public static boolean debugAI = false;

    public static void debug(String message) {
        if (debugEnabled)
            System.out.println("DEBUG: " + message);
    }

    public static void debugAI(String message) {
        if (debugAI)
            System.out.println("AI: " + message);
    }

}
/***********************************************************************************************
 *
 *                  All rights reserved, SpaceParrots UG (c) copyright 2019
 *
 ***********************************************************************************************/