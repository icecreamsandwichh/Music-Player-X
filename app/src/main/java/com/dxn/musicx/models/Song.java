package com.dxn.musicx.models;

import java.io.Serializable;

public class Song implements Serializable {
    private final String id;
    private final String title;
    private final String album;
    private final String artist;
    private final String path;
    private final Long duration;
    private final String artUri;

    public Song(String id, String title, String album, String artist, String path, Long duration,String artUri) {
        this.id = id;
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.path = path;
        this.duration = duration;
        this.artUri = artUri;

    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public String getPath() {
        return path;
    }

    public Long getDuration() {
        return duration;
    }

    public String getArtUri() {
        return artUri;
    }
}
