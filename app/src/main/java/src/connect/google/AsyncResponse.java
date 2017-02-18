package src.connect.google;

import java.util.ArrayList;

import src.database.object.Book;

/**
 * Any class that handles book requests must implement it
 * and handle the response in an overrode 'processFinish' method
 *
 * @author Adrien
 * @version 1.00, 17/02/2017
 */

public interface AsyncResponse {
    void processFinish(ArrayList<Book> output);
}
