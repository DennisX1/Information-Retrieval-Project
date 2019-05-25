package test;

import LinkAnalysis.Evaluation;
import LinkAnalysis.HITS;
import Preprocessing.Stemmer;
import Preprocessing.StopWordRemovalUtils;
import SimMeasuresUtils.TFIDFUtils;
import SimMeasuresUtils.WordEmbeddingsUtils;
import data.Review;
import data.ReviewGraph;
import io.MatrixUtils;
import io.UtilsJson;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class HITSTest_V2 {
    private static final int QUANTITY_REVIEWS = 4;
    private static final double EPSILON = 0.000001;
    private static final int MAX_ITERATIONS = 30;
    private static final double INIT_LABEL = 0.2;

    @Test
    public void hitsTestWithSims() throws Exception {

        Review[] reviews = new Review[QUANTITY_REVIEWS];
        try {
            reviews = UtilsJson.getReviewsFromDataset(
                    QUANTITY_REVIEWS, 75, UtilsJson.Dataset.AMAZON_INSTANT_VIDEO);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        Assert.assertNotNull(reviews[QUANTITY_REVIEWS-1]);


        Review cleaned[] = StopWordRemovalUtils.removeStopWords(reviews);


        double[][] wordEmbeddingSims = WordEmbeddingsUtils.calculateSimWordEmbeddingsUtils(cleaned);


        Review stemmed[] = Stemmer.stemReviews(cleaned);

        double[][] tfIdfSims = TFIDFUtils.computeSimilarities(stemmed);

        double[] maxMinTFID = getMinMaxMatrix(tfIdfSims);
        double[] maxMinEmbeddings = getMinMaxMatrix(wordEmbeddingSims);



        Review.addKnownPercentage(5, reviews);
        ReviewGraph graph = new ReviewGraph(reviews, tfIdfSims);


        double[][] A = {{1,2,3,4},{5,6,7,8},{9,1,2,3},{4,5,6,7}};
                double[] B = {8,9,1,2,};
        double [] res = MatrixUtils.multiplyMatrixVector(A, B);
        assertEquals(37, res[0], 0.01);
        assertEquals(117, res[1], 0.01);
        assertEquals(89, res[2], 0.01);
        assertEquals(97, res[3], 0.01);


        System.out.println("");
        HITS HITS_algo = new HITS(graph, EPSILON, MAX_ITERATIONS, INIT_LABEL);
        HITS_algo.runHITS();

        System.out.println("Combination: TF-IDF/HITS           Known Labels: " + 5 + "%" +
                "      MAE: " + Evaluation.calculateMAE(reviews) +
                "      MSE: " + Evaluation.calculateMSE(reviews) +
                "      PCC: " + Evaluation.calculatePCC(reviews));
    }
    private static double[] getMinMaxMatrix(double[][] matrix) {
        double[] minmax = new double[3];
        double min =2;
        double  max =-1;
        int counterZero =0;
        for (int i = 0; i <  matrix.length; i++) {
            for (int j = 0; j <matrix[i].length ; j++) {
                //min
                if (matrix[i][j] ==0.0){
                    counterZero++;
                }
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
        minmax[2] = counterZero;
        return  minmax;
    }
}
