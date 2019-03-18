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

    private static int LIMIT = 3;

    private static double[][] updateAuthMatrix;
    private static double[][] updateHubsMatrix;
    private static double[][] transposedGraph;

    private static void  testHITS(){
        //updateAuthMatrix = new double[LIMIT][LIMIT];
        //updateHubsMatrix  = new double [LIMIT][LIMIT];
        HITS algoHITS = new HITS(testGraph);
        algoHITS.runHITS(new HITS_Scores().ITERATIONS, new HITS_Scores().THRESHOLD);
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
