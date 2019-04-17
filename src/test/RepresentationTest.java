package test;

import Preprocessing.StopWordRemovalUtils;
import SimMeasuresUtils.WordEmbeedingsUtils;
import data.Review;
import org.junit.Test;
import static org.junit.Assert.*;


public class RepresentationTest {
    private static Review[] originalRev = new Review[3];

    @Test
    public void testTFIDFRepresentations() {
        originalRev[0] = new Review("I love this book so much. It is a good book to read.", 5.0, true);
        // TODO DENNIS

    }

    @Test
    public static void testStopWordRemoval() {

        originalRev[0] = new Review("I love this book so much. It is a good book to read.", 5.0, true);
        String noStopWordsText = "love book good book read";
        originalRev[1] = new Review("I love this cat it is so pretty. We all love this cat", 5.0, true);
        String noStopWordsTextOne = "love cat pretty love cat";
        originalRev[2] = new Review("Saturn is a gas planet with rings. It is a very large planet", 5.0, true);
        String noStopWordsTextTwo = "saturn gas planet rings large planet";

        Review[] review = StopWordRemovalUtils.removeStopWords(originalRev);

        assertEquals(noStopWordsText, review[0].getText());
        assertEquals(noStopWordsTextOne, review[1].getText());
        assertEquals(noStopWordsTextTwo, review[2].getText());

    }

    @Test
    public static void testDenseRepresentations() {

        Review[] orginalReview = new Review[2];
        orginalReview[0] = new Review("work story", 5.0, true);
        orginalReview[1] = new Review("police media", 5.0, true);


        Review[] cleaned = StopWordRemovalUtils.removeStopWords(orginalReview);

        double[][] denseRepresentation = WordEmbeedingsUtils.calculateSimWordEmbeedingsUtils(cleaned);


        assertEquals(1, denseRepresentation[0][0], 0);
        assertEquals(0.5179, denseRepresentation[0][1], 0);
        assertEquals(0.5179, denseRepresentation[1][0], 0);
        assertEquals(1.0, denseRepresentation[1][1], 0);

    }


    public static void main(String[] args) {
        testStopWordRemoval();
        testDenseRepresentations();
    }
}


