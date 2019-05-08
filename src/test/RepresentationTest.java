package test;

import Preprocessing.Stemmer;
import Preprocessing.StopWordRemovalUtils;
import SimMeasuresUtils.TFIDFUtils;
import SimMeasuresUtils.WordEmbeedingsUtils;
import data.Review;
import org.junit.Test;

import static org.junit.Assert.*;


public class RepresentationTest {
    private static Review[] originalRev = new Review[3];

    @Test
    public static void testTFIDFRepresentations() {
        Review[] orginalReview = new Review[3];
        orginalReview[0] = new Review("Saturn is the gas planet with rings.", 5.0, true);
        orginalReview[1] = new Review("Jupiter is the largest gas planet.", 5.0, true);
        orginalReview[2] = new Review("Saturn is the Roman god of sowing", 5.0, true);

        Review[] cleaned = StopWordRemovalUtils.removeStopWords(orginalReview);
        Review[] stemmed = Stemmer.stemReviews(cleaned);

        double[][] cosine = TFIDFUtils.computeSimilarities(stemmed);

        assertEquals(0.1523, cosine[0][1], 0);
        assertEquals(0.1523, cosine[1][0], 0);
        assertEquals(0.0648, cosine[0][2], 0);
        assertEquals(0.0648, cosine[2][0], 0);
        assertEquals(0, cosine[1][2], 0);
        assertEquals(0, cosine[2][1], 0);
        assertEquals(1, cosine[0][0], 0);
        assertEquals(1, cosine[1][1], 0);
        assertEquals(1, cosine[2][2], 0);

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

        Review[] originalReview = new Review[2];
        originalReview[0] = new Review("work story", 5.0, true);
        originalReview[1] = new Review("police media", 5.0, true);


        Review[] cleaned = StopWordRemovalUtils.removeStopWords(originalReview);

        double[][] denseRepresentation = WordEmbeedingsUtils.calculateSimWordEmbeedingsUtils(cleaned);


        assertEquals(1.0, denseRepresentation[0][0], 0);
        assertEquals(0.5179, denseRepresentation[0][1], 0);
        assertEquals(0.5179, denseRepresentation[1][0], 0);
        assertEquals(1.0, denseRepresentation[1][1], 0);

    }


    public static void main(String[] args) {
        testStopWordRemoval();
        testDenseRepresentations();
        testTFIDFRepresentations();
    }
}


