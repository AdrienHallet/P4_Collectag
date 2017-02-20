package src.connect.google;

import java.util.ArrayList;

import src.commons.BookException;
import src.database.object.Book;

/**
 * Any class that handles book requests must implement it
 * and handle the response in an overrode 'researchFinish' method
 *
 * @author Adrien
 * @version 1.02, 17/02/2017
 *          <p>
 *          1.00 : base
 *          1.01 : name refactoring (issue with threaded application)
 *          1.02 : new researchNullResult() method
 */
public interface AsyncResponse {
    /**
     * Asynchronous response method, called when the API finished working<br />
     * <b>Warning :</b> can be empty
     *
     * @param output the list containing the result
     */
    void researchFinish(ArrayList<Book> output);

    /**
     * Asynchronous response method, called when the API has no result
     * <br />Handle the void result here
     */
    void researchNullResult();
}
