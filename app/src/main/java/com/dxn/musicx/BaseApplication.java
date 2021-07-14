package com.dxn.musicx;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class BaseApplication extends Application {

    static final String CHANNEL_ID = "music";
    static final String PLAY_PAUSE = "play_pause";
    static final String PREV = "previous";
    static final String NEXT = "next";
    static final String EXIT = "exit";

    @Override
    public void onCreate() {
        super.onCreate();

        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,"Playing", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Now playing songs notification channel");
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }


}
