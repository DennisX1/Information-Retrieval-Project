package data;

import io.MatrixUtils;

/**
 * Class to represent the weightedGraph of review nodes and the weights of their uni-directed arcs. Graph is supposedly fully connected. - Graph omits last row in output
 *
 * @author N.Seemann
 */
public class ReviewGraph {
    public static int QUANTITY_NODES  = 2000; // size of the graph / number of nodes we consider

    private static double[][] weightedGraph; // fully connected weightedGraph

    public ReviewGraph(int numberOfReviewNodes) {
        weightedGraph = new double[numberOfReviewNodes][numberOfReviewNodes]; // cannot spare a row, due to HITS algo
    }

    /*public ReviewGraph( Review reviewContainer, int NumReviews){
        weightedGraph    = new double[NumReviews][NumReviews];
        for (int i = 0; i < NumReviews; i++) {
            for (int j = i+1; j < NumReviews; j++) {
                weightedGraph[i][j]    =  reviewContainer.get(i+j);
            }
        }
    }*/

    public double[][] getGraph() {
        return weightedGraph;
    }

    public void setArcWeight(int reviewID1, int reviewID2, double weight) {  // like this we store less weights
        if(reviewID1 < reviewID2) {
            weightedGraph[reviewID1][reviewID2] = weight;
        } else {
            weightedGraph[reviewID2][reviewID1] = weight;
        }
    }


    public String toString() {
        return MatrixUtils.matrixToString(weightedGraph);
    }

    public static void addALLReviewsRANDOM(Review[] revs){

        double random;
        for (int i = 0; i <revs.length-1; i++) {
            for (int j = i; j <revs.length ; j++) {
                // if i =j -> 0 since reflective
                if ( i == j){
                    weightedGraph[i][j] = 1.0;
                } else {
                    random = Math.random();
                    weightedGraph[i][j] = random;
                    weightedGraph[j][i] = random;
                }
            }
        }
    }
}
