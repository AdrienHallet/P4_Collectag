package src.commons;

import android.support.annotation.NonNull;

/**
 * Created by Adrien on 17/02/2017.
 *
 * Class to test ISBN numbers' validity
 * Asserted by ISBNUnitTest
 */

public class ISBN {

    /**
     * isISBN
     * @param isbn a number
     * @return true if isbn is a valid ISBN-10 or ISBN-13, false otherwise
     */
    public static boolean isISBN(String isbn){
        if(isbn == null)
            return false;

        isbn = isbn.replaceAll( "-", "" );

        if(isbn.length() == 10){
            try
            {
                int tot = 0;
                for ( int i = 0; i < 9; i++ )
                {
                    int digit = Integer.parseInt( isbn.substring( i, i + 1 ) );
                    tot += ((10 - i) * digit);
                }

                String checksum = Integer.toString( (11 - (tot % 11)) % 11 );
                if ( "10".equals( checksum ) )
                {
                    checksum = "X";
                }

                return checksum.equals( isbn.substring( 9 ) );
            }
            catch ( NumberFormatException nfe )
            {
                //to catch invalid ISBNs that have non-numeric characters in them
                return false;
            }
        }
        else if(isbn.length() == 13){
            try
            {
                int tot = 0;
                for ( int i = 0; i < 12; i++ )
                {
                    int digit = Integer.parseInt( isbn.substring( i, i + 1 ) );
                    tot += (i % 2 == 0) ? digit : digit * 3;
                }

                //checksum must be 0-9. If calculated as 10 then = 0
                int checksum = 10 - (tot % 10);
                if ( checksum == 10 )
                {
                    checksum = 0;
                }

                return checksum == Integer.parseInt( isbn.substring( 12 ) );
            }
            catch ( NumberFormatException nfe )
            {
                //to catch invalid ISBNs that have non-numeric characters in them
                return false;
            }
        }
        else return false;

    }

    /**
     * isISBN10
     * @param isbn a String
     * @return true if isbn is a valid ISBN-10, false otherwise
     */
    public static boolean isISBN10(String isbn) {
        return isISBN(isbn) && isbn.length() == 10;
    }

    /**
     * isISBN10
     * @param isbn a Long
     * @return true if isbn is a valid ISBN-10, false otherwise
     */
    public static boolean isISBN10(Long isbn) {
        return isISBN(String.valueOf(isbn)) && String.valueOf(isbn).length() == 10;
    }

    /**
     * isISBN13
     * @param isbn a String
     * @return true if isbn is a valid ISBN-13, false otherwise
     */
    public static boolean isISBN13(String isbn) {
        return isISBN(isbn) && isbn.length() == 13;
    }

    /**
     * isISBN13
     * @param isbn a Long
     * @return true if isbn is a valid ISBN-13, false otherwise
     */
    public static boolean isISBN13(Long isbn) {
        return isISBN(String.valueOf(isbn)) && String.valueOf(isbn).length() == 13;
    }

    /**
     * @pre isbn is a valid isbn number
     * @param isbn a ISBN-13
     * @return the corresponding ISBN-10
     */
    @NonNull
    public static String convert13to10(String isbn){
        String isbn10 = isbn.substring(3,12);
        int checksum = 0;
        int weight = 10;

        for(Character c : isbn10.toCharArray())
        {
            checksum += Character.getNumericValue(c) * weight;
            weight--;
        }

        checksum = 11-(checksum % 11);
        if (checksum == 10)
            isbn10 += "X";
        else if (checksum == 11)
            isbn10 += "0";
        else
            isbn10 += checksum;

        return isbn10;
    }

    /**
     * @pre isbn is a valid isbn number
     * @param isbn a ISBN-13
     * @return the corresponding ISBN-10
     */
    @NonNull
    public static Long convert13to10(Long isbn){
        return Long.parseLong(convert13to10(String.valueOf(isbn)));
    }

    /**
     * @pre isbn is a valid isbn number
     * @param isbn a ISBN-10
     * @return the corresponding ISBN-13
     */
    @NonNull
    public static String convert10to13(String isbn){
        String isbn13  = isbn;
        isbn13 = "978" + isbn13.substring(0,9);
        //if (LOG_D) Log.d(TAG, "ISBN13 without sum" + ISBN13);
        int d;

        int sum = 0;
        for (int i = 0; i < isbn13.length(); i++) {
            d = ((i % 2 == 0) ? 1 : 3);
            sum += ((((int) isbn13.charAt(i)) - 48) * d);
            //if (LOG_D) Log.d(TAG, "adding " + ISBN13.charAt(i) + "x" + d + "=" + ((((int) ISBN13.charAt(i)) - 48) * d));
        }
        sum = 10 - (sum % 10);
        isbn13 += sum;

        return isbn13;
    }

    /**
     * @pre isbn is a valid isbn number
     * @param isbn a ISBN-10
     * @return the corresponding ISBN-13
     */
    @NonNull
    public static Long convert10to13(Long isbn){
        return Long.parseLong(convert10to13(String.valueOf(isbn)));
    }
}
