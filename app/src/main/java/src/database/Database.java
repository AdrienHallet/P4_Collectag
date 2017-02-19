package src.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
    private BookReaderDbHelper dbHelper;

    public Database(Context context) {
        this.dbHelper = new BookReaderDbHelper(context);
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
            values.put(BookContract.BookEntry.COLUMN_NAME_ISBN_10, book.getIsbn10());

        if (book.getIsbn13() != null)
            values.put(BookContract.BookEntry.COLUMN_NAME_ISBN_13, book.getIsbn13());

        if (book.getTitle() != null)
            values.put(BookContract.BookEntry.COLUMN_NAME_TITLE, book.getTitle());

        if (book.getAuthor() != null)
            values.put(BookContract.BookEntry.COLUMN_NAME_AUTHOR, book.getAuthor());

        if (book.getPublishedDate() != null)
            values.put(BookContract.BookEntry.COLUMN_NAME_PUBLISHED_DATE, book.getPublishedDate());

        if (book.getDescription() != null)
            values.put(BookContract.BookEntry.COLUMN_NAME_DESCRIPTION, book.getDescription());

        if (book.getPageCount() != null)
            values.put(BookContract.BookEntry.COLUMN_NAME_PAGE_COUNT, book.getPageCount());

        if (book.getMaturityRating() != null)
            values.put(BookContract.BookEntry.COLUMN_NAME_MATURITY_RATING, book.getMaturityRating());

        if (book.getLanguage() != null)
            values.put(BookContract.BookEntry.COLUMN_NAME_LANGUAGE, book.getLanguage());

        if (book.getCover() != null)
            values.put(BookContract.BookEntry.COLUMN_NAME_COVER, ImageHelper.getBytes(book.getCover()));

        long newRowId = db.insert(BookContract.BookEntry.TABLE_NAME, null, values);
        Log.d("SQL", "Added new row in table books with id " + newRowId);
        db.close();
        return true;
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
                BookContract.BookEntry._ID,
                BookContract.BookEntry.COLUMN_NAME_ISBN_10,
                BookContract.BookEntry.COLUMN_NAME_ISBN_13,
                BookContract.BookEntry.COLUMN_NAME_TITLE,
                BookContract.BookEntry.COLUMN_NAME_AUTHOR,
                BookContract.BookEntry.COLUMN_NAME_PUBLISHED_DATE,
                BookContract.BookEntry.COLUMN_NAME_DESCRIPTION,
                BookContract.BookEntry.COLUMN_NAME_PAGE_COUNT,
                BookContract.BookEntry.COLUMN_NAME_MATURITY_RATING,
                BookContract.BookEntry.COLUMN_NAME_LANGUAGE,
                BookContract.BookEntry.COLUMN_NAME_COVER
        };

        Cursor cursor = db.rawQuery("SELECT * FROM " + BookContract.BookEntry.TABLE_NAME, null);
        ArrayList<Book> values = new ArrayList<>();
        if (cursor.moveToFirst()) {
            Log.d("SQL", "Book table contains " + cursor.getColumnCount() + " columns named :");
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                Log.d("SQL", cursor.getColumnNames()[i]);
            }
            while (!cursor.isAfterLast()) {
                Book book = new Book();

                if (!cursor.isNull(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME_TITLE)))
                    book.setTitle(cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME_TITLE)));

                if (!cursor.isNull(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME_AUTHOR)))
                    book.setAuthor(cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME_AUTHOR)));

                if (!cursor.isNull(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME_PUBLISHED_DATE)))
                    book.setPublishedDate(cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME_PUBLISHED_DATE)));

                if (!cursor.isNull(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME_DESCRIPTION)))
                    book.setDescription(cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME_DESCRIPTION)));

                if (!cursor.isNull(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME_ISBN_10)))
                    book.setIsbn10(cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME_ISBN_10)));

                if (!cursor.isNull(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME_ISBN_13)))
                    book.setIsbn13(cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME_ISBN_13)));

                if (!cursor.isNull(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME_PAGE_COUNT)))
                    book.setPageCount(cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME_PAGE_COUNT)));

                if (!cursor.isNull(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME_MATURITY_RATING)))
                    book.setMaturityRating(cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME_MATURITY_RATING)));

                if (!cursor.isNull(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME_LANGUAGE)))
                    book.setLanguage(cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME_LANGUAGE)));

                if (!cursor.isNull(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME_COVER)))
                    book.setCover(ImageHelper.getImage(cursor.getBlob(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME_COVER))));

                values.add(book);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return values;
    }

}
