package test;

import LinkAnalysis.Evaluation;
import LinkAnalysis.HITS;
import data.Review;
import data.ReviewGraph;
import io.MatrixUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class HITSTest {
    private static int QUANTITY_REVIEWS = 3;
    private static final double EPSILON = 0.000001;
    private static int MAX_ITERATIONS = 2;
    private static final double INIT_LABEL = 0.4;

    private static ReviewGraph testGraph;

    @Test
    public void testHITSAdjacency() {
        HITS algoHITS = new HITS(testGraph, EPSILON, MAX_ITERATIONS, INIT_LABEL);


        //AdjacencyMatrix if we use A
        double[][] adjMatrix = algoHITS.getAdjacencyMatrix();
        assertEquals(0., adjMatrix[0][0], 0.01);
        assertEquals(0.3, adjMatrix[0][1], 0.01);
        assertEquals(0.7, adjMatrix[0][2], 0.01);
        assertEquals(0.1, adjMatrix[1][0], 0.01);
        assertEquals(0, adjMatrix[1][1], 0.01);
        assertEquals(0.8, adjMatrix[1][2], 0.01);
        assertEquals(0.4, adjMatrix[2][0], 0.01);
        assertEquals(0.6, adjMatrix[2][1], 0.01);
        assertEquals(0, adjMatrix[2][2], 0.01);
    }
    @Test
    public void testHITS() {
        HITS algoHITS = new HITS(testGraph, EPSILON, MAX_ITERATIONS, INIT_LABEL);

        algoHITS.runHITS();
        double[] predictions = algoHITS.finalHITScores();
        //MatrixUtils.printVectorDouble(predictions);

        assertEquals(1.0, predictions[0], 0.01); // should be the real label
        assertEquals(5.0, predictions[1], 0.01); // should be the real label
        assertEquals(2.07, predictions[2], 0.01); // prediction - denormalized

        System.out.println("\nMSE HITS");
        Evaluation.calcAndPrintMSE(algoHITS.getReviews());

    }

    /*@Test
    public void testHITSZNormalise() {
        HITS algoHITS = new HITS(testGraph, EPSILON, MAX_ITERATIONS, INIT_LABEL);


        algoHITS.runHITS();
        double[] predictions = algoHITS.finalHITScores();
        //MatrixUtils.printVectorDouble(predictions);

        assertEquals(1.0, predictions[0], 0.01);
        assertEquals(5.0, predictions[1], 0.01);
        assertEquals(1.0, predictions[2], 0.01);
    }*/

    @Test
    public void testRTHITS() {
        QUANTITY_REVIEWS = 1000;
        HITS algoHITS = new HITS(testGraph, EPSILON, MAX_ITERATIONS, INIT_LABEL);
        final long timeStart = System.currentTimeMillis();
        algoHITS.runHITS();
        final long timeEnd = System.currentTimeMillis();
        System.out.println("RT : " + (timeEnd - timeStart) + " Seconds");


    }

    @BeforeClass
    public static void init() {
        //______________________PREPARATIONS__________
        Review[] reviews = new Review[QUANTITY_REVIEWS];
        reviews[0] = new Review("a", 1.0, true);
        reviews[0].setEvalReview(false);
        reviews[1] = new Review("ab", 5.0, true);
        reviews[1].setEvalReview(false);
        reviews[2] = new Review("abc", 4.0, false);
        reviews[2].setEvalReview(true);

        double[][] testSimilarities = {{1.0, 0.3, 0.7},
                {0.1, 1.0, 0.8},
                {0.4, 0.6, 1.0}};

        testGraph = new ReviewGraph(reviews, testSimilarities);
    }

}

