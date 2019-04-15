package data;

import io.MatrixUtils;

/**
 * Class to represent the weightedGraph of review nodes and the weights of their uni-directed arcs. Graph is supposedly fully connected. - Graph omits last row in output
 *
 * @author N.Seemann
 */
public class ReviewGraph {
    private double[][] weightedGraph; // fully connected weightedGraph
    private Review[] includedReviews;

    public ReviewGraph(int numberOfReviewNodes) {
        weightedGraph = new double[numberOfReviewNodes][numberOfReviewNodes]; // cannot spare a row, due to HITS algo
    }

    /**
     * Constructor to create a graph for the given queries with the given similarity measures.
     *
     * @param reviews      Review[] Array of reviews
     * @param similarities double[][] matrix with similarity measures
     */
    public ReviewGraph(Review[] reviews, double[][] similarities) {
        includedReviews = reviews;
        weightedGraph = new double[reviews.length][reviews.length];
        for (int i = 0; i < reviews.length; i++) {
            for (int j = i; j < reviews.length; j++) {
                weightedGraph[i][j] = similarities[i][j];
            }
        }
    }

    /**
     * Method to return the graph.
     *
     * @return double[][] array representing the graph
     */
    public double[][] getGraph() {
        return weightedGraph;
    }

    /**
     * Method used to set the weight in the graph to a desired weight. Since the graph is undirected
     * this is to be done for i->j and j->i. Used for low similarity measures.
     *
     * @param reviewID1 int ID of first review
     * @param reviewID2 int ID of second review
     * @param weight    new weight. Probability 0 at all time.
     */
    public void setArcWeight(int reviewID1, int reviewID2, double weight) {  // like this we store less weights
        if (reviewID1 < reviewID2) {
            weightedGraph[reviewID1][reviewID2] = weight;
        } else {
            weightedGraph[reviewID2][reviewID1] = weight;
        }
    }

    /**
     * Method to print out the graph (e.g. the matrix)
     *
     * @return graph represented as a String
     */
    public String toString() {
        return MatrixUtils.matrixToString(weightedGraph);
    }

    /**
     * Method to fill the graph with random weights.
     *
     * @param revs Array of Reviews that should be included in the graph
     */
    public void addALLReviewsRANDOM(Review[] revs) {
        includedReviews = revs;
        double random;
        for (int i = 0; i < revs.length; i++) {
            for (int j = i; j < revs.length; j++) {
                // if i =j -> 0 since reflective
                if (i == j) {
                    weightedGraph[i][j] = 1.0;
                } else {
                    random = Math.random();
                    weightedGraph[i][j] = random;
                    weightedGraph[j][i] = random;
                }
            }
        }
    }

    /**
     * Method to fill the graph with random weights. If weight is below a given threshold
     * it is set to zero. Requires check that the graph is still connected.
     *
     * @param revs               Array of Reviews that should be included in the graph
     * @param exclusionthreshold double value, lower bound for weights in graph
     */
    public void addALLReviewsRANDOM(Review[] revs, double exclusionthreshold) {
        double random;
        for (int i = 0; i < revs.length; i++) {
            for (int j = i; j < revs.length; j++) {
                // if i =j -> 0 since reflective
                if (i == j) {
                    weightedGraph[i][j] = 0.0;
                } else {
                    random = Math.random();
                    if (random < exclusionthreshold) {
                        random = 0.0;
                    }
                    weightedGraph[i][j] = random;
                    weightedGraph[j][i] = random;
                }
            }
        }
    }

    /**
     * Method to obtain the array of reviews the graph contains.
     *
     * @return Review[] Array of included reviews / reviews the graph is based on
     */
    public Review[] getIncludedReviews() {
        return includedReviews;
    }

    /**
     * Method to obtain the number of reviews the graph contains.
     *
     * @return in - number of reviews the Graph contains
     */
    public int getReviewQuantity() {
        return includedReviews.length;
    }
}
