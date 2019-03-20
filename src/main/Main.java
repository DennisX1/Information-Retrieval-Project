package main;

import LinkAnalysis.HITS;
import LinkAnalysis.HITS_Scores;
import data.Review;
import data.ReviewGraph;

import java.io.IOException;

import static io.UtilsJson.readJSONLimit;

public class Main {
    private static ReviewGraph testGraph;
    private static Review[] reviews;

    public static int getLIMIT() {
        return LIMIT;
    }

    private static int LIMIT = 2000;

    private static double[][] updateAuthMatrix;
    private static double[][] updateHubsMatrix;
    private static double[][] transposedGraph;

    private static void  testHITS(){
        HITS algoHITS = new HITS(testGraph);
        // TODO test which one works correct

        final long timeStart = System.currentTimeMillis();
        //algoHITS.runHITS(new HITS_Scores().ITERATIONS, new HITS_Scores().THRESHOLD);
        algoHITS.runHITS(20, new HITS_Scores().THRESHOLD);
        final long timeEnd = System.currentTimeMillis();
        System.out.println("RT : " + (timeEnd - timeStart) + " Seconds");

    }


    private static void testReviewGraph(){

        reviews = new Review[LIMIT];
        try {
            reviews = readJSONLimit(LIMIT);
            System.out.println(reviews.length);

        } catch (IOException e) {
            e.printStackTrace();
        }

        testGraph = new ReviewGraph(LIMIT);
        testGraph.addALLReviewsRANDOM(reviews);


        //System.out.println(testGraph.toString());


    }

    public static void main(String[] args) {
        testReviewGraph();
        testHITS();


    }

}
