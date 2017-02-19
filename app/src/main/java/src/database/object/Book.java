package src.database.object;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents a book
 *
 * @author Adrien
 * @version 2.00
 */

public class Book {

    private String title;
    private String author;
    private String publishedDate;
    private String description;
    private String isbn10;
    private String isbn13;
    private String pageCount;
    private String maturityRating;
    private String language;
    private String coverUrl;
    private Bitmap cover;

    /**
     * Empty constructor
     */
    public Book() {

    }

    /**
     * Create a new book by its title
     *
     * @param title book's title
     */
    public Book(String title) {
        this.title = title;
    }

    /**
     * Create a book with specified arguments
     */
    public Book(String title, String author, String publishedDate, String description, String isbn10, String isbn13, String pageCount, String maturityRating, String language, Bitmap cover) {
        this.title = title;
        this.author = author;
        this.publishedDate = publishedDate;
        this.description = description;
        this.isbn10 = isbn10;
        this.isbn13 = isbn13;
        this.pageCount = pageCount;
        this.maturityRating = maturityRating;
        this.language = language;
        this.cover = cover;
    }

    /**
     * Create a book from a JSON file
     *
     * @param jsonObject JSON object representing one book (currently supporting Google JSON key only)
     */
    public Book(JSONObject jsonObject) {
        try {
            if (jsonObject.has("volumeInfo")) {
                JSONObject volume = jsonObject.getJSONObject("volumeInfo");
                this.title = volume.optString("title");
                this.author = getAuthorList(volume);
                this.publishedDate = volume.optString("publishedDate");
                this.description = volume.optString("description");
                this.pageCount = volume.optString("pageCount");
                this.maturityRating = volume.optString("maturityRating");
                this.language = volume.optString("language");
                this.coverUrl = volume.optJSONObject("imageLinks").optString("thumbnail");

                if (volume.has("industryIdentifiers")) {
                    JSONArray identifiers = volume.getJSONArray("industryIdentifiers");
                    for (int i = 0; i < identifiers.length(); i++) {
                        JSONObject object = identifiers.getJSONObject(i);
                        if (object.optString("type").equals("ISBN_10")) {
                            this.isbn10 = object.optString("identifier");
                        } else if (object.optString("type").equals("ISBN_13")) {
                            this.isbn13 = object.optString("identifier");
                        } else {
                            //ToDo
                            //Non-ISBN format. Do we need to handle that ?
                        }
                    }
                }


            }
        } catch (JSONException e) {
            Log.e("Book constructor", e.toString());
        } catch (Exception e) {
            Log.e("Book constructor", "Unexpected error "+e.toString());
        }
    }

    // Return comma separated author list when there is more than one author
    private static String getAuthorList(final JSONObject jsonObject) {
        try {
            final JSONArray authors = jsonObject.getJSONArray("authors");
            int numAuthors = authors.length();
            final String[] authorStrings = new String[numAuthors];
            for (int i = 0; i < numAuthors; ++i) {
                authorStrings[i] = authors.getString(i);
            }
            return TextUtils.join(", ", authorStrings);
        } catch (JSONException e) {
            return "";
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getPageCount() {
        return pageCount;
    }

    public void setPageCount(String pageCount) {
        this.pageCount = pageCount;
    }

    public String getMaturityRating() {
        return maturityRating;
    }

    public void setMaturityRating(String maturityRating) {
        this.maturityRating = maturityRating;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public Bitmap getCover() {
        return cover;
    }

    public void setCover(Bitmap cover) {
        this.cover = cover;
    }
}
