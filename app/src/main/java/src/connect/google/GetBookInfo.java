package src.connect.google;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import src.p4_collectag.StaticEnvironment;

/**
 * Created by Adrien on 16/02/2017.
 */

public class GetBookInfo extends AsyncTask<String, Void, String> {

    public AsyncResponse delegate = null;

    @Override
    protected String doInBackground(String... bookURLs) { //... = arbitrary number of args
        StringBuilder bookBuilder = new StringBuilder();

        for (String bookSearchURL : bookURLs) {
            //search urls
            Log.d("URL FETCHER",bookSearchURL);
            HttpClient bookClient = new DefaultHttpClient();
            try {
                //get the data
                HttpGet bookGet = new HttpGet(bookSearchURL);
                HttpResponse bookResponse = bookClient.execute(bookGet);
                StatusLine bookSearchStatus = bookResponse.getStatusLine();
                if (bookSearchStatus.getStatusCode()==200) {
                    HttpEntity bookEntity = bookResponse.getEntity();
                    InputStream bookContent = bookEntity.getContent();
                    InputStreamReader bookInput = new InputStreamReader(bookContent);
                    BufferedReader bookReader = new BufferedReader(bookInput);
                    String lineIn;
                    while ((lineIn=bookReader.readLine())!=null) {
                        bookBuilder.append(lineIn);
                    }
                }

            }
            catch(Exception e){ e.printStackTrace(); }


        }
        return bookBuilder.toString();
    }

    protected void onPostExecute(String result) {
        try{
            JSONObject resultObject = new JSONObject(result);
            JSONArray bookArray = resultObject.getJSONArray("items");
            JSONObject bookObject = bookArray.getJSONObject(0);
            JSONObject volumeObject = bookObject.getJSONObject("volumeInfo");
            delegate.processFinish(volumeObject.toString());
        }
        catch (Exception e) {
            e.printStackTrace();

        }
    }
}
