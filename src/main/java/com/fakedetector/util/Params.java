package com.fakedetector.util;

public class Params {
    private Params() { }

    /** Turns text into an int, or returns the fallback if it isn't a number. */
    public static int toInt(String s, int fallback) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {      // null, empty, or not a number
            return fallback;
        }
    }
}