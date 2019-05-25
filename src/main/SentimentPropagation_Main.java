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
    private static final double EPSILON = 0.000001;
    private static final int MAX_ITERATIONS = 30;
    private static final double INIT_LABEL = 0.2;


    public static void main(String[] args) throws Exception {

        System.out.println("****Loading Reviews****");
        /*** get random dataset **/
        Review[] reviews = new Review[QUANTITY_REVIEWS];
        try {
            reviews = UtilsJson.getReviewsFromDataset(
                    QUANTITY_REVIEWS, 0, UtilsJson.Dataset.AMAZON_INSTANT_VIDEO);
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

        Review cleaned[] = StopWordRemovalUtils.removeStopWords(reviews);

        System.out.println("****Stopwords Removed****");

        /*** WordEmbeddings with stop word removal */
        System.out.println("****Computing Word embeddings****");
        double[][] wordEmbeddingSims = WordEmbeddingsUtils.calculateSimWordEmbeddingsUtils(cleaned);
        System.out.println("****Finished WordEmbedding computing****");

        /*** Stemming */
        System.out.println("****Stemming****");
        Review stemmed[] = Stemmer.stemReviews(cleaned);
        System.out.println("****Stemming finished****");


        /*** TFIDF with stop word removal + stemming */
        System.out.println("****Computing TF-IDF values****");
        double[][] tfIdfSims = TFIDFUtils.computeSimilarities(stemmed);
        System.out.println("****Finished TF-IDF computing****");

        stemmed = null;
        cleaned = null;

        double[] maxMinTFID = getMinMaxMatrix(tfIdfSims);
        double[] maxMinEmbeddings = getMinMaxMatrix(wordEmbeddingSims);

        //Thresholding


        System.out.println("****Performing Evaluation****");
        for (int percentageKnownLabels = 5; percentageKnownLabels <= 95; percentageKnownLabels += 5) {

            Review.addKnownPercentage(5, reviews);
            //************************* RUN WITH TF-IDF  ******************/
            //   System.out.println("~~~~~~~RUN WITH TF_IDF~~~~~~~");
            ReviewGraph graph = new ReviewGraph(reviews, tfIdfSims);
            //System.out.println(graph.toString());


            /*** run HITS ALgo */
            System.out.println("");
            HITS HITS_algo = new HITS(graph, EPSILON, MAX_ITERATIONS, INIT_LABEL);
            HITS_algo.runHITS();

            System.out.println("Combination: TF-IDF/HITS           Known Labels: " + percentageKnownLabels + "%" +
                    "      MAE: " + Evaluation.calculateMAE(reviews) +
                    "      MSE: " + Evaluation.calculateMSE(reviews) +
                    "      PCC: " + Evaluation.calculatePCC(reviews));
            System.out.println("CombinationV2: TF-IDF/HITS           Known Labels: " + percentageKnownLabels + "%" +
                    "      MAE: " + Evaluation.calculateMAE_standard(reviews) +
                    "      MSE: " + Evaluation.calculateMSE_standard(reviews) +
                    "      PCC: " + Evaluation.calculatePCC_standard(reviews));
            ///*** run PageRank Algo with TF-IDF */

            PageRank.performCalculations(reviews, tfIdfSims);
            System.out.println("Combination: TF-IDF/PageRank       Known Labels: " + percentageKnownLabels + "%" +
                    "      MAE: " + Evaluation.calculateMAE(reviews) +
                    "      MSE: " + Evaluation.calculateMSE(reviews) +
                    "      PCC: " + Evaluation.calculatePCC(reviews));

            //************************* RUN WITH EMBEDDINGS ******************/
            //  System.out.println("\n~~~~~~~RUN WITH EMBEDDINGS~~~~~~~");

            graph = new ReviewGraph(reviews, wordEmbeddingSims);
            //System.out.println(graph.toString());

            HITS_algo = new HITS(graph, EPSILON, MAX_ITERATIONS, INIT_LABEL);
            HITS_algo.runHITS();


            System.out.println("Combination: Embedding/HITS        Known Labels: " + percentageKnownLabels + "%" +
                    "      MAE: " + Evaluation.calculateMAE(reviews) +
                    "      MSE: " + Evaluation.calculateMSE(reviews) +
                    "      PCC: " + Evaluation.calculatePCC(reviews));
            System.out.println("CombinationV2: TF-IDF/HITS           Known Labels: " + percentageKnownLabels + "%" +
                    "      MAE: " + Evaluation.calculateMAE_standard(reviews) +
                    "      MSE: " + Evaluation.calculateMSE_standard(reviews) +
                    "      PCC: " + Evaluation.calculatePCC_standard(reviews));

            //*** Page Rank Embedding*////

            PageRank.performCalculations(reviews, wordEmbeddingSims);


            System.out.println("Combination: Embedding/PageRank    Known Labels: " + percentageKnownLabels + "%" +
                    "      MAE: " + Evaluation.calculateMAE(reviews) +
                    "      MSE: " + Evaluation.calculateMSE(reviews) +
                    "      PCC: " + Evaluation.calculatePCC(reviews));


        }
    }

    private static void getStatistics(Review[] reviews) {
        int counter1 = 0;
        int counter2 = 0;
        int counter3= 0;
        int counter4 = 0;
        int counter5 =0;
        int counterKomisch =0;

        for (Review r: reviews) {
            if (r.getRealRating() ==1.0){
                counter1++;}
            else if (r.getRealRating() ==2.0) {
                counter2++;
            }
            else if (r.getRealRating() ==3.0) {
                counter3++;
            }
            else if (r.getRealRating() ==4.0) {
                counter4++;
            }
            else if (r.getRealRating() ==5.0) {
                counter5++;
            }
            else {
                counterKomisch++;}
        }
    }
    private static double[] getMinMaxMatrix(double[][] matrix) {
        double[] minmax = new double[2];
        double min =2;
        double  max =-1;
        for (int i = 0; i <  matrix.length; i++) {
            for (int j = i+1; j <matrix[i].length ; j++) {
                //min
                if(matrix[i][j]<min && i != j && matrix[i][j]> 0.0){
                    min =matrix[i][j];
                }
                if (matrix[i][j]>max && i != j && matrix[i][j]< 1.0){
                    max =matrix[i][j];
                }
            }
        }
        minmax[0] =min;
        minmax[1] = max;
        return  minmax;
    }
}
