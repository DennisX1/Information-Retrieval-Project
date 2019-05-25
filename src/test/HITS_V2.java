package test;

import data.Review;
import data.ReviewGraph;
import io.MatrixUtils;

/**
 * Class used to test out a second version of HITS - No improvement
 * @author  N.Seemann
 */
public class HITS_V2 {
    private ReviewGraph g;
    private static double DELTA;
    private static int ITERATIONS;
    private double[][] adjacency;
    private boolean converged;

    public HITS_V2(ReviewGraph graph, double delta, int iterationLimit) {
        g = graph;
        DELTA = delta;
        ITERATIONS = iterationLimit;
        adjacency = graph.getGraph();
        converged= false;
    }

    public void run(){
        double[] scores_before;
        double[] updatedScores = new double[g.getReviewQuantity()];
        int round =0;
        while (round< ITERATIONS && !converged){
            scores_before = getScores(g.getIncludedReviews());
            updatedScores = MatrixUtils.multiplyMatrixVector(adjacency, scores_before);


            updatedScores = normalizeScores(updatedScores);

            updatePredictions(updatedScores);
            round++;
        }

        convertBack(updatedScores);


    }
    private void convertBack(double[] scores){
        for (int i = 0; i < scores.length ; i++) {
           if (! g.getIncludedReviews()[i].isKnown()){
                g.getIncludedReviews()[i].setPredictedRating(scores[i] *5);
            }
        }

    }
    private void updatePredictions(double[] updatedScores) {
        boolean conv = false;
        for (int i = 0; i < updatedScores.length; i++) {
            if (!g.getIncludedReviews()[i].isKnown()){
                if ((Math.abs(g.getIncludedReviews()[i].getPredictedRating()-updatedScores[i]))< DELTA){
                    conv = true;
                }else {
                    conv = false;
                }
                g.getIncludedReviews()[i].setPredictedRating(updatedScores[i]);
            }
        }

        converged= conv;

    }

    private double[] normalizeScores(double[] updatedScores) {
        double[] tmp = new double[updatedScores.length];
        double sum =0.0;
        for (double d : updatedScores
             ) {
            sum = sum + d;
        }

        if (sum == 0.0){
            return updatedScores;
        }
        for (int i = 0; i < updatedScores.length ; i++) {
            tmp[i] = updatedScores[i]/sum;
        }
        return tmp;

    }

    private double[] getScores(Review[] includedReviews) {
        double[] tmp = new double[includedReviews.length];
        for (int i = 0; i < tmp.length ; i++) {
            if(includedReviews[i].isKnown()) {
                tmp[i] = includedReviews[i].getRealRating();
            }
            else{
                tmp[i] = includedReviews[i].getPredictedRating();
            }
        }
        return tmp;

    }
}
