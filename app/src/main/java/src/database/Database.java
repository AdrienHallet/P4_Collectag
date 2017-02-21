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
import src.database.object.CategoryItem;

/**
 * Interaction class with the database
 *
 * @author Adrien
 * @version 2.00
 *          Changelog :
 *          <ul>
 *          <li>2.00 : Now supports category</li>
 *          </ul>
 */

public class Database {

    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public Database(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
    }

    /* BOOK */

    /**
     * Add one book from book database object
     *
     * @param book the book to save in the database
     * @return the id of inserted row
     */
    public long addBook(Book book) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = getContentValues(book);

        long newRowId = db.insert(Contract.BookEntry.TABLE_NAME, null, values);
        Log.d("SQL", "Added new row in table books with id " + newRowId);
        db.close();
        book.setRowId(newRowId);
        return newRowId;
    }

    private ContentValues getContentValues(Book book) {
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

        return values;
    }

    /**
     * Update the information in the book database<br />
     * You can't update a book that is not already in the database
     *
     * @param book the book to update
     * @return true if book could be updated, false if ID does not exist
     */
    public boolean updateBook(Book book) {

        if (book.getRowId() < 0) return false;

        db = dbHelper.getWritableDatabase();

        ContentValues values = getContentValues(book);
        String[] args = {String.valueOf(book.getRowId())};
        long updatedRow = db.update(Contract.BookEntry.TABLE_NAME, values, "rowid = ?", args);
        db.close();
        return book.getRowId() == updatedRow;
    }

    /**
     * Remove specified book from the database
     *
     * @param book the book to remove from the database (irreversible)
     * @return true if operation was successful, false otherwise
     */
    public boolean removeBook(Book book) {
        if (book.getRowId() < 0) return false;
        db = dbHelper.getWritableDatabase();
        db.delete(Contract.BookEntry.TABLE_NAME, "rowid=" + book.getRowId(), null);
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

        Cursor cursor = db.rawQuery("SELECT * FROM " + Contract.BookEntry.TABLE_NAME, null);
        ArrayList<Book> values = new ArrayList<>();
        if (cursor.moveToFirst()) {
            Log.d("SQL", "Book table contains " + cursor.getColumnCount() + " columns named :");
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                Log.d("SQL", cursor.getColumnNames()[i]);
            }
            while (!cursor.isAfterLast()) {
                Book book = new Book();
                long rowid = cursor.getLong(0);
                book.setRowId(rowid + 1);

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
        db.close();
        return values;
    }

    /* CATEGORY */

    /**
     * Add given category to the database<br />
     * <b>Warning : if name already exists, we will not add category</b>
     *
     * @param categoryItem the category to add
     * @return the id of created row in database
     */
    public long addCategory(CategoryItem categoryItem) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = getContentValues(categoryItem);

        long newRowId = db.insert(Contract.CategoryEntry.TABLE_NAME, null, values);
        Log.d("SQL", "Added new row in table category with id " + newRowId);
        db.close();
        categoryItem.setRowId(newRowId);
        return newRowId;
    }

    public boolean updateCategory(CategoryItem categoryItem) {
        if (categoryItem.getRowId() < 0) return false;

        db = dbHelper.getWritableDatabase();

        ContentValues values = getContentValues(categoryItem);
        String[] args = {String.valueOf(categoryItem.getRowId())};
        long updatedRow = db.update(Contract.CategoryEntry.TABLE_NAME, values, "rowid = ?", args);
        db.close();
        return categoryItem.getRowId() == updatedRow;
    }

    private ContentValues getContentValues(CategoryItem categoryItem) {
        ContentValues values = new ContentValues();

        if (categoryItem.getTitle() != null && categoryItem.getTitle().trim() != "")
            values.put(Contract.CategoryEntry.COLUMN_NAME_TITLE, categoryItem.getTitle());

        return values;
    }

    /**
     * Get all categories in database
     *
     * @return an arraylist containing the categories item found
     */
    public ArrayList<CategoryItem> getAllCategories() {
        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Contract.CategoryEntry.TABLE_NAME, null);
        ArrayList<CategoryItem> values = new ArrayList<>();
        if (cursor.moveToFirst()) {
            Log.d("SQL", "Category table contains " + cursor.getColumnCount() + " columns named :");
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                Log.d("SQL", cursor.getColumnNames()[i]);
            }
            while (!cursor.isAfterLast()) {
                CategoryItem categoryItem = new CategoryItem();

                categoryItem.setRowId(cursor.getLong(0) + 1);

                if (!cursor.isNull(cursor.getColumnIndex(Contract.CategoryEntry.COLUMN_NAME_TITLE)))
                    categoryItem.setTitle(cursor.getString(cursor.getColumnIndex(Contract.CategoryEntry.COLUMN_NAME_TITLE)));

                values.add(categoryItem);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
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
        db.execSQL(Contract.SQL_DROP_BOOK_TABLE);
        db.execSQL(Contract.SQL_DROP_CATEGORY_TABLE);
        db.execSQL(Contract.SQL_CREATE_BOOK_ENTRIES);
        db.execSQL(Contract.SQL_CREATE_CATEGORY_ENTRIES);
        db.close();
    }

}
