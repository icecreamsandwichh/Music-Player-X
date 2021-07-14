package com.dxn.musicx.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dxn.musicx.R;

public class SongListViewHolder extends RecyclerView.ViewHolder {
    public TextView titleTextView;
    public TextView detailsTextView;
    public TextView lengthTextView;
    public ImageView albumImageView;
    public SongListViewHolder(@NonNull View itemView) {
        super(itemView);
        albumImageView = itemView.findViewById(R.id.album_image);
        titleTextView = itemView.findViewById(R.id.song_title);
        detailsTextView = itemView.findViewById(R.id.song_details);
        lengthTextView = itemView.findViewById(R.id.song_length);
    }
}

