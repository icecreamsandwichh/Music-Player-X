package com.dxn.musicx.util;

public class Utilities {
    public static String longMillsToTime(long milliseconds) {
        long minutes = (milliseconds / 1000) / 60;
        long seconds = (milliseconds / 1000) % 60;
        if(seconds<10) {
            return minutes+":0"+seconds;
        }
        return minutes+":"+seconds;
    }
}
