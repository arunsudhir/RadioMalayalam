package com.arunsudhir.radiomalayalam.io.archive;

import com.arunsudhir.radiomalayalam.song.SongItem;

import java.util.Date;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 * Class that represents a cached song.
 */
@Value
@Builder
public class CachedSong {
    private long songId;
    @NonNull
    private String externalId;
    @NonNull
    private String name;
    private String album;
    @NonNull
    private String songPath;
    private String singer1;
    private String singer2;
    private String music;
    @NonNull
    private ArchiveFile file;

    public long getFileId() {
        return file.getId();
    }

    public byte[] getContent() {
        return file.getData();
    }

    public Date getCachedDate() {
        return file.getCreateDate();
    }

    public SongItem toSongItem() {
        SongItem item = new SongItem();
        item.setId(externalId);
        item.setAlbum(album);
        item.setMusic(music);
        item.setSinger1(singer1);
        item.setSinger2(singer2);
        item.setSongPath(songPath);
        return item;
    }
}
