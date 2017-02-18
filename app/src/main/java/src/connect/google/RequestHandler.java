package src.connect.google;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Generate mandatory String requests for Google Book API
 *
 * @author Adrien
 * @version 1.00
 */

public class RequestHandler {
    private static final String API_BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final String ACCESS_KEY = "AIzaSyDdOB9TPOJi_RwKnMnjrgsMbuXnXu5FpU0"; //Please don't share
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

    /**
     * Generate an isbn search on Google Book API
     *
     * @param query the isbn you're looking for
     * @return the request to use on GetBookInfo
     */
    public String isbnRequest(final String query) {
        try {
            String url = getApiUrl("isbn:");
            return url + URLEncoder.encode(query, "utf-8") + "&Key=" + ACCESS_KEY;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Generate an unspecified search on Google Book API
     *
     * @param query the keywords you're looking for
     * @return the request to use on GetBookInfo
     */
    public String getUnspecified(final String query) {
        try {
            String url = getApiUrl("");
            return url + URLEncoder.encode(query, "utf-8") + "&Key=" + ACCESS_KEY;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

}