package src.connect.google;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Adrien on 16/02/2017.
 */

public class RequestHandler {
    private static final String API_BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final String ACCESS_KEY = "AIzaSyDdOB9TPOJi_RwKnMnjrgsMbuXnXu5FpU0";
    private AsyncHttpClient client;

    public RequestHandler() {
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }

    // Method for accessing the search API
    public void createRequest(final String query, JsonHttpResponseHandler handler) {
        try {
            String url = getApiUrl("");
            client.get(url + URLEncoder.encode(query, "utf-8"), handler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    public String isbnRequest(final String query) {
        try {
            String url = getApiUrl("isbn:");
            return url + URLEncoder.encode(query, "utf-8")+"&Key="+ACCESS_KEY;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}