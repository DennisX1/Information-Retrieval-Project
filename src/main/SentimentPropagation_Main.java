package main;

import LinkAnalysis.HITS;
import LinkAnalysis.SentimentEvaluation;
import Preprocessing.Stemmer;
import Preprocessing.StopWordRemovalUtils;
import SimMeasuresUtils.TFIDFUtils;
import SimMeasuresUtils.WordEmbeddingsUtils;
import data.Review;
import data.ReviewGraph;
import io.UtilsJson;

/**
 * Class containing the main method for the project for Sentiment Propagation with Link Analysis.
 */
public class SentimentPropagation_Main {
    private static final int QUANTITY_REVIEWS= 10;
    private static final int PERCENTAGE_KNOWN_LABELS = 50;
    private static final double EPSILON = 0.000001;
    private static final int MAX_ITERATIONS = 30;
    private static final double INIT_LABEL = 0.4;



    public static void main(String[] args) {
        /*** get random dataset **/
        Review[] reviews = new Review[QUANTITY_REVIEWS];
        try {
            reviews = UtilsJson.getReviewsFromDataset(
                    QUANTITY_REVIEWS, PERCENTAGE_KNOWN_LABELS, UtilsJson.Dataset.AMAZON_INSTANT_VIDEO);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }


        if (reviews[QUANTITY_REVIEWS-1] == null ){
            System.exit(1);
        }


        /*** create Graph for nodes */
        ReviewGraph graph = new ReviewGraph(QUANTITY_REVIEWS);


        /*** Sim Measures */
        /*** stop word removal */
        Review cleaned [] = StopWordRemovalUtils.removeStopWords(reviews);
        /*** Stemming */
        Review stemmed [] = Stemmer.stemReviews(cleaned);
        /*** TFIDF with stop word removal + stemming */
        double [] [] tfIdfSims = TFIDFUtils.computeSimilarities(stemmed);
        /*** WordEmbeddings with stop word removal */
        double [] []  wordEmbeddingSims = WordEmbeddingsUtils.calculateSimWordEmbeddingsUtils(cleaned);

        graph.addALLReviewsRANDOM(reviews);

        // ReviewGraph graph = new ReviewGraph(reviews, double[][] similarities)

        //System.out.println(graph.toString());

        /*** run PageRank Algo */











        /*** run HITS ALgo */
        HITS    HITS_algo = new HITS(graph,  EPSILON, MAX_ITERATIONS, INIT_LABEL);
        HITS_algo.runHITS();
        double[] finalVec = HITS_algo.finalHITScores();

        SentimentEvaluation evaHITS = new SentimentEvaluation(reviews);
        evaHITS.createEvaluationSME();
        evaHITS.printSME("HITS \t");

    }
}
