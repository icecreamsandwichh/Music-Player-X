package com.dxn.musicx;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;

import com.dxn.musicx.ui.activities.PlayerActivity;

public class MusicService extends Service {
    public MusicService() {
    }

    private final MyBinder binder = new MyBinder();
    public MediaPlayer mediaPlayer;
    private MediaSessionCompat session;

    @Override
    public IBinder onBind(Intent intent) {
        session = new MediaSessionCompat(getBaseContext(),"My Music");
        return binder;
    }

    public class MyBinder extends Binder {
        public  MusicService currentService() {
            return MusicService.this;
        }
    }

    public void showNotification() {

        Intent nextIntent = new Intent(getBaseContext(),NotificationReceiver.class).setAction(BaseApplication.NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(getBaseContext(),0,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent prevIntent = new Intent(getBaseContext(),NotificationReceiver.class).setAction(BaseApplication.PREV);
        PendingIntent prevPendingIntent = PendingIntent.getBroadcast(getBaseContext(),0,prevIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent playPauseIntent = new Intent(getBaseContext(),NotificationReceiver.class).setAction(BaseApplication.PLAY_PAUSE);
        PendingIntent playPausePendingIntent = PendingIntent.getBroadcast(getBaseContext(),0,playPauseIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent exitIntent = new Intent(getBaseContext(),NotificationReceiver.class).setAction(BaseApplication.EXIT);
        PendingIntent exitPendingIntent = PendingIntent.getBroadcast(getBaseContext(),0,exitIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat
                .Builder(getBaseContext(),BaseApplication.CHANNEL_ID)
                .setContentTitle(PlayerActivity.songs.get(PlayerActivity.position).getTitle())
                .setContentText(PlayerActivity.songs.get(PlayerActivity.position).getArtist())
                .setSmallIcon(R.drawable.library_music_black_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.headset_black_24dp))
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(session.getSessionToken()))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOnlyAlertOnce(true)
                .addAction(R.drawable.skip_previous_black_24dp,"Previous",prevPendingIntent)
                .addAction(getPlayPauseIcon(),"Play Pause",playPausePendingIntent)
                .addAction(R.drawable.skip_next_black_24dp,"Next",nextPendingIntent)
                .addAction(R.drawable.close_black_24dp,"Next",exitPendingIntent)
                .build();

        startForeground(15,notification);
    }

    private int getPlayPauseIcon() {
        if(PlayerActivity.isPlaying) {
            return R.drawable.pause_black_24dp;
        } else {
            return R.drawable.play_arrow_black_24dp;
        }
    }
}