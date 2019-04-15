package LinkAnalysis;

import data.Review;
import data.ReviewGraph;
import io.MatrixUtils;


/**
 * Class to represent the HITS algorithm. It is a slight variation to the original implementation.
 * Assumptions: HubScore = AuthScore  => therefore, we introduce HITS_Score as the only score
 * Update Matrix L*L^T = L^T*L
 * Updating rule: score^k = L * score^(k-1)
 * Update Matrix L <=> weighted Graph of reviews
 */
public class HITS {
    private ReviewGraph similarityGraph;
    private double[][] weightedGraph;
    private Review[] reviews;
    private int quantityReviews;
    private double[][] updateMatrix;
    //private Map<Integer, HITS_Score> scoreCollection;
     private boolean converged;
    //public static final double EPSILON = 0.000001;
    //public static final int MAX_ITERATIONS = 30;
    //private static final double INITLABEL = 0.4;
    public static double EPSILON;
     public static int MAX_ITERATIONS;
    private static  double INIT_LABEL;

    /**
     * Constructor of the HITS Algorithm.
     *
     * @param graph   Review[] - Array of reviews that should be included
     *
     */
    public HITS(ReviewGraph graph, double delta, int iterationLimit, double initLabel) {
        similarityGraph = graph;
        quantityReviews = graph.getReviewQuantity();
        reviews = graph.getIncludedReviews();
        weightedGraph = graph.getGraph();           //shallow Copy
        converged = false;
        EPSILON= delta;
        MAX_ITERATIONS = iterationLimit;
        INIT_LABEL = initLabel;
        updateMatrix = weightedGraph; // no transformation needed since it is an undirected graph
    }

    /**
     * Method the trigger the HITS Algorithm.
     *
     *
     */
    public void runHITSV2() {

      startPropagation();

    }
    private void startPropagation(){
        double[] updatedScores = new double[quantityReviews];
        int iterations =0;
        int test =0;
        while (!converged && iterations < MAX_ITERATIONS ) {
            System.out.println("Iteration: "+ iterations);

            double[] oldScores = getOldScores();
            MatrixUtils.printVectorDouble(oldScores);
            //Multiply and save in HitScores
            updatedScores = MatrixUtils.multiplyMatrixVectorWeighted(updateMatrix, oldScores, weightedGraph);

            // normalize
            updatedScores = normalizeScores(updatedScores);
            MatrixUtils.printVectorDouble(updatedScores);
            // Update
            updateScoresAllNodes(updatedScores);
            iterations++;
        }

        writePredictions(updatedScores);
    }

    private double[] getOldScores(){
        double[] oldScoresVec = new double[quantityReviews];
        for (int j = 0; j < quantityReviews; j++) {                 // for each node get score
            if (reviews[j].isKnown()){
                oldScoresVec[j] = reviews[j].getNormalizedRating();
            }
            else if (reviews[j].getPredictedRating() == 0.0){
                oldScoresVec[j] = INIT_LABEL;
            }
            else {
                oldScoresVec[j] = reviews[j].getPredictedRating();
            }
        }
        return   oldScoresVec;
    }
    public double[] finalHITScores(){
        double[] finalScores = new double[quantityReviews];
        for (int j = 0; j < quantityReviews; j++) {
            if (reviews[j].isKnown()){
                finalScores[j] = reviews[j].getNormalizedRating();
            } else {
                finalScores[j] = reviews[j].getPredictedRating();
            }
        }
        return finalScores;
    }

    public void printFinalScores (){
        System.out.println("Final HIT Scores:");
        for (int j = 0; j < quantityReviews; j++) {                 // for each node get score
            if (reviews[j].isKnown()){
                System.out.println(reviews[j].getNormalizedRating());
            }
            else {System.out.println( reviews[j].getPredictedRating());
            }
        }
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
        for (int i = 0; i < scoreVecHITS.length; i++) {
            if (!reviews[i].isKnown()){
                // convergence
                if( Math.abs(reviews[i].getPredictedRating() -scoreVecHITS[i]) < EPSILON){
                    converged = true;
                }
                reviews[i].setPredictedRating(scoreVecHITS[i]);
            }
        }
    }

    private double[][] createUpdateMatrix(double[][] matrix1, double[][] matrix2) {
        return MatrixUtils.matrixMultiplicationSameSize(matrix1, matrix2);
    }


}
