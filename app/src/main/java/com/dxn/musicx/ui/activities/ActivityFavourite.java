package com.dxn.musicx.ui.activities;

import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;
import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.dxn.musicx.databinding.ActivityFavouriteBinding;
import com.dxn.musicx.databinding.ActivityPlaylistBinding;

public class ActivityFavourite extends AppCompatActivity {

    private ActivityFavouriteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavouriteBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        binding.backBtn.setOnClickListener(v -> {
            finish();
        });
    }
}