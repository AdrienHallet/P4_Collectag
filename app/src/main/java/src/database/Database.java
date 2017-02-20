package src.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import src.commons.ImageHelper;
import src.database.object.Book;

/**
 * Interaction class with the database
 *
 * @author Adrien
 * @version 2.00
 */

public class Database {

    SQLiteDatabase db;
    private DatabaseHelper dbHelper;
    private static final int SMART_REMOVAL_ALLOWED_ERRORS = 0;

    public Database(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    /**
     * Add one book from book database object
     *
     * @param book the book to save in the database
     * @return true if operation was successful, false otherwise
     */
    public boolean addBook(Book book) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        if (book.getIsbn10() != null)
            values.put(Contract.BookEntry.COLUMN_NAME_ISBN_10, book.getIsbn10());

        if (book.getIsbn13() != null)
            values.put(Contract.BookEntry.COLUMN_NAME_ISBN_13, book.getIsbn13());

        if (book.getTitle() != null)
            values.put(Contract.BookEntry.COLUMN_NAME_TITLE, book.getTitle());

        if (book.getAuthor() != null)
            values.put(Contract.BookEntry.COLUMN_NAME_AUTHOR, book.getAuthor());

        if (book.getPublishedDate() != null)
            values.put(Contract.BookEntry.COLUMN_NAME_PUBLISHED_DATE, book.getPublishedDate());

        if (book.getDescription() != null)
            values.put(Contract.BookEntry.COLUMN_NAME_DESCRIPTION, book.getDescription());

        if (book.getPageCount() != null)
            values.put(Contract.BookEntry.COLUMN_NAME_PAGE_COUNT, book.getPageCount());

        if (book.getMaturityRating() != null)
            values.put(Contract.BookEntry.COLUMN_NAME_MATURITY_RATING, book.getMaturityRating());

        if (book.getLanguage() != null)
            values.put(Contract.BookEntry.COLUMN_NAME_LANGUAGE, book.getLanguage());

        if (book.getCover() != null)
            values.put(Contract.BookEntry.COLUMN_NAME_COVER, ImageHelper.getBytes(book.getCover()));

        long newRowId = db.insert(Contract.BookEntry.TABLE_NAME, null, values);
        Log.d("SQL", "Added new row in table books with id " + newRowId);
        db.close();
        return true;
    }

    /**
     * Remove specified book from the database
     *
     * @param book the book to remove from the database (irreversible)
     * @return true if operation was successful, false otherwise
     */
    public boolean removeBook(Book book) {
        db = dbHelper.getWritableDatabase();
        if (book.getIsbn13() != null)
            db.execSQL("DELETE FROM " + Contract.BookEntry.TABLE_NAME + " WHERE " + Contract.BookEntry.COLUMN_NAME_ISBN_13 + "='" + book.getIsbn13() + "'");

        else if (book.getIsbn10() != null)
            db.execSQL("DELETE FROM " + Contract.BookEntry.TABLE_NAME + " WHERE " + Contract.BookEntry.COLUMN_NAME_ISBN_10 + "='" + book.getIsbn10() + "'");
        else {
            db.close();
            return smartRemoval(book);
        }

        db.close();
        return true; //FixMe : is it useful to check deletion
    }

    /**
     * Smart deletion method when primary key cannot be used to get the book<br />
     * This will remove the first book matching the parameter with the error margin 'SMART_REMOVAL_ALLOWED_ERRORS'
     * Be careful, if you pass empty book, null match will occur (missing information is considered as same information)
     *
     * @param book to book to remove
     * @return true if book has been safely deleted
     */
    private boolean smartRemoval(Book book) {
        db = dbHelper.getWritableDatabase();
        ArrayList<Book> bookList = getAllBooks();
        for (Book current : bookList) {
            int diff = current.differentiate(book);
            if (diff <= SMART_REMOVAL_ALLOWED_ERRORS) {
                if (current.getIsbn10() != null) {
                    db.execSQL("DELETE FROM " + Contract.BookEntry.TABLE_NAME + " WHERE " + Contract.BookEntry.COLUMN_NAME_ISBN_10 + "='" + book.getIsbn10() + "'");
                    return true;
                } else if (current.getIsbn13() != null) {
                    db.execSQL("DELETE FROM " + Contract.BookEntry.TABLE_NAME + " WHERE " + Contract.BookEntry.COLUMN_NAME_ISBN_13 + "='" + book.getIsbn13() + "'");
                    return true;
                } else if (current.getDescription() != null) {
                    db.execSQL("DELETE FROM " + Contract.BookEntry.TABLE_NAME + " WHERE " + Contract.BookEntry.COLUMN_NAME_DESCRIPTION + "='" + book.getDescription() + "'");
                    return true;
                } else if (current.getTitle() != null) {
                    db.execSQL("DELETE FROM " + Contract.BookEntry.TABLE_NAME + " WHERE " + Contract.BookEntry.COLUMN_NAME_TITLE + "='" + book.getTitle() + "'");
                    return true;
                } else
                    return false;

            }
        }
        db.close();
        return false;
    }

