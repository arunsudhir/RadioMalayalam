package com.arunsudhir.radiomalayalam.song;

/**
 * Created by Arun on 9/1/2015.
 */

import java.io.Serializable;

/**
 * An item representing a song
 */
public class SongItem implements Serializable{
    public String id;
    public String songName;
    public String album;
    public String songPath;
    public String singer1;
    public String singer2;
    public String music;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getSongPath() {
        return songPath;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

    public String getSinger1() {
        return singer1;
    }

    public void setSinger1(String singer1) {
        this.singer1 = singer1;
    }

    public String getSinger2() {
        return singer2;
    }

    public void setSinger2(String singer2) {
        this.singer2 = singer2;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }


    public SongItem(){}

    public SongItem(String name) {
        this.songName = name;
    }

    @Override
    public String toString() {
        return songName;
    }
}
