package src.database;

import android.provider.BaseColumns;

/**
 * Contract class for the database object 'Book'
 *
 * @author Adrien
 * @version 3.00 (now blobbed)
 */

class Contract {

    /**
     * Create all tables and columns
     */
    static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + BookEntry.TABLE_NAME + " (" +
                    BookEntry.COLUMN_NAME_ISBN_13 + " TEXT UNIQUE ON CONFLICT REPLACE," +
                    BookEntry.COLUMN_NAME_ISBN_10 + " TEXT UNIQUE ON CONFLICT REPLACE," +
                    BookEntry.COLUMN_NAME_TITLE + " TEXT," +
                    BookEntry.COLUMN_NAME_AUTHOR + " TEXT," +
                    BookEntry.COLUMN_NAME_PUBLISHED_DATE + " TEXT," +
                    BookEntry.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    BookEntry.COLUMN_NAME_PAGE_COUNT + " TEXT," +
                    BookEntry.COLUMN_NAME_MATURITY_RATING + " TEXT," +
                    BookEntry.COLUMN_NAME_LANGUAGE + " TEXT," +
                    BookEntry.COLUMN_NAME_COVER + " BLOB)";

    /**
     * Delete all tables, columns and data
     */
    static final String SQL_DROP_ALL_TABLES =
            "DROP TABLE IF EXISTS " + BookEntry.TABLE_NAME;


    private Contract() {
    }

    /**
     * Represent the book table and columns
     */
    static class BookEntry implements BaseColumns {
        static final String TABLE_NAME = "books";
        static final String COLUMN_NAME_ISBN_13 = "isbn_13";
        static final String COLUMN_NAME_ISBN_10 = "isbn_10";
        static final String COLUMN_NAME_TITLE = "title";
        static final String COLUMN_NAME_AUTHOR = "author";
        static final String COLUMN_NAME_PUBLISHED_DATE = "published_date";
        static final String COLUMN_NAME_DESCRIPTION = "description";
        static final String COLUMN_NAME_PAGE_COUNT = "page_count";
        static final String COLUMN_NAME_MATURITY_RATING = "maturity_rating";
        static final String COLUMN_NAME_LANGUAGE = "language";
        static final String COLUMN_NAME_COVER = "cover";
    }
}
