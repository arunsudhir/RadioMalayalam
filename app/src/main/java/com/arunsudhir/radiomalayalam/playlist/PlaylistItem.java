package com.arunsudhir.radiomalayalam.playlist;

import java.io.Serializable;

/**
 * Created by Arun on 10/19/2015.
 */
public class PlaylistItem implements Serializable {
    public String playlistName;
    public String url;

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public String getPlaylistUrl() {
        return url;
    }

    public void setPlaylistUrl(String playlistUrl) {
        this.url = playlistUrl;
    }
}
