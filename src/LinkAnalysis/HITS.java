package LinkAnalysis;

import data.Review;
import data.ReviewGraph;
import io.MatrixUtils;

import java.util.*;

public class HITS {
    private double[][] weightedGraph;
    private Review[] reviews;
    private int quantityReviews;
    private double[][] updateAuthMatrix;
    private double[][] updateHubMatrix;
    private Map<Integer, HITS_Scores> scoreCollection;
    private int topK = 200;
    private int[] topKReviewIDs;


    public HITS(ReviewGraph graph, Review[] reviews) {
        System.out.println("CHECK if always symmetric and than reduce calculations and sizes");
        this.reviews = reviews;
        quantityReviews = reviews.length;
        topKReviewIDs = new int[topK];
        weightedGraph = graph.getGraph();
        scoreCollection = new HashMap<>();
        /** Regular approach  ***/
        /*
        double[][] transposedGraph = MatrixUtils.transposeMatrix(graph.getGraph());
        //MatrixUtils.printMatrixDouble(graph.getGraph(), "Initial Graph");
        //MatrixUtils.printMatrixDouble(transposedGraph, "Transposed Graph");

        updateAuthMatrix = createUpdateMatrix(graph.getGraph(), transposedGraph);
        //MatrixUtils.printMatrixDouble(updateAuthMatrix, "L^T*L");
        updateHubMatrix = createUpdateMatrix(transposedGraph, graph.getGraph());
        //MatrixUtils.printMatrixDouble(updateHubMatrix, "L*L^T");
        //updateHubMatrix = MatrixUtils.transposeMatrix(updateAuthMatrix);
        //MatrixUtils.printMatrixDouble(updateHubMatrix, "L*L^T V2");*/

        /******** making use of undirected arcs **********/
        updateAuthMatrix = weightedGraph;
        updateHubMatrix = weightedGraph;
    }

    private void resetMatrices() {
        double[][] transposedGraph = MatrixUtils.transposeMatrix(weightedGraph);
        //MatrixUtils.printMatrixDouble(weightedGraph, "Initial Graph");
        //MatrixUtils.printMatrixDouble(transposedGraph, "Transposed Graph");

        updateAuthMatrix = createUpdateMatrix(weightedGraph, transposedGraph);
        //MatrixUtils.printMatrixDouble(updateAuthMatrix, "L^T*L");
        updateHubMatrix = createUpdateMatrix(transposedGraph, weightedGraph);
        //MatrixUtils.printMatrixDouble(updateHubMatrix, "L*L^T");
        //updateHubMatrix = MatrixUtils.transposeMatrix(updateAuthMatrix);
        //MatrixUtils.printMatrixDouble(updateHubMatrix, "L*L^T V2");
        scoreCollection.clear();

    }

    private void resetMatricesNoTransposition() {
        updateAuthMatrix = weightedGraph;
        updateHubMatrix = weightedGraph;
        scoreCollection.clear();
    }

    public void runHITS(int iterations, double exclusionThreshold) {
        //double[] newHubsVec = new double[quantityReviews];
        double[] scoreVecHITS = new double[quantityReviews];

        for (int i = 0; i < iterations; i++) {

            double[] vecY = new double[quantityReviews];
            double[] vecX = new double[quantityReviews];

            for (int j = 0; j < quantityReviews; j++) { // for each node get score

                HITS_Scores otherNodes = getScoreFromCollection(reviews[j].getId());
                //*** vecX[j] = otherNodes.getAuthorityScore();
                vecY[j] = otherNodes.getHubScore();
            }

            //Multiply and save in HitScores
            scoreVecHITS = MatrixUtils.multiplyMatrixVectorWeighted(updateAuthMatrix, vecY, weightedGraph);
            //*** newHubsVec = MatrixUtils.multiplyMatrixVectorWeighted(updateHubMatrix, vecX, weightedGraph);

            // normalize
            scoreVecHITS = normalizeScores(scoreVecHITS);
            //*** newHubsVec = normalizeScores(newHubsVec);
            updateScoresAllNodes(scoreVecHITS, exclusionThreshold);


        }

        System.out.println("TEST   2:");
        MatrixUtils.printVectorDouble(scoreVecHITS);

        getTopKReviews();
    }

    private double[] normalizeScores(double[] scoreVec) {
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


    private void updateScoresAllNodes(double[] scoreVecHITS, double exclusionThreshold) {
        double auth;
        double hub;
        for (int i = 0; i < scoreVecHITS.length; i++) {
            HITS_Scores cur = getScoreFromCollection(reviews[i].getId());
            auth = scoreVecHITS[i];
            hub = 1.0;
            if (auth < exclusionThreshold) {
                auth = 0.0;
            }
            //*** update hubs
            if (hub < exclusionThreshold) {
                hub = 0.0;
            }
            cur.updateScores(auth, hub);
        }
    }

    private double getWeight(int row, int col) {
        // potenital to save less if symmetric
        return weightedGraph[row][col];
    }

    private HITS_Scores getScoreFromCollection(int k) {
        HITS_Scores node = scoreCollection.get(k);
        if (node == null) { // not yet in collection
            node = new HITS_Scores();
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
        printMap(scoreCollection);
        // TODO differ between unknown and known label nodes?
        List<Map.Entry<Integer, HITS_Scores>> list = new ArrayList<>(scoreCollection.entrySet());

        /*Collections.sort(list, new Comparator<Map.Entry<Integer, HITS_Scores>>() {
            public int compare(Map.Entry< Integer, HITS_Scores> o1,
                               Map.Entry< Integer, HITS_Scores> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });*/
        Collections.sort(list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        int k = 0;
        for (Map.Entry<Integer, HITS_Scores> entry : list ) {
            if (k < topK) {
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

    public int findPositionOfReview(int ID){
        for (int i = 0; i <quantityReviews ; i++) {
            if(reviews[i].getId() == ID){
                return i;
            }
        }
        return -1;
    }
}
