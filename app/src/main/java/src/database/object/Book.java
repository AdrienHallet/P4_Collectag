package src.database.object;

import org.json.JSONObject;

/**
 * Work still in progress
 */

public class Book {

    public String title;
    public String author;
    public String publishedDate;
    public String description;
    public String isbn10;
    public String isbn13;
    public String pageCount;
    public String maturityRating;
    public String language;
    public String cover;

    public Book() {

    }

    public Book(String title, String author, String publishedDate, String description, String isbn10, String isbn13, String pageCount, String maturityRating, String language, String cover) {
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

    public Book(JSONObject jsonObject) {
        //ToDo parse json
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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
