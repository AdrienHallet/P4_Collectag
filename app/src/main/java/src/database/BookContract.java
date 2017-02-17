package src.database;

import android.provider.BaseColumns;

/**
 * Created by Adrien on 16/02/2017.
 */

public class BookContract {

    private BookContract() {}

    public static class BookEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_ISBN_13 = "isbn_13";
        public static final String COLUMN_ISBN_10 = "isbn_10";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
        public static final String COLUMN_COVER = "cover";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + BookEntry.TABLE_NAME + " (" +
                    BookEntry._ID + " INTEGER PRIMARY KEY," +
                    BookEntry.COLUMN_ISBN_13+ " TEXT," +
                    BookEntry.COLUMN_ISBN_10+ " TEXT," +
                    BookEntry.COLUMN_NAME_TITLE + " TEXT," +
                    BookEntry.COLUMN_NAME_SUBTITLE + " TEXT" +
                    BookEntry.COLUMN_COVER + " BLOB)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + BookEntry.TABLE_NAME;
}
