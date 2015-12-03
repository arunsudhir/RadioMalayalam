CREATE TABLE IF NOT EXISTS app_archive (
    file_id         INTEGER PRIMARY KEY AUTOINCREMENT,
    file_name       TEXT    NOT NULL UNIQUE,
    content         BLOB,
    create_datetime INTEGER NOT NULL DEFAULT CURRENT_TIMESTAMP
)