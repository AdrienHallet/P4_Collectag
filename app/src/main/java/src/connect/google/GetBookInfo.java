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
 * <li>Call method execute() method with the request String as parameter</li>
 * </ul>
 * <h2>3. Retrieve Book list</h2>
 * <ul>
 * <li>Your calling class must implement AsyncResponse interface</li>
 * <li>Call method get() on GetBookInfo object (timeout available)</li>
 * <li>Override method processFinish(ArrayList<src.database.object.Book> output)</li>
 * <li>Do something with the ArrayList of books</li>
 * <p>
 * </ul>
 *
 * @author Adrien
 * @version 1.00
 */

public class GetBookInfo extends AsyncTask<String, Void, String> {

    public AsyncResponse delegate = null;

    @Override
    protected String doInBackground(String... bookURLs) { //... = arbitrary number of args
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
        return bookBuilder.toString();
    }

    protected void onPostExecute(String result) {
        try {
            JSONObject resultObject = new JSONObject(result);
            JSONArray bookArray = resultObject.getJSONArray("items");
            ArrayList<Book> bookList = new ArrayList<>();
            for (int i = 0; i < bookArray.length(); i++) {
                JSONObject bookObject = bookArray.getJSONObject(i);
                src.database.object.Book book = new src.database.object.Book(bookObject);
                bookList.add(book);
            }
            delegate.processFinish(bookList);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
