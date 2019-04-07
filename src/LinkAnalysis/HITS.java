package LinkAnalysis;

import data.Review;
import data.ReviewGraph;
import io.MatrixUtils;

import javax.swing.*;
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
    private int topK = 200;
    private int[] topKReviewIDs;
    private boolean converged;
    public static final double EPSILON = 0.0001;
    public static final int MAX_ITERATIONS = 30;


    /**
     * Constructor of the HITS Algorithmm.
     *
     * @param graph   Review[] - Array of reviews that should be included
     * @param reviews []
     */
    public HITS(ReviewGraph graph, Review[] reviews) {
        System.out.println("CHECK if always symmetric and than reduce calculations and sizes");
        this.reviews = reviews;
        quantityReviews = reviews.length;
        topKReviewIDs = new int[topK];
        weightedGraph = graph.getGraph();
        scoreCollection = new HashMap<>();
        converged = false;
        /** Regular approach  ***/
        /*
        double[][] transposedGraph = MatrixUtils.transposeMatrix(graph.getGraph());
        //MatrixUtils.printMatrixDouble(graph.getGraph(), "Initial Graph");
        //MatrixUtils.printMatrixDouble(transposedGraph, "Transposed Graph");

        updateMatrix = createUpdateMatrix(graph.getGraph(), transposedGraph);
        //MatrixUtils.printMatrixDouble(updateMatrix, "L^T*L");
        updateHubMatrix = createUpdateMatrix(transposedGraph, graph.getGraph());
        //MatrixUtils.printMatrixDouble(updateHubMatrix, "L*L^T");
        //updateHubMatrix = MatrixUtils.transposeMatrix(updateMatrix);
        //MatrixUtils.printMatrixDouble(updateHubMatrix, "L*L^T V2");*/

        /******** making use of undirected arcs **********/
        updateMatrix = weightedGraph;
    }

    /**
     * Method the trigger the HITS Algorithm.
     *
     * @param iterations int how many iterations should be considered
     */
    public void runHITS(int iterations) {
        double[] updatedScores = new double[quantityReviews];

        for (int i = 0; i < iterations; i++) {
            double[] oldScoresVec = new double[quantityReviews];

            for (int j = 0; j < quantityReviews; j++) { // for each node get score
                HITS_Score otherNodes = getScoreFromCollection(reviews[j].getId());
                oldScoresVec[j] = otherNodes.getScore();
            }

            //Multiply and save in HitScores
            updatedScores = MatrixUtils.multiplyMatrixVectorWeighted(updateMatrix, oldScoresVec, weightedGraph);

            // normalize
            updatedScores = normalizeScores(updatedScores);

            // Update
            updateScoresAllNodes(updatedScores);
        }

        System.out.println("Final Hit Scores V1:");
        MatrixUtils.printVectorDouble(updatedScores);
        getTopKReviews();
        propagateSentiment();
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
                putScoreToCollection(reviews[j].getId(), -1.0);
            }else {
                putScoreToCollection(reviews[j].getId(), reviews[j].getRealRating());
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
            for (int j = 0; j < quantityReviews; j++) { // for each node get score
                HITS_Score otherNodes = getScoreFromCollection(reviews[j].getId());
                oldScoresVec[j] = otherNodes.getScore();
            }
            MatrixUtils.printVectorDouble(oldScoresVec);
            //Multiply and save in HitScores
            updatedScores = MatrixUtils.multiplyMatrixVectorWeighted(updateMatrix, oldScoresVec, weightedGraph);

            // normalize
            //updatedScores = normalizeScores(updatedScores);
            MatrixUtils.printVectorDouble(updatedScores);
            // Update
            updateScoresAllNodes(updatedScores);
            iterations++;
        }

        writePredictions(updatedScores);

        System.out.println("Final HIT Scores:");
        MatrixUtils.printVectorDouble(updatedScores);


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
            if (rating == -1.0) {
                node = new HITS_Score();
                scoreCollection.put(k, node);
                return true;
            } else {
                node = new HITS_Score(rating);
                scoreCollection.put(k, node);
                return true;
            }
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

    public void propagateSentiment() {
        for (int i = 0; i < quantityReviews; i++) {
            if (!reviews[i].isKnown()) {
                calculateSentiment(reviews[i], i);
            }

        }
    }

    private void getTopKReviews() {
        //printMap(scoreCollection);
        List<Map.Entry<Integer, HITS_Score>> list = new ArrayList<>(scoreCollection.entrySet());

        /*Collections.sort(list, new Comparator<Map.Entry<Integer, HITS_Scores>>() {
            public int compare(Map.Entry< Integer, HITS_Scores> o1,
                               Map.Entry< Integer, HITS_Scores> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });*/
        Collections.sort(list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        int k = 0;
        for (Map.Entry<Integer, HITS_Score> entry : list) {
            if (k < topK) {
                if (reviews[findPositionOfReview(entry.getKey())].isKnown()) {
                    continue;
                }
                topKReviewIDs[k] = entry.getKey();
                k++;

            } else {
                break;
            }
        }
    }


    private static <K, V> void printMap(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey()
                    + " Value : " + entry.getValue());
        }
    }

    private void calculateSentiment(Review rev, int positionGraph) {
        double sentiment = 0.0;
        //idea = weight * label over all top K nodes avg by number of nodes
        // SIGMA sim measure * label) /k
        // for which review?
        int row = positionGraph;
        int column;
        for (int i = 0; i < topK; i++) {
            column = findPositionOfReview(topKReviewIDs[i]);
            // it this ith  topK Review Label know? add it up
            if ((reviews[column]).isKnown()) {
                sentiment += weightedGraph[row][column] * reviews[i].getRealRating();
            }
        }
        rev.setPredictedRating(sentiment);
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
