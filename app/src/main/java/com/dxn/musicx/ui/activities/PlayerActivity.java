package com.dxn.musicx.ui.activities;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.bumptech.glide.Glide;
import com.dxn.musicx.MusicService;
import com.dxn.musicx.R;
import com.dxn.musicx.databinding.ActivityPlayerBinding;
import com.dxn.musicx.models.Song;
import com.dxn.musicx.util.Constants;
import com.dxn.musicx.util.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class PlayerActivity extends AppCompatActivity implements ServiceConnection, MediaPlayer.OnCompletionListener {

    @SuppressLint("StaticFieldLeak")
    public static ActivityPlayerBinding binding;
    private static String CALLED_FROM;
    public static ArrayList<Song> songs;
    public static int position = 0;
    public static Song song;
    public static boolean isPlaying = false;
    public static MusicService musicService;
    public static Thread seekBarThread;
    public static boolean repeat = false;


    @SuppressLint("UseCompatTextViewDrawableApis")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // create and start service
        Intent serviceIntent = new Intent(this, MusicService.class);
        bindService(serviceIntent, this, BIND_AUTO_CREATE);
        startService(serviceIntent);

        songs = new ArrayList<>();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        CALLED_FROM = intent.getStringExtra(Constants.CALLED_FROM);
        songs = (ArrayList<Song>) bundle.get(Constants.SONGS);

        if (Constants.SONG_LIST.equals(CALLED_FROM)) {
            position = bundle.getInt(Constants.POSITION);
        } else {
            Collections.shuffle(songs);
        }

        binding.backBtn.setOnClickListener(v -> finish());

        binding.btnPlayPause.setOnClickListener(v -> playPauseSong());

        binding.btnNext.setOnClickListener(v -> playNextSong());

        binding.btnPrev.setOnClickListener(v -> playPrevSong());

        binding.repeatBtn.setOnClickListener(v -> {
            if (!repeat) {
                repeat = true;
                binding.repeatBtn.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.cool_blue)));
            } else {

                binding.repeatBtn.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.cool_pink)));
                repeat = false;
            }
        });

    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MusicService.MyBinder binder = (MusicService.MyBinder) service;
        musicService = binder.currentService();
        try {
            playSong();
            updateUi();
        } catch (IOException e) {
            e.printStackTrace();
        }
        musicService.showNotification();
        binding.seekBar.setMax(musicService.mediaPlayer.getDuration());
        musicService.mediaPlayer.setOnCompletionListener(PlayerActivity.this);
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    musicService.mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                binding.progressText.setText(Utilities.longMillsToTime(musicService.mediaPlayer.getCurrentPosition()));
            }
        });
        updateSeekBar();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        musicService = null;
    }

    private void playSong() throws IOException {
        song = songs.get(position);
        if (musicService.mediaPlayer == null) {
            musicService.mediaPlayer = new MediaPlayer();
        }
        musicService.mediaPlayer.reset();
        musicService.mediaPlayer.setDataSource(song.getPath());
        musicService.mediaPlayer.prepare();
        musicService.mediaPlayer.start();
        isPlaying = true;
        updateUi();
    }

    public void playNextSong() {
        position++;
        if (position == songs.size()) {
            position = 0;
        }
        try {
            playSong();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playPrevSong() {
        position--;
        if (position == -1) {
            position = songs.size() - 1;
        }
        try {
            playSong();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playPauseSong() {
        if (musicService.mediaPlayer != null) {
            if (isPlaying) {
                musicService.mediaPlayer.pause();
                binding.btnPlayPause.setImageResource(R.drawable.play_arrow_black_24dp);
                isPlaying = false;
            } else {
                musicService.mediaPlayer.start();
                binding.btnPlayPause.setImageResource(R.drawable.pause_black_24dp);
                isPlaying = true;
            }
        }
        updateUi();
    }

    private void updateUi() {
        binding.songTitle.setText(song.getTitle());
        binding.songAlbum.setText(song.getAlbum());
        binding.musicLengthText.setText(Utilities.longMillsToTime(song.getDuration()));
        Glide.with(getApplicationContext())
                .load(song.getArtUri())
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.shapeableImageView);
        musicService.showNotification();
        binding.seekBar.setProgress(musicService.mediaPlayer.getCurrentPosition());
        binding.progressText.setText(Utilities.longMillsToTime(musicService.mediaPlayer.getCurrentPosition()));
    }

    public void updateSeekBar() {
        seekBarThread = new Thread(() -> {
            int currentProgress = 0;
            while (currentProgress < musicService.mediaPlayer.getDuration()) {
                try {
                    currentProgress = musicService.mediaPlayer.getCurrentPosition();
                    binding.seekBar.setProgress(currentProgress);
                    binding.progressText.setText(Utilities.longMillsToTime(musicService.mediaPlayer.getCurrentPosition()));
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        seekBarThread.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(repeat) {
            try {
                playSong();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            playNextSong();
        }
    }
}