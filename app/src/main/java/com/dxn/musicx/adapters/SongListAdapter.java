package com.dxn.musicx.adapters;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dxn.musicx.R;
import com.dxn.musicx.models.Song;
import com.dxn.musicx.ui.activities.MainActivity;
import com.dxn.musicx.ui.activities.PlayerActivity;
import com.dxn.musicx.util.Constants;
import com.dxn.musicx.util.Utilities;
import com.dxn.musicx.viewholders.SongListViewHolder;

import java.util.ArrayList;

public class SongListAdapter extends RecyclerView.Adapter<SongListViewHolder> {

    private final ArrayList<Song> songs;
    private final Context context;

    public SongListAdapter(Context context, ArrayList<Song> songs) {
        this.songs = songs;
        this.context = context;
    }

    @NonNull
    @Override
    public SongListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list_layout, parent, false);
        return new SongListViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SongListViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.titleTextView.setText(song.getTitle());
        holder.detailsTextView.setText(song.getAlbum());
        holder.lengthTextView.setText(Utilities.longMillsToTime(song.getDuration()));
        Glide.with(context).load(song.getArtUri()).placeholder(R.drawable.ic_launcher_background).into(holder.albumImageView);

        holder.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(context, PlayerActivity.class);
                    intent.putExtra(Constants.POSITION, position);
                    intent.putExtra(Constants.SONGS, songs);
                    intent.putExtra(Constants.CALLED_FROM,Constants.SONG_LIST);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    ContextCompat.startActivity(context, intent, null);
                }
        );
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }
}
