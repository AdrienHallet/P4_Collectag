package src.connect.google;

import java.util.ArrayList;

import src.database.object.Book;

/**
 * Any class that handles book requests must implement it
 * and handle the response in an overrode 'researchFinish' method
 *
 * @author Adrien
 * @version 1.01, 17/02/2017
 *          Changelog : name refactoring (issue with threaded application)
 */

public interface AsyncResponse {
    void researchFinish(ArrayList<Book> output);
}
