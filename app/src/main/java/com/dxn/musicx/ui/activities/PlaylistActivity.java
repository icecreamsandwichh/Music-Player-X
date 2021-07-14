package com.dxn.musicx.ui.activities;

import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.dxn.musicx.databinding.ActivityPlayerBinding;
import com.dxn.musicx.databinding.ActivityPlaylistBinding;

public class PlaylistActivity extends AppCompatActivity {

    private ActivityPlaylistBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlaylistBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.backBtn.setOnClickListener( v -> {
            finish();
        });
    }
}