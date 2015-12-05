CREATE TABLE IF NOT EXISTS songs (
    song_id     INTEGER PRIMARY KEY AUTOINCREMENT,
    file_id     INTEGER NOT NULL REFERENCES app_archive(file_id) ON DELETE CASCADE,
    external_id TEXT    NOT NULL,
    song_name   TEXT    NOT NULL,
    album       TEXT,
    song_path   TEXT    NOT NULL,
    singer1     TEXT,
    singer2     TEXT,
    music       TEXT
)