package com.arunsudhir.radiomalayalam.io.archive;

import java.util.Date;

import lombok.Builder;
import lombok.Value;

/**
 * Class to represent a playlist item.
 */
@Value
@Builder
public class PlaylistItem {
    long id;
    long songId;
    long position;
    Date createDate;
}
