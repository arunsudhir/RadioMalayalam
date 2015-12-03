package com.arunsudhir.radiomalayalam.sql;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import lombok.Getter;

/**
 * A helper class to begin and end a SQLite transactions.
 */
public class SqliteTransaction implements AutoCloseable {
    @Getter
    private final SQLiteDatabase database;
    private boolean wasSuccessful = false;

    public SqliteTransaction(SQLiteOpenHelper helper) {
        this.database = helper.getWritableDatabase();
        this.database.beginTransaction();
    }

    public void setSuccessful() {
        this.wasSuccessful = true;
    }

    /**
     * Closes the object and release any system resources it holds.
     */
    @Override
    public void close() {
        if (wasSuccessful) {
            database.endTransaction();
        }
    }
}
