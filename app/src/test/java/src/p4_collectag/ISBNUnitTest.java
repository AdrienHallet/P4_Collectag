package src.p4_collectag;

import org.junit.Test;

import src.commons.ISBN;

import static org.junit.Assert.assertEquals;

/**
 * Created by Adrien on 17/02/2017.
 * Unit tests for ISBN helper class
 */

public class ISBNUnitTest {
    @Test
    public void isISBN_isCorrect() throws Exception {
        assertEquals(ISBN.isISBN("0127450408"), true);
        assertEquals(ISBN.isISBN("9780127450407"), true);
        assertEquals(ISBN.isISBN("97801274504079"), false);
        assertEquals(ISBN.isISBN("0"), false);

        assertEquals(ISBN.isISBN10(9780127450407L),false);
        assertEquals(ISBN.isISBN13(9780127450407L),true);
    }
    @Test
    public void convert_isCorrect() throws Exception{
        assertEquals(ISBN.isISBN10("0127450408"),ISBN.isISBN13(ISBN.convert10to13("0127450408"))); //true
        assertEquals(ISBN.isISBN13("9780127450407"),ISBN.isISBN10(ISBN.convert13to10("9780127450407"))); //true
    }
}
