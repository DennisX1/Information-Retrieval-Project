package test;

import LinkAnalysis.HITS;
import data.Review;
import data.ReviewGraph;
import io.MatrixUtils;
import io.UtilsJson;

import java.io.IOException;

import static io.UtilsJson.readJSONLimit;
import static org.junit.Assert.assertEquals;

public class HITSTest {
    private static int QUANTITY_REVIEWS = 3;
    private static final double EPSILON = 0.000001;
    private static int MAX_ITERATIONS = 1;
    private static final double INIT_LABEL = 0.4;

    private static ReviewGraph testGraph;


    private static void testHITS() {
        HITS algoHITS = new HITS(testGraph, EPSILON, MAX_ITERATIONS, INIT_LABEL);

        algoHITS.runHITSV2();
        double[] predictions = algoHITS.finalHITScores();
        MatrixUtils.printVectorDouble(predictions);

        assertEquals(0.2, predictions[0], 0.01);
        assertEquals(1.0, predictions[1], 0.01);
        assertEquals(0.186, predictions[2], 0.01);

    }

    private static void testRTHITS() {
        QUANTITY_REVIEWS = 1000;


        HITS algoHITS = new HITS(testGraph, EPSILON, MAX_ITERATIONS, INIT_LABEL);

        final long timeStart = System.currentTimeMillis();
        algoHITS.runHITSV2();
        final long timeEnd = System.currentTimeMillis();
        System.out.println("RT : " + (timeEnd - timeStart) + " Seconds");


    }


    public static void main(String[] args) {
        Review[] reviews = new Review[QUANTITY_REVIEWS];
        reviews[0] = new Review("a", 1.0, true);
        reviews[1] = new Review("ab", 5.0, true);
        reviews[2] = new Review("abc", 4.0, false);

        double[][] testSimilarities = {{1.0, 0.3, 0.7},
                {0.1, 1.0, 0.8},
                {0.4, 0.6, 1.0}};

        testGraph = new ReviewGraph(reviews, testSimilarities);


        testHITS();
        //testRTHITS();
    }

}

