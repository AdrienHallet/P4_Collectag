package src.connect.google;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import src.commons.ImageHelper;
import src.database.object.Book;

/**
 * Google book API <br />
 * <h1>How to use the API ?</h1>
 * Quick guide on the usage of this API.
 * The API is threaded for non-blocking usage, especially with slow internet
 * <h2>1. Generate request String</h2>
 * <ul>
 * <li>Create new RequestHandler</li>
 * <li>Get request String (see RequestHandler)</li>
 * </ul>
 * <h2>2. Execute request</h2>
 * <ul>
 * <li>Create new GetBookInfo</li>
 * <li>Assign "delegate" to 'this' (UI Thread) (e.g. : myGetBookInfo.delegate = this)</li>
 * <li>Call method execute() method with the request String as parameter</li>
 * </ul>
 * <h2>3. Retrieve Book list</h2>
 * <ul>
 * <li>Your calling class must implement AsyncResponse interface</li>
 * <li>(optional) Call method get() on GetBookInfo object (timeout available)</li>
 * <li>Override method processFinish(ArrayList<src.database.object.Book> output)</li>
 * <li>Do something with the ArrayList of books</li>
 * <p>
 * </ul>
 *
 * @author Adrien
 * @version 1.00
 */

public class GetBookInfo extends AsyncTask<String, Void, ArrayList<Book>> {

    public AsyncResponse delegate = null;

    private ArrayList<Book> result = new ArrayList<>();

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Book> doInBackground(String... bookURLs) { //... = arbitrary number of args
        StringBuilder bookBuilder = new StringBuilder();

        for (String bookSearchURL : bookURLs) {
            //search urls
            Log.d("URL FETCHER", bookSearchURL);
            HttpClient bookClient = HttpClientBuilder.create().build();
            try {
                //get the data
                HttpGet bookGet = new HttpGet(bookSearchURL);
                HttpResponse bookResponse = bookClient.execute(bookGet);
                StatusLine bookSearchStatus = bookResponse.getStatusLine();
                if (bookSearchStatus.getStatusCode() == 200) {
                    HttpEntity bookEntity = bookResponse.getEntity();
                    InputStream bookContent = bookEntity.getContent();
                    InputStreamReader bookInput = new InputStreamReader(bookContent);
                    BufferedReader bookReader = new BufferedReader(bookInput);
                    String lineIn;
                    while ((lineIn = bookReader.readLine()) != null) {
                        bookBuilder.append(lineIn);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        parseBooks(bookBuilder.toString());
        Log.d("Thread check", "1");
        return result;
    }

    private void parseBooks(String json) {
        try {
            Log.d("Thread check", "2");
            JSONObject resultObject = new JSONObject(json);
            JSONArray bookArray = resultObject.getJSONArray("items");
            ArrayList<Book> bookList = new ArrayList<>();
            for (int i = 0; i < bookArray.length(); i++) {
                JSONObject bookObject = bookArray.getJSONObject(i);
                src.database.object.Book book = new src.database.object.Book(bookObject);
                Log.d("Thread check", "3");
                book.setCover(ImageHelper.getImage(ImageHelper.getImageFromURL(book.getCoverUrl())));
                Log.d("Thread check", "4");
                bookList.add(book);
            }
            result = bookList;
        } catch (Exception e) {
            Log.e("Book Parsing Error", e.toString());

        }
    }

    protected void onPostExecute(ArrayList<Book> result) {
        Log.d("Background Thread", "onPostExecute called");
        //ToDo handle null array exception (or throw it to main thread ?)
        delegate.researchFinish(result);
    }
}
