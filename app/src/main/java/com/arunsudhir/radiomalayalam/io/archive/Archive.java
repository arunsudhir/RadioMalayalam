package com.arunsudhir.radiomalayalam.io.archive;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Environment;

import com.arunsudhir.radiomalayalam.io.ResourceLoader;
import com.arunsudhir.radiomalayalam.logging.Logger;
import com.arunsudhir.radiomalayalam.sql.Condition;
import com.arunsudhir.radiomalayalam.sql.QueryBuilder;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Class that is used to archive files, playlists and songs for the app.
 */
public class Archive extends SQLiteOpenHelper {
    private static final Logger LOG = new Logger(Archive.class);
    private static final File ARCHIVE_FILE = new File(Environment.getExternalStorageDirectory(), "rm_archive.db");
    private static final String TABLE_ARCHIVE = "app_archive";
    private static final String COLUMN_FILE_ID = "file_id";
    private static final String COLUMN_FILE_NAME = "file_name";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_CREATE_DATETIME = "create_datetime";
    private static final ResourceLoader RESOURCE_LOADER = ResourceLoader.forClass(Archive.class).fromFolder("resources");
    private static final String INSERT_FILE_SQL = RESOURCE_LOADER.loadResource("insert_file.sql");

    public Archive(Context context) {
        super(context, ARCHIVE_FILE.toString(), null, 1);
    }

    /**
     * Inserts a file to the archive
     * @param filePath Path of the file
     * @param content Content of the file
     * @return ID of the file
     */
    public ArchiveFile addFile(String filePath, byte[] content) {
        Date createDate = new Date();
        SQLiteStatement insertStatement = getWritableDatabase().compileStatement(INSERT_FILE_SQL);
        insertStatement.bindString(0, filePath);
        insertStatement.bindBlob(1, content);
        long id = insertStatement.executeInsert();
        return getFileById(id).get();
    }

    public Optional<CachedSong> getCachedSong(long id) {
        try (Cursor cursor = QueryBuilder
                .fromTable("songs")
                .where(Condition.eq("song_id", id))
                .execute(this)) {
            if (!cursor.moveToNext()) {
                return Optional.absent();
            }
            long fileId = cursor.getLong(1);
            Optional<ArchiveFile> file = getFileById(fileId);
            if (!file.isPresent()) {
                return Optional.absent();
            }
            return Optional.of(CachedSong
                    .builder()
                    .songId(id)
                    .externalId(cursor.getString(2))
                    .name(cursor.getString(3))
                    .album(cursor.getString(4))
                    .songPath(cursor.getString(5))
                    .singer1(cursor.getString(6))
                    .singer2(cursor.getString(7))
                    .music(cursor.getString(8))
                    .file(file.get())
                    .build());
        }
    }

    public List<Playlist> getPlaylists() {
        ImmutableList.Builder<Playlist> playlists = ImmutableList.builder();
        try (Cursor cursor = QueryBuilder
                .fromTable("playlists")
                .orderBy("playlist_position ASC")
                .execute(this)) {
            while (cursor != null && cursor.moveToNext()) {
                long albumArtFileId = cursor.getLong(3);
                Optional<ArchiveFile> albumArt = getFileById(albumArtFileId);
                playlists.add(Playlist
                        .builder()
                        .id(cursor.getLong(0))
                        .name(cursor.getString(1))
                        .position(cursor.getLong(2))
                        .albumArt(albumArt)
                        .build());
            }
        }
        return playlists.build();
    }

    public List<PlaylistItem> getPlaylist(long id) {
        ImmutableList.Builder<PlaylistItem> playlist = ImmutableList.builder();
        try (Cursor cursor = QueryBuilder
                .fromTable("playlist_items")
                .where(Condition.eq("playlist_id", id))
                .orderBy("item_position ASC")
                .execute(this)) {
            while (cursor != null && cursor.moveToNext()) {
                playlist.add(PlaylistItem
                        .builder()
                        .id(cursor.getLong(0))
                        .songId(cursor.getLong(2))
                        .position(cursor.getLong(3))
                        .createDate(new Date(cursor.getLong(4)))
                        .build());
            }
        }
        return playlist.build();
    }

    public Optional<ArchiveFile> getFileById(long id) {
        return getFileWhere(COLUMN_FILE_ID, id);
    }

    public Optional<ArchiveFile> getFileByName(String fileName) {
        return getFileWhere(COLUMN_FILE_NAME, fileName);
    }

    public List<ArchiveFile> getFilesMatchingName(String fileSqlPattern) {
        return getFilesMatching(COLUMN_FILE_NAME, "LIKE", fileSqlPattern);
    }

    private <T> Optional<ArchiveFile> getFileWhere(String columnName, T value) {
        List<ArchiveFile> files = getFilesMatching(columnName, "=", value);
        if (files.isEmpty()) {
            return Optional.absent();
        }
        return Optional.of(files.get(0));
    }

    private <T> List<ArchiveFile> getFilesMatching(String columnName, String operator, T value) {
        ImmutableList.Builder<ArchiveFile> files = ImmutableList.builder();
        QueryBuilder query = QueryBuilder
                .fromTable(TABLE_ARCHIVE)
                .column(COLUMN_FILE_ID)
                .column(COLUMN_FILE_NAME)
                .column(COLUMN_CONTENT)
                .column(COLUMN_CREATE_DATETIME)
                .where(Condition.where(String.format("%s %s ?", columnName, operator), value.toString()));
        try (Cursor cursor = query.execute(this)) {
            while (cursor != null && cursor.moveToNext()) {
                files.add(ArchiveFile
                        .builder()
                        .id(cursor.getLong(0))
                        .path(cursor.getString(1))
                        .data(cursor.getBlob(2))
                        .createDate(new Date(cursor.getLong(3)))
                        .build());
            }
        }
        return files.build();
    }

    public boolean move(String sourceFile, String destinationFile, boolean overwriteExisting) {
        int conflictResolution = overwriteExisting ? SQLiteDatabase.CONFLICT_REPLACE : SQLiteDatabase.CONFLICT_ABORT;
        ContentValues values = new ContentValues(1);
        values.put(COLUMN_FILE_NAME, destinationFile);
        return getWritableDatabase().updateWithOnConflict(
                TABLE_ARCHIVE,
                values,
                String.format("%s = ?", COLUMN_FILE_NAME),
                new String[]{sourceFile},
                conflictResolution) != 0;
    }

    public boolean deleteFileByName(String fileName) {
        return deleteFileWhere(COLUMN_FILE_NAME, fileName);
    }

    public boolean deleteFileById(long id) {
        return deleteFileWhere(COLUMN_FILE_ID, id);
    }

    private <T> boolean deleteFileWhere(String columnName, T columnValue) {
        return getWritableDatabase().delete(TABLE_ARCHIVE, String.format("%s = ?", columnName), new String[]{columnValue.toString()}) != 0;
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(loadResource("create_archive.sql"));
        db.execSQL(loadResource("create_songs.sql"));
        db.execSQL(loadResource("create_playlists.sql"));
        db.execSQL(loadResource("create_playlist_items.sql"));
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Ignoring for now
        LOG.info("Ignoring DB upgrade from %d to %d", oldVersion, newVersion);
    }

    private String loadResource(String fileName) {
        return RESOURCE_LOADER.loadResource(fileName);
    }
}
