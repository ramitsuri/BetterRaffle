package com.betterraffle.main.helper;

import java.util.Random;

public class Utils {

    private static int LAST_GEN_TOKEN = -1;

    public static boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static boolean isEOF(String string) {
        return string.equals(Constants.END_OF_FILE);
    }

    public static int pickRandomInt(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }

    public static int generateToken() {
        int start = Constants.START_TOKEN;
        if (LAST_GEN_TOKEN != -1) {
            start = LAST_GEN_TOKEN;
        }
        LAST_GEN_TOKEN = start + 1;
        return LAST_GEN_TOKEN;
    }
}
