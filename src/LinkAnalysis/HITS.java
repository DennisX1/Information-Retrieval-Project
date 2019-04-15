package LinkAnalysis;

import data.Review;
import data.ReviewGraph;
import io.MatrixUtils;

import java.util.*;

/**
 * Class to represent the HITS algorithm. It is a slight variation to the original implementation.
 * Assumptions: HubScore = AuthScore  => therefore, we introduce HITS_Score as the only score
 * Update Matrix L*L^T = L^T*L
 * Updating rule: score^k = L * score^(k-1)
 * Update Matrix L <=> weighted Graph of reviews
 */
public class HITS {
    private double[][] weightedGraph;
    private Review[] reviews;
    private int quantityReviews;
    private double[][] updateMatrix;
    private Map<Integer, HITS_Score> scoreCollection;
     private boolean converged;
    public static final double EPSILON = 0.000001;
    public static final int MAX_ITERATIONS = 30;


    /**
     * Constructor of the HITS Algorithm.
     *
     * @param graph   Review[] - Array of reviews that should be included
     * @param reviews []
     */
    public HITS(ReviewGraph graph, Review[] reviews) {
        this.reviews = reviews;
        quantityReviews = reviews.length;
        weightedGraph = graph.getGraph(); //shallow Copy
        scoreCollection = new HashMap<>();
        converged = false;
        updateMatrix = weightedGraph;
    }

    /**
     * Method the trigger the HITS Algorithm.
     *
     *
     */
    public void runHITSV2() {

        //for all nodes with unknown labels init
        for (int j = 0; j < quantityReviews; j++) { // for each node get score
            if ( !reviews[j].isKnown()){ // node with unknown label
                putScoreToCollection(reviews[j].getId(), 0.4);
            }else {
                putScoreToCollection(reviews[j].getId(), reviews[j].getNormalizedRating());
            }
        }
        startPropagation();

    }
    private void startPropagation(){
        double[] updatedScores = new double[quantityReviews];
        int iterations =1;
        while (!converged && iterations < MAX_ITERATIONS ) {
            System.out.println("Iteration: "+ iterations);

            double[] oldScoresVec = new double[quantityReviews];
            for (int j = 0; j < quantityReviews; j++) {                 // for each node get score
                HITS_Score otherNodes = getScoreFromCollection(reviews[j].getId());
                oldScoresVec[j] = otherNodes.getScore();
            }
            MatrixUtils.printVectorDouble(oldScoresVec);
            //Multiply and save in HitScores
            updatedScores = MatrixUtils.multiplyMatrixVectorWeighted(updateMatrix, oldScoresVec, weightedGraph);

            // normalize
            updatedScores = normalizeScores(updatedScores);
            MatrixUtils.printVectorDouble(updatedScores);
            // Update
            updateScoresAllNodes(updatedScores);
            iterations++;
        }

        writePredictions(updatedScores);

        System.out.println("Final HIT Scores:");
        double[] oldScoresVec = new double[quantityReviews];
        for (int j = 0; j < quantityReviews; j++) {                 // for each node get score
            HITS_Score otherNodes = getScoreFromCollection(reviews[j].getId());
            oldScoresVec[j] = otherNodes.getScore();
        }
        MatrixUtils.printVectorDouble(oldScoresVec);


    }

    private void writePredictions(double[] updatedScores) {
        for (int j = 0; j < quantityReviews; j++) { // for each node get score
            if (!reviews[j].isKnown()){
                reviews[j].setPredictedRating(updatedScores[j]);
            }
        }
    }

    /**
     * Method to normalize the calculated score.
     *
     * @param scoreVec double[] array containing the calculated scores
     * @return normalized double[] array with scores
     */
    private double[] normalizeScores(double[] scoreVec) {
        System.out.println("Before normalization");
        MatrixUtils.printVectorDouble(scoreVec);
        double sumScores = 0.0;
        for (int i = 0; i < scoreVec.length; i++) {
            sumScores += scoreVec[i];
        }
        // TODO what to do if the sum is zero?
        if (sumScores == 0.0) {
            System.out.println("oh oh this is no good...");
        }
        if (sumScores > 0.0) {
            for (int i = 0; i < scoreVec.length; i++) {
                scoreVec[i] = scoreVec[i] / sumScores;
            }
        }
        return scoreVec;
    }

    /**
     * Method to update the score for each node.
     *
     * @param scoreVecHITS double[] normalized, updated scores for all nodes
     */
    private void updateScoresAllNodes(double[] scoreVecHITS) {
        double score;
        for (int i = 0; i < scoreVecHITS.length; i++) {
            if (!reviews[i].isKnown()){
                HITS_Score cur = getScoreFromCollection(reviews[i].getId());
                score = scoreVecHITS[i];
                // convergence
                if( Math.abs(cur.getScore()-score) < EPSILON){
                    converged = true;
                }
                cur.updateScores(score);
            }
        }
    }

    /**
     * Add new Score to scoreCollection -only for Nodes with an unknown label
     * @return store in collection successful
     */
    private boolean putScoreToCollection(int k, double rating) {
        HITS_Score node = scoreCollection.get(k);
        if (node == null) { // not yet in collection
                node = new HITS_Score(rating);
                scoreCollection.put(k, node);
                return true;
        }
        return false;
    }

    /**
     * Method to obtain the HIT_Score object for a given reviewID.
     *
     * @param k int- ID of review
     * @return HITS_Score - Score object belonging to the given reviewID
     */
    private HITS_Score getScoreFromCollection(int k) {
        HITS_Score node = scoreCollection.get(k);
        if (node == null) { // not yet in collection
            node = new HITS_Score();
            scoreCollection.put(k, node);
        }
        return node;
    }


    private double[][] createUpdateMatrix(double[][] matrix1, double[][] matrix2) {
        return MatrixUtils.matrixMultiplicationSameSize(matrix1, matrix2);
    }


    private static <K, V> void printMap(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey()
                    + " Value : " + entry.getValue());
        }
    }



    /**
     * Method to find the position of a review within the matrix for a given ID.
     *
     * @param ID int - reviewID
     * @return int - position of review in matrix
     */
    public int findPositionOfReview(int ID) {
        for (int i = 0; i < quantityReviews; i++) {
            if (reviews[i].getId() == ID) {
                return i;
            }
        }
        return -1;
    }
}