    /**
     * getAllBooks()
     * <p>
     * Return all books in "book" database
     *
     * @return ArrayList<Book> all books in database
     */
    public ArrayList<Book> getAllBooks() {
        db = dbHelper.getReadableDatabase();

        String[] projection = { //Can be used in query, not yet though
                Contract.BookEntry._ID,
                Contract.BookEntry.COLUMN_NAME_ISBN_10,
                Contract.BookEntry.COLUMN_NAME_ISBN_13,
                Contract.BookEntry.COLUMN_NAME_TITLE,
                Contract.BookEntry.COLUMN_NAME_AUTHOR,
                Contract.BookEntry.COLUMN_NAME_PUBLISHED_DATE,
                Contract.BookEntry.COLUMN_NAME_DESCRIPTION,
                Contract.BookEntry.COLUMN_NAME_PAGE_COUNT,
                Contract.BookEntry.COLUMN_NAME_MATURITY_RATING,
                Contract.BookEntry.COLUMN_NAME_LANGUAGE,
                Contract.BookEntry.COLUMN_NAME_COVER
        };

        Cursor cursor = db.rawQuery("SELECT * FROM " + Contract.BookEntry.TABLE_NAME, null);
        ArrayList<Book> values = new ArrayList<>();
        if (cursor.moveToFirst()) {
            Log.d("SQL", "Book table contains " + cursor.getColumnCount() + " columns named :");
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                Log.d("SQL", cursor.getColumnNames()[i]);
            }
            while (!cursor.isAfterLast()) {
                Book book = new Book();

                if (!cursor.isNull(cursor.getColumnIndex(Contract.BookEntry.COLUMN_NAME_TITLE)))
                    book.setTitle(cursor.getString(cursor.getColumnIndex(Contract.BookEntry.COLUMN_NAME_TITLE)));

                if (!cursor.isNull(cursor.getColumnIndex(Contract.BookEntry.COLUMN_NAME_AUTHOR)))
                    book.setAuthor(cursor.getString(cursor.getColumnIndex(Contract.BookEntry.COLUMN_NAME_AUTHOR)));

                if (!cursor.isNull(cursor.getColumnIndex(Contract.BookEntry.COLUMN_NAME_PUBLISHED_DATE)))
                    book.setPublishedDate(cursor.getString(cursor.getColumnIndex(Contract.BookEntry.COLUMN_NAME_PUBLISHED_DATE)));

                if (!cursor.isNull(cursor.getColumnIndex(Contract.BookEntry.COLUMN_NAME_DESCRIPTION)))
                    book.setDescription(cursor.getString(cursor.getColumnIndex(Contract.BookEntry.COLUMN_NAME_DESCRIPTION)));

                if (!cursor.isNull(cursor.getColumnIndex(Contract.BookEntry.COLUMN_NAME_ISBN_10)))
                    book.setIsbn10(cursor.getString(cursor.getColumnIndex(Contract.BookEntry.COLUMN_NAME_ISBN_10)));

                if (!cursor.isNull(cursor.getColumnIndex(Contract.BookEntry.COLUMN_NAME_ISBN_13)))
                    book.setIsbn13(cursor.getString(cursor.getColumnIndex(Contract.BookEntry.COLUMN_NAME_ISBN_13)));

                if (!cursor.isNull(cursor.getColumnIndex(Contract.BookEntry.COLUMN_NAME_PAGE_COUNT)))
                    book.setPageCount(cursor.getString(cursor.getColumnIndex(Contract.BookEntry.COLUMN_NAME_PAGE_COUNT)));

                if (!cursor.isNull(cursor.getColumnIndex(Contract.BookEntry.COLUMN_NAME_MATURITY_RATING)))
                    book.setMaturityRating(cursor.getString(cursor.getColumnIndex(Contract.BookEntry.COLUMN_NAME_MATURITY_RATING)));

                if (!cursor.isNull(cursor.getColumnIndex(Contract.BookEntry.COLUMN_NAME_LANGUAGE)))
                    book.setLanguage(cursor.getString(cursor.getColumnIndex(Contract.BookEntry.COLUMN_NAME_LANGUAGE)));

                if (!cursor.isNull(cursor.getColumnIndex(Contract.BookEntry.COLUMN_NAME_COVER)))
                    book.setCover(ImageHelper.getImage(cursor.getBlob(cursor.getColumnIndex(Contract.BookEntry.COLUMN_NAME_COVER))));

                values.add(book);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return values;
    }

    public long getBookCount() {
        db = dbHelper.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, Contract.BookEntry.TABLE_NAME);
        db.close();
        return count;
    }

    public void wipeDatabase() {
        db = dbHelper.getWritableDatabase();
        db.execSQL(Contract.SQL_DROP_ALL_TABLES);
        db.execSQL(Contract.SQL_CREATE_ENTRIES);
        db.close();
    }

}
