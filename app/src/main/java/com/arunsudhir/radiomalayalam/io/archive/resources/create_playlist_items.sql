CREATE TABLE IF NOT EXISTS playlist_items (
    playlist_item_id        INTEGER PRIMARY KEY AUTOINCREMENT,
    playlist_id             INTEGER NOT NULL REFERENCES playlists(playlist_id) ON DELETE CASCADE,
    song_id                 INTEGER NOT NULL REFERENCES songs(song_id) ON DELETE CASCADE,
    item_position           INTEGER NOT NULL,
    create_datetime         INTEGER NOT NULL DEFAULT CURRENT_TIMESTAMP
)