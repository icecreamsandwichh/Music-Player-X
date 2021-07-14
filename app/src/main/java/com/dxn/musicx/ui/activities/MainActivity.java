package com.dxn.musicx.ui.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;

import com.dxn.musicx.adapters.SongListAdapter;
import com.dxn.musicx.databinding.ActivityMainBinding;
import com.dxn.musicx.models.Song;
import com.dxn.musicx.util.Constants;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SongListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<Song> mSongs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setSupportActionBar(binding.include.topAppBar);
        mSongs = new ArrayList<>();
        getRuntimePermissions();
        attachBindings();
        setupRecyclerView();

    }

    private void setupRecyclerView() {
        mAdapter = new SongListAdapter(getApplicationContext(), mSongs);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        binding.recyclerView.setAdapter(mAdapter);
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.totalSongs.setText("Total Songs: " + mSongs.size());
    }

    @SuppressLint("RtlHardcoded")
    private void attachBindings() {
        binding.include.topAppBar.setNavigationOnClickListener(v -> {
            binding.drawerLayout.openDrawer(Gravity.LEFT);
        });

        binding.playlistBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, PlaylistActivity.class));
        });

        binding.favouriteBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, ActivityFavourite.class));
        });

        binding.shuffleBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, PlayerActivity.class);
            intent.putExtra(Constants.SONGS, mSongs);
            intent.putExtra(Constants.CALLED_FROM,Constants.SHUFFLE);
            startActivity(intent);
        });
    }

    private void getRuntimePermissions() {
        Dexter.withContext(MainActivity.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        mSongs = fetchSongs(Environment.getExternalStorageDirectory());
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }

    private ArrayList<Song> fetchSongs(File file) {
        ArrayList<Song> songs = new ArrayList<>();
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATE_ADDED,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID

        };
        Cursor cursor = this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, MediaStore.Audio.Media.DATE_ADDED);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String idC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                    String albumC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    String artistC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    Long durationC = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                    String pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    String albumIdC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                    Uri uri = Uri.parse("content://media/external/audio/albumart");
                    String artUri = Uri.withAppendedPath(uri, albumIdC).toString();
                    Song song = new Song(idC, titleC, albumC, artistC, pathC, durationC, artUri);
                    File file1 = new File(song.getPath());
                    if (file1.exists()) {
                        songs.add(song);
                    }
                } while (cursor.moveToNext());
                cursor.close();
            }
        }
        return songs;
    }
}