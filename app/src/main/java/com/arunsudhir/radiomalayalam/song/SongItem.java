package com.arunsudhir.radiomalayalam.song;

/**
 * Created by Arun on 9/1/2015.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.arunsudhir.radiomalayalam.communication.CommunicationConstants;

import java.io.Serializable;

/**
 * An item representing a song
 */
public class SongItem implements Serializable, Parcelable{
    public String id;
    public String songName;
    public String album;
    public String songPath;
    public String singer1;
    public String singer2;
    public String music;
    public String albumArtPath;

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

    public String getAlbumArtPath() {
        return CommunicationConstants.baseUrl + songPath.substring(0,songPath.lastIndexOf("/"))+"/AlbumArt.jpg";
    }

    public void setAlbumArtPath(String path) {
        this.songPath = path;
    }


    public SongItem(){}

    public SongItem(String name) {
        this.songName = name;
    }

    @Override
    public String toString() {
        return songName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(songName);
        dest.writeString(album);
        dest.writeString(songPath);
        dest.writeString(singer1);
        dest.writeString(singer2);
        dest.writeString(music);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<SongItem> CREATOR = new Parcelable.Creator<SongItem>() {
        public SongItem createFromParcel(Parcel in) {
            return new SongItem(in);
        }

        public SongItem[] newArray(int size) {
            return new SongItem[size];
        }
    };

    public SongItem(Parcel src)
    {
        id = src.readString();
        songName = src.readString();
        album = src.readString();
        songPath = src.readString();
        singer1 = src.readString();
        singer2 = src.readString();
        music = src.readString();
    }
}
