package com.dxn.musicx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dxn.musicx.models.Song;
import com.dxn.musicx.ui.activities.PlayerActivity;

import java.io.IOException;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {

            case BaseApplication.PREV:
                playPrevMusic();
                break;

            case BaseApplication.NEXT:
                playNextMusic();
                break;

            case BaseApplication.PLAY_PAUSE:
                playPauseMusic();
                break;

            case BaseApplication.EXIT:
                PlayerActivity.musicService.stopForeground(true);
                PlayerActivity.musicService = null;
                System.exit(1);
                break;

        }
    }

    private void playPauseMusic() {
        if(PlayerActivity.isPlaying) {
            PlayerActivity.musicService.mediaPlayer.pause();
            PlayerActivity.binding.btnPlayPause.setImageResource(R.drawable.play_arrow_black_24dp);
            PlayerActivity.isPlaying = false;
        } else {
            PlayerActivity.musicService.mediaPlayer.start();
            PlayerActivity.binding.btnPlayPause.setImageResource(R.drawable.pause_black_24dp);
            PlayerActivity.isPlaying = true;
        }
        updateAppUi();
    }

    private void playNextMusic() {
        PlayerActivity.position++;
        if (PlayerActivity.position == PlayerActivity.songs.size()) {
            PlayerActivity.position = 0;
        }
        try {
            if(PlayerActivity.musicService.mediaPlayer==null) {
                PlayerActivity.musicService.mediaPlayer = new MediaPlayer();
            }
            PlayerActivity.musicService.mediaPlayer.reset();
            PlayerActivity.musicService.mediaPlayer.setDataSource(PlayerActivity.songs.get(PlayerActivity.position).getPath());
            PlayerActivity.musicService.mediaPlayer.prepare();
            PlayerActivity.musicService.mediaPlayer.start();
            updateAppUi();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playPrevMusic() {
        PlayerActivity.position--;
        if (PlayerActivity.position == -1) {
            PlayerActivity.position = PlayerActivity.songs.size() - 1;
        }
        try {
            if(PlayerActivity.musicService.mediaPlayer==null) {
                PlayerActivity.musicService.mediaPlayer = new MediaPlayer();
            }
            PlayerActivity.musicService.mediaPlayer.reset();
            PlayerActivity.musicService.mediaPlayer.setDataSource(PlayerActivity.songs.get(PlayerActivity.position).getPath());
            PlayerActivity.musicService.mediaPlayer.prepare();
            PlayerActivity.musicService.mediaPlayer.start();
            updateAppUi();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateAppUi() {
        Song song = PlayerActivity.songs.get(PlayerActivity.position);
        PlayerActivity.binding.songTitle.setText(song.getTitle());
        PlayerActivity.binding.songAlbum.setText(song.getAlbum());
        Glide.with((PlayerActivity.musicService.getBaseContext()))
                .load(song.getArtUri())
                .placeholder(R.drawable.ic_launcher_background)
                .into(PlayerActivity.binding.shapeableImageView);
        PlayerActivity.musicService.showNotification();
    }
}