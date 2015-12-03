package com.arunsudhir.radiomalayalam.io.archive;

import java.util.Date;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 * Class that represents a file in the archive
 */
@Value
@Builder
public class ArchiveFile {
    long id;
    @NonNull
    String path;
    byte[] data;
    @NonNull
    Date createDate;
}
