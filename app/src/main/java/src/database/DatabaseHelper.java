package src.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static src.database.Contract.SQL_CREATE_ENTRIES;
import static src.database.Contract.SQL_DROP_ALL_TABLES;

/**
 * Database helper class
 *
 * @author Adrien
 * @version 1.00
 */

class DatabaseHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    static final int DATABASE_VERSION = 4;
    static final String DATABASE_NAME = "collectag.db";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when database is created, execute SQL creation
     *
     * @param db the database
     */
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    /**
     * Called when database schema version changes
     * and oldVersion < newVersion
     *
     * @param db         the database
     * @param oldVersion the old schema version
     * @param newVersion the new schema version
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            Log.d("SQL", "New database version found. Wiping existing data " + oldVersion + " -> " + newVersion);
            db.execSQL(SQL_DROP_ALL_TABLES);
            onCreate(db);
        }
    }

    /**
     * Called when database schema version changes
     * and oldVersion > newVersion
     *
     * @param db         the database
     * @param oldVersion the old schema version
     * @param newVersion the new schema version
     */
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
