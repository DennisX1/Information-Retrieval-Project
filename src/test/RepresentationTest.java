package test;

import data.Review;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RepresentationTest {
    private static Review originalRev= new Review("I love this book so much. It is a good book to read.", 5.0, true);
    @Test
    public void testTFIDFRepresentations(){
        // TODO DENNIS

    }
    @Test
    public void testStopWordRemoval(){
        String noStopWordsText = "I love this book much It good book read";

        // TODO DENNIs apply stopword Removal
        String stopWordRemoved = "XY"; // TODO

        assertEquals(stopWordRemoved, noStopWordsText);

    }

    @Test
    public void testDenseRepresentations(){
        double [] denseRepresentation;



        //assertEquals(stopWordRemoved, noStopWordsText);
    }




}
