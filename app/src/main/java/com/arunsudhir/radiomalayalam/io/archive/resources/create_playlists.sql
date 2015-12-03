CREATE TABLE IF NOT EXISTS playlists (
    playlist_id             INTEGER PRIMARY KEY AUTOINCREMENT,
    name                    TEXT    NOT NULL,
    playlist_position       INTEGER NOT NULL,
    playlist_art_file_id    INTEGER REFERENCES app_archive(file_id) ON DELETE SET NULL,
    create_datetime         INTEGER NOT NULL DEFAULT CURRENT_TIMESTAMP
)