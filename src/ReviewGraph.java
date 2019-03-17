
/**
 * Class to represent the graph of review nodes and the weights of their uni-directed arcs. Graph is supposedly fully connected.
 *
 * @author N.Seemann
 */
public class ReviewGraph {


    private static double[][] graph; // fully conncected graph

    public ReviewGraph(int numberOfReviewNodes) {
        graph = new double[numberOfReviewNodes - 1][numberOfReviewNodes];
    }

    /*public ReviewGraph( Review reviewContainer, int NumReviews){
        graph    = new double[NumReviews][NumReviews];
        for (int i = 0; i < NumReviews; i++) {
            for (int j = 1; j < NumReviews; j++) {
                graph[i][j]    =  reviewContainer.get(i+j);
            }
        }
    }*/

    public double[][] getGraph() {
        return graph;
    }

    public void setArcWeight(int reviewID1, int reviewID2, double weight) {
        graph[reviewID1][reviewID2] = weight;
    }


    public String toString() {
        StringBuilder print = new StringBuilder("Graph: \n");
        for (int i = 0; i < graph.length; i++) { //last row not printed
            for (int j = 0; j <= graph.length; j++) { // i=j not good for printing
                //print.append("[" + i + ";" + j + "]~" + graph[i][j]);
                print.append( graph[i][j] + "\t\t");
            }
            print.append("\n");
        }

        return print.toString();
    }

    public static void main(String[] args) {
        ReviewGraph testGraph = new ReviewGraph(3);
        Review a = new Review("jksjdk");
        Review b = new Review("Hallo");
        Review c = new Review("Wie ");


        //((n*n)-n )/2
        testGraph.setArcWeight(0, 1, 0.3);
        testGraph.setArcWeight(0, 2, 0.3);
        testGraph.setArcWeight(1, 2, 0.3);

        System.out.println(testGraph.toString());


    }

}
