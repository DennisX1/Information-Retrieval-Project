package LinkAnalysis;

import data.Review;
import data.ReviewGraph;
import io.MatrixUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * Class to represent the HITS algorithm. It is a slight variation to the original implementation.
 * Assumptions: HubScore = AuthScore  => therefore, we introduce HITS_Score as the only score
 * Update Matrix L*L^T = L^T*L
 * Updating rule: score^k = L * score^(k-1)
 * Update Matrix L <=> weighted Graph of reviews
 */
public class HITS {
     //private ReviewGraph similarityGraph;
    private Review[] reviews;
    private int quantityReviews;
    private double[][] adjacencyMatrix;
    private boolean converged;
    //private boolean useZNormalization;
    private static double EPSILON;
    private static int MAX_ITERATIONS;
    private static double INIT_LABEL;

    /**
     * Constructor of the HITS Algorithm.
     *
     * @param graph Review[] - Array of reviews that should be included
     */
    public HITS(ReviewGraph graph, double delta, int iterationLimit, double initLabel){//, boolean zNormalization) {
        //similarityGraph = graph;
        quantityReviews = graph.getReviewQuantity();
        reviews = graph.getIncludedReviews();

        converged = false;
        //useZNormalization = false;// zNormalization; // not used anymore
        EPSILON = delta;
        MAX_ITERATIONS = iterationLimit;
        INIT_LABEL = initLabel;
        // no transformation needed since it is an undirected graph
        //double[][] tmp = generateUpdateMatrix(graph.getGraph());
        adjacencyMatrix =  generateUpdateMatrix(graph.getGraph());
    }
    /**
     * Constructor of the HITS Algorithm.
     *
     * @param graph Review[] - Array of reviews that should be included
     */
    public HITS(ReviewGraph graph, double delta, int iterationLimit, double initLabel, double[][] AA){//, boolean zNormalization) {
        //similarityGraph = graph;
        quantityReviews = graph.getReviewQuantity();
        reviews = graph.getIncludedReviews();

        converged = false;
        //useZNormalization = false;// zNormalization; // not used anymore
        EPSILON = delta;
        MAX_ITERATIONS = iterationLimit;
        INIT_LABEL = initLabel;
        // no transformation needed since it is an undirected graph

        adjacencyMatrix =  AA;
    }

    /**
     * Method to create the adjacency matrix, where each self-edge (i=J) = 0.
     * Diagonal of the matrix is filled with zeros.
     * Required for the update calculation.
     *
     * @param weightedGraph double[][] weighted Graph containing all similarity measures
     * @return double[][] adjacency matrix  a diagonal filled with zeros.
     */
    private double[][] generateUpdateMatrix(double[][] weightedGraph) {
        int counter =0; int counterZero=0;
        double[][] adjustedGraph = new double[weightedGraph.length][weightedGraph.length];
        for (int i = 0; i < weightedGraph.length; i++) {
            for (int j = 0; j < weightedGraph[i].length; j++) {
                /*if (weightedGraph[i][j] == 0.0) {
                    adjustedGraph[i][j] = 0;
                    counterZero++;
                }else if(weightedGraph[i][j] < 0.5){
                    adjustedGraph[i][j] =0;
                    counter++;
                }else {*/
                    adjustedGraph[i][j] = weightedGraph[i][j];
                //}
                if (i == j) {
                    adjustedGraph[i][j] = 0.0;
                }
            }
        }
        return adjustedGraph;
    }

    /**
     *
     * Method the trigger the HITS Algorithm.
     */
    public void runHITS() {
        /***startPropagation */
        double[] updatedScores = new double[quantityReviews];
        int iterations = 0;
        while (!converged && iterations < MAX_ITERATIONS) {
            //System.out.println("Iteration: " + iterations);

            double[] oldScores = getOldScores();
            //MatrixUtils.printVectorDouble(oldScores);
            //Multiply and save in HitScores
            double[][] A = {{1,2,3,4},{5,0,0,8},{9,0,0,3},{4,5,6,7}};
            updatedScores = MatrixUtils.multiplyMatrixVector(adjacencyMatrix, oldScores);

            // normalize Scores
            updatedScores = normalizeScores(updatedScores);
            //MatrixUtils.printVectorDouble(updatedScores);

            // Update
            updateScoresAllNodes(updatedScores);
            iterations++;
        }
        //****make it a prob. distribution ***/
/* as discussed we do not normalize after the last iteration to make
the evaluation results comparable between PageRank and HITS
the values are only de-normalized
        double maxVal = getMax(updatedScores);

        if (useZNormalization){
            double minVal = getMin(updatedScores);
            zNormalize(updatedScores, maxVal, minVal);
        }else {
            makeProbDistribution(updatedScores, maxVal);
        }

        //*** write Predication after denormalizing*/
        writePredictions(updatedScores);

    }

