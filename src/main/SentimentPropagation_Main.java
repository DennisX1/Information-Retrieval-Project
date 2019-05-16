package main;

import LinkAnalysis.Evaluation;
import LinkAnalysis.HITS;
import LinkAnalysis.PageRank;
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
    private static final int QUANTITY_REVIEWS = 3000;
    //    private static final int PERCENTAGE_KNOWN_LABELS = 80;
    private static final double EPSILON = 0.000001;
    private static final int MAX_ITERATIONS = 30;
    private static final double INIT_LABEL = 0.4;


    public static void main(String[] args) {

            System.out.println("****Loading Reviews****");
            System.out.println("");
            /*** get random dataset **/
            Review[] reviews = new Review[QUANTITY_REVIEWS];
            try {
                reviews = UtilsJson.getReviewsFromDataset(
                        QUANTITY_REVIEWS, 5, UtilsJson.Dataset.AMAZON_INSTANT_VIDEO);

            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }


            if (reviews[QUANTITY_REVIEWS - 1] == null) {
                System.exit(1);
            }


            /*** create Graph for nodes */
            System.out.println("****Reviews loaded****");

            /*** Sim Measures */
            /*** stop word removal */

            System.out.println("****Removing Stopwords****");
            System.out.println("");

            Review cleaned[] = StopWordRemovalUtils.removeStopWords(reviews);
            System.out.println("****Stopwords Removed****");
            System.out.println("");

            /*** Stemming */
            System.out.println("****Stemming****");
            System.out.println("");
            Review stemmed[] = Stemmer.stemReviews(cleaned);
            System.out.println("****Stemming finished");
            System.out.println("");
            /*** TFIDF with stop word removal + stemming */
            System.out.println("****Computing TF-IDF values****");
            System.out.println("");

            double[][] tfIdfSims = TFIDFUtils.computeSimilarities(stemmed);
            System.out.println("****Finished TF-IDF computing****");
            System.out.println("");

            /*** WordEmbeddings with stop word removal */
            System.out.println("****Computing Word embeddings****");
            System.out.println("");
            double[][] wordEmbeddingSims = WordEmbeddingsUtils.calculateSimWordEmbeddingsUtils(cleaned);
            System.out.println("****Finished WordEmbedding computing****");



            for (int percentageKnownLabels = 0; percentageKnownLabels <= 99; percentageKnownLabels += 5) {

            /************************* run with tf-idf ******************/

            ReviewGraph graph = new ReviewGraph(reviews, tfIdfSims);
            // System.out.println(graph.toString());

            /*** run PageRank Algo */


            /*** run HITS ALgo */
            System.out.println("****Run HITS****");
            System.out.println("");
            HITS HITS_algo = new HITS(graph, EPSILON, MAX_ITERATIONS, INIT_LABEL, true);
            HITS_algo.runHITS();
            double[] finalVec = HITS_algo.finalHITScores();

            System.out.println("\nMSE HITS");
            Evaluation.calcAndPrintMSE(reviews);
            System.out.println("Finished HITS");



            System.out.println("Combination: TF-IDF/HITS           Known Labels: " + percentageKnownLabels + "%" +
                    "   MSE: ..." + "  Pearson correlation:");
            System.out.println("Combination: Embedding/HITS        Known Labels: " + percentageKnownLabels + "%" +
                    "   MSE: ..." + "  Pearson correlation:");
            System.out.println("Combination: TF-IDF/PageRank       Known Labels: " + percentageKnownLabels + "%" +
                    "   MSE: ..." + "  Pearson correlation:");
            System.out.println("Combination: Embedding/PageRank    Known Labels: " + percentageKnownLabels + "%" +
                    "   MSE: ..." + "  Pearson correlation:");
            System.out.println();


        }
        /************************* RUN WITH TF-IDF ******************/

        //double [] []  wordEmbeddingSims = WordEmbeedingsUtils.calculateSimWordEmbeedingsUtils(cleaned);
        //graph = new ReviewGraph(reviews, wordEmbeddingSims);
        //System.out.println(graph.toString());
    }
}
