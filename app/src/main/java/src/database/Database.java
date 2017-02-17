package src.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import src.commons.ISBN;
import src.connect.google.AsyncResponse;
import src.connect.google.Book;
import src.connect.google.GetBookInfo;
import src.connect.google.RequestHandler;
import src.p4_collectag.StaticEnvironment;

/**
 * Created by Adrien on 16/02/2017.
 */

public class Database implements AsyncResponse {

    private Context context;
    private BookReaderDbHelper dbHelper;
    SQLiteDatabase db;

    private Book currentBook = null;

    public Database(Context context){
        this.context = context;
        this.dbHelper = new BookReaderDbHelper(context);
    }

    /**
     * Dummy method, example & test use only
     */
    public boolean addBook(){
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

    public boolean addBookISBN(String isbn){
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        if(ISBN.isISBN10(isbn)){
            values.put(BookContract.BookEntry.COLUMN_ISBN_10, isbn);
            values.put(BookContract.BookEntry.COLUMN_ISBN_13, ISBN.convert10to13(isbn));
        }else if(ISBN.isISBN13(isbn)){
            values.put(BookContract.BookEntry.COLUMN_ISBN_10, ISBN.convert13to10(isbn));
            values.put(BookContract.BookEntry.COLUMN_ISBN_13, isbn);
        }else{
            Log.e("ISBN", "Not a valid ISBN :"+isbn);
            return false;
        }

        RequestHandler handler = new RequestHandler();
        GetBookInfo request = new GetBookInfo();
        request.delegate = this; //claim to receive response here
        request.execute(handler.isbnRequest(isbn));
        try {
            request.get(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            //ToDo Do something with the interruption
        } catch (ExecutionException e) {
            //ToDo Do something when error occurs
        } catch (TimeoutException e) {
            //ToDo Do something when response is late
        }
        return true;

    }

    /**
     * getAllBooks()
     *
     * Return all books in "book" database
     * @return ArrayList<String>
     */
    public ArrayList<String> getAllBooks(){
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

        Cursor cursor = db.rawQuery("SELECT * FROM "+BookContract.BookEntry.TABLE_NAME,null);
        ArrayList<String> values = new ArrayList<>();
        if (cursor .moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                String name = cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME_TITLE));
                values.add(name);
                cursor.moveToNext();
            }
        }

        return values;
    }

    @Override
    public void processFinish(String output){
        StaticEnvironment.mainActivity.snackThis(output);
        //ToDo Parse result (or change output type and process directly in response creator) into the current book
    }

}
