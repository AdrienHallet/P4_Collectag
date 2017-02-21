package src.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static src.database.Contract.SQL_CREATE_BOOK_ENTRIES;
import static src.database.Contract.SQL_CREATE_CATEGORY_ENTRIES;
import static src.database.Contract.SQL_CREATE_CATEGORY_RELATIONS;
import static src.database.Contract.SQL_DROP_BOOK_TABLE;
import static src.database.Contract.SQL_DROP_CATEGORY_RELATIONS;
import static src.database.Contract.SQL_DROP_CATEGORY_TABLE;

/**
 * Database helper class
 *
 * @author Adrien
 * @version 1.00
 */

class DatabaseHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 11;
    private static final String DATABASE_NAME = "collectag.db";

    private static DatabaseHelper mInstance = null;

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    /**
     * Called when database is created, execute SQL creation
     *
     * @param db the database
     */
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_BOOK_ENTRIES);
        db.execSQL(SQL_CREATE_CATEGORY_ENTRIES);
        db.execSQL(SQL_CREATE_CATEGORY_RELATIONS);
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
            db.execSQL(SQL_DROP_BOOK_TABLE);
            db.execSQL(SQL_DROP_CATEGORY_TABLE);
            db.execSQL(SQL_DROP_CATEGORY_RELATIONS);
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
