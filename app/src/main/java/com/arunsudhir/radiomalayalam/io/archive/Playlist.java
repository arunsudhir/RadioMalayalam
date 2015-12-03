package com.arunsudhir.radiomalayalam.io.archive;

import com.google.common.base.Optional;

import lombok.Builder;
import lombok.Value;

/**
 * Class to represent a playlist
 */
@Value
@Builder
public class Playlist {
    long id;
    String name;
    long position;
    Optional<ArchiveFile> albumArt;
}
