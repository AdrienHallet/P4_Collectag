package src.commons;

/**
 * Exception class representing a book not found
 *
 * @author Adrien
 * @version 1.00
 */

public class BookException extends Exception{

    public static final int NO_BOOK_FOUND = 0; //Todo wait for api compatibility, then redo
    public static final int NO_COVER_FOUND = 1;

    private int errorType;

    public BookException() {
    }

    public BookException(String message) {
        super(message);
    }

    public BookException(int errorType){
        this.errorType = errorType;
    }

    public int getErrorType() {
        return errorType;
    }

    public void setErrorType(int errorType) {
        this.errorType = errorType;
    }

    public String toString(){
        switch(this.errorType){
            case 0 :
                return "No book found";
            case 1 :
                return "No cover found";
            default:
                return "Unknown error";
        }
    }
}