    private void makeProbDistribution(double[] updatedScores, double maxVal) {
        for (int i = 0; i < updatedScores.length; i++) {
            updatedScores[i] = updatedScores[i] / maxVal;
        }
    }
    private void zNormalize(double[] updatedScores, double maxVal, double minVal) {
        for (int i = 0; i < updatedScores.length; i++) {
            updatedScores[i] = (updatedScores[i] - minVal) / (maxVal- minVal);
        }
    }

    private double getMax(double[] updatedScores) {
        double max =-1.0;
        for ( double d: updatedScores  ) {
            if (d > max){
                max = d;
            }
        }
        return max;
    }
    private double getMin(double[] updatedScores) {
        double min =1.0;
        for ( double d: updatedScores  ) {
            if (d < min){
                min = d;
            }
        }
        return min;
    }

    /**
     * Method to obtain the HITS scores of the previous run and return it in a
     * form of a vector /array.
     *
     * @return double[] - containing the previous HITS scores
     */
    private double[] getOldScores() {
        double[] oldScoresVec = new double[quantityReviews];
        for (int j = 0; j < quantityReviews; j++) {                 // for each node get score
            if (reviews[j].isKnown()) {
                oldScoresVec[j] = reviews[j].getNormalizedRating();
            } else if (reviews[j].getPredictedRating() == 0.0) {
                oldScoresVec[j] = INIT_LABEL;
            } else {
                oldScoresVec[j] = reviews[j].getPredictedRating();
            }
        }
        return oldScoresVec;
    }

    /**
     * Method to obtain the final scores the HITS algo calculated.
     *
     * @return double[] containing the final HITS scores
     */
    public double[] finalHITScores() {
        double[] finalScores = new double[quantityReviews];
        for (int j = 0; j < quantityReviews; j++) {
            if (reviews[j].isKnown()) {
                finalScores[j] = reviews[j].getRealRating();
            } else {
                finalScores[j] = reviews[j].getPredictedRating();
            }
        }
        return finalScores;
    }

    /**
     * Method to print out the final HITS Scores.
     */
    public void printFinalScores() {
        System.out.println("Final HIT Scores:");
        for (int j = 0; j < quantityReviews; j++) {
            if (reviews[j].isKnown()) {
                System.out.println(reviews[j].getPredictedRating());
            } else {
                System.out.println(reviews[j].getPredictedRating());
            }
        }
    }

    private void writePredictions(double[] updatedScores) {
        for (int j = 0; j < quantityReviews; j++) { // for each node get score
            //if (!reviews[j].isKnown()) {
                reviews[j].setPredictedRating(denormalizeHITSScore(updatedScores[j]));
            //}
        }
    }

    /**
     * Method to denormalize the calculated Scores. Assures that the
     * Evaluation results of PageRank and HITS are comparable.
     *
     * @param normalizedScore double normalized HITS score
     * @return double - denormalized HITS score
     */
    private double denormalizeHITSScore(double normalizedScore) {
        return normalizedScore * 5;
        //return normalizedScore * 4 + 1;
    }

    /**
     * Method to normalize the calculated score.
     *
     * @param scoreVec double[] array containing the calculated scores
     * @return normalized double[] array with scores
     */
    private double[] normalizeScores(double[] scoreVec) {
        //System.out.println("Before normalization");
        //MatrixUtils.printVectorDouble(scoreVec);
        double sumScores = 0.0;
        for (int i = 0; i < scoreVec.length; i++) {
                sumScores += scoreVec[i];
        }
        if (sumScores <= 0.0) {
            System.out.println("oh oh this is no good...");
            return  scoreVec;
        }

        for (int i = 0; i < scoreVec.length; i++) {
                scoreVec[i] = scoreVec[i] / sumScores;
        }
       // MatrixUtils.printVectorDouble(scoreVec);
        return scoreVec;
    }

    /**
     * Method to update the score for each node.
     *
     * @param scoreVecHITS double[] normalized, updated scores for all nodes
     */
    private void updateScoresAllNodes(double[] scoreVecHITS) {
        boolean allConverged = false;
        for (int i = 0; i < scoreVecHITS.length; i++) {
            if (!reviews[i].isKnown()) {
                // convergence
                if (Math.abs(reviews[i].getPredictedRating() - scoreVecHITS[i]) < EPSILON) {
                    allConverged = true;
                } else {
                    allConverged = false;
                }
                reviews[i].setPredictedRating(scoreVecHITS[i]);
            }
        }
        converged= allConverged;
    }

    /**
     * Method to obtain the calculated adjacency Matrix.
     *
     * @return double[][] AdjacencyMatrix
     */
    public double[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    /**
     * Method to obtain the matrix of Reviews.
     * @return
     */
    public Review[] getReviews() {
        return reviews;
    }

}
