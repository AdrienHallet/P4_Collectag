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
    static final String SQL_CREATE_BOOK_ENTRIES =
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

    static final String SQL_CREATE_CATEGORY_ENTRIES =
            "CREATE TABLE " + CategoryEntry.TABLE_NAME + " (" +
                    CategoryEntry.COLUMN_NAME_TITLE + " TEXT NOT NULL UNIQUE ON CONFLICT IGNORE)";

    static final String SQL_CREATE_CATEGORY_RELATIONS =
            "CREATE TABLE " + CategoryManyToMany.TABLE_NAME + " (" +
                    CategoryManyToMany.COLUMN_NAME_BOOK_ID + "TEXT," +
                    CategoryManyToMany.COLUMN_NAME_CATEGORY_ID + " TEXT)";

    /**
     * Delete all tables, columns and data
     */
    static final String SQL_DROP_BOOK_TABLE =
            "DROP TABLE IF EXISTS " + BookEntry.TABLE_NAME;

    static final String SQL_DROP_CATEGORY_TABLE =
            "DROP TABLE IF EXISTS " + CategoryEntry.TABLE_NAME;

    static final String SQL_DROP_CATEGORY_RELATIONS =
            "DROP TABLE IF EXISTS " + CategoryManyToMany.TABLE_NAME;

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

    /**
     * Represent the different categories existing
     */
    static class CategoryEntry implements BaseColumns {
        static final String TABLE_NAME = "categories";
        static final String COLUMN_NAME_TITLE = "book";
    }

    static class CategoryManyToMany implements BaseColumns {
        static final String TABLE_NAME = "categories_many_to_many";
        static final String COLUMN_NAME_CATEGORY_ID = "category_id";
        static final String COLUMN_NAME_BOOK_ID = "book_id";
    }
}
