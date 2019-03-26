package main;

import LinkAnalysis.HITS;
import LinkAnalysis.SentimentEvaluation;
import data.Review;
import data.ReviewGraph;
import io.UtilsJson;

public class SentimentPropagration_Main {
    private static final int QUANTITY_REVIEWS= 5;
    private static final int PERCENTAGE_KOWNLABELS = 50;
    private static final int ITERATIONS= 10;
    private static final double EXCLUSION_THRESHOLD = 0.000002;

    public static void main(String[] args) {
        /*** get random dataset **/
        Review[] reviews = new Review[QUANTITY_REVIEWS];
        try {
            reviews = UtilsJson.getReviewsFromDataset(
                    QUANTITY_REVIEWS, PERCENTAGE_KOWNLABELS, UtilsJson.Dataset.AMAZON_INSTANT_VIDEO);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }


        if (reviews[QUANTITY_REVIEWS-1] == null ){
            System.exit(1);
        }


        /*** create Graph for nodes */
        ReviewGraph graph = new ReviewGraph(QUANTITY_REVIEWS);
        // TODO add SIM Measure here
        graph.addALLReviewsRANDOM(reviews);
        //graph.addALLReviewsRANDOM(reviews, EXCLUSION_THRESHOLD);
        System.out.println(graph.toString());

        /*** run PageRank Algo */











        /*** run HITS ALgo */
        HITS    HITS_algo = new HITS(graph, reviews);
        HITS_algo.runHITS(ITERATIONS, EXCLUSION_THRESHOLD);
        HITS_algo.propagateSentiment();

        SentimentEvaluation evaHITS = new SentimentEvaluation(reviews);
        evaHITS.createEvaluationSME();
        evaHITS.printSME("HITS \t");

    }
}
