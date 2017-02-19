package src.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import src.connect.google.Book;

/**
 * Created by Adrien on 16/02/2017.
 */

public class Database {

    SQLiteDatabase db;
    private Context context;
    private BookReaderDbHelper dbHelper;
    private Book currentBook = null;

    public Database(Context context) {
        this.context = context;
        this.dbHelper = new BookReaderDbHelper(context);
    }

    /**
     * Dummy method, example & test use only
     */
    public boolean addBook() {
        // Gets the data repository in write mode
        db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(BookContract.BookEntry.COLUMN_ISBN_10, "1853260312");
        values.put(BookContract.BookEntry.COLUMN_ISBN_13, "9781853260315");
        values.put(BookContract.BookEntry.COLUMN_NAME_TITLE, "20 000 miles under the sea");
        values.put(BookContract.BookEntry.COLUMN_NAME_SUBTITLE, "I hate my life");

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(BookContract.BookEntry.TABLE_NAME, null, values);
        db.close();
        return true;
    }

    /**
     * getAllBooks()
     * <p>
     * Return all books in "book" database
     *
     * @return ArrayList<String>
     */
    public ArrayList<String> getAllBooks() {
        db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BookContract.BookEntry._ID,
                BookContract.BookEntry.COLUMN_ISBN_10,
                BookContract.BookEntry.COLUMN_ISBN_13,
                BookContract.BookEntry.COLUMN_NAME_TITLE,
                BookContract.BookEntry.COLUMN_NAME_SUBTITLE
        };

        Cursor cursor = db.rawQuery("SELECT * FROM " + BookContract.BookEntry.TABLE_NAME, null);
        ArrayList<String> values = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME_TITLE));
                values.add(name);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return values;
    }

}
