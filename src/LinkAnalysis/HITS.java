package LinkAnalysis;

import data.Review;
import data.ReviewGraph;
import data.Sentiment;
import io.MatrixUtils;
import main.Main;

import java.util.HashMap;
import java.util.Map;

public class HITS {
    private static double[][] weightedGraph;
    private static double[][] updateAuthMatrix;
    private static double[][] updateHubMatrix;
    private static Map<Integer, HITS_Scores> scoreCollection;
    private static double topK = 0.15;
    private int[] topKReviews;

    public static final int[][] graph = {{0, 0, 0, 1, 0, 0, 0, 0}, //A
            {0, 0, 1, 0, 1, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 0, 1, 0, 0},
            {0, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 1, 0, 0, 0, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0}, // H
    };


    public HITS(ReviewGraph graph) {
        System.out.println("CHECK if always symmetric and than reduce calculations and sizes");
        scoreCollection = new HashMap<>();
        int limit = new Main().getLIMIT();
        weightedGraph = graph.getGraph();

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

        /******** making use of Symmetry **********/
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
        double newAuth = 0.0;
        double newHub = 0.0;
        double[] newHubsVec = new double[weightedGraph[0].length];
        double[] newAuthsVec = new double[weightedGraph[0].length];

        for (int i = 0; i < iterations; i++) {
            //*** update auth
            double[] vecY = new double[weightedGraph[0].length];
            double[] vecX = new double[weightedGraph[0].length];
            for (int j = 0; j < weightedGraph[0].length; j++) { // for each node get score

                HITS_Scores otherNodes = getScoreFromCollection(j);
                vecX[j] = otherNodes.getAuthorityScore();
                vecY[j] = otherNodes.getHubScore();
            }

            //Multiply and save in HitScores
            newAuthsVec = MatrixUtils.multiplyMatrixVectorWeighted(updateAuthMatrix, vecY, weightedGraph);
            newHubsVec = MatrixUtils.multiplyMatrixVectorWeighted(updateHubMatrix, vecX, weightedGraph);

            // normalize
            newAuthsVec = normalizeScores(newAuthsVec);
            newHubsVec = normalizeScores(newHubsVec);
            updateScoresAllNodes(newAuthsVec, newHubsVec, exclusionThreshold);

            if (i == iterations - 1) {
                // store top K Scores

                getTopKReviews(newAuthsVec);
            }
        }

        System.out.println("TEST   2:");
        MatrixUtils.printVectorDouble(newAuthsVec);
        MatrixUtils.printVectorDouble(newHubsVec);
    }

    private double[] normalizeScores(double[] scoreVec) {
        double sumScores = 0.0;
        for (int i = 0; i < scoreVec.length; i++) {
            sumScores += scoreVec[i];
        }
        // TODO what to do if the sum is zero?
        if (sumScores > 0.0) {
            for (int i = 0; i < scoreVec.length; i++) {
                scoreVec[i] = scoreVec[i] / sumScores;
            }
        }
        return scoreVec;
    }


    private void updateScoresAllNodes(double[] newAuthsVec, double[] newHubsVec, double exclusionThreshold) {
        double auth;
        double hub;
        for (int i = 0; i < newAuthsVec.length; i++) {
            HITS_Scores cur = getScoreFromCollection(i);
            auth = newAuthsVec[i];
            hub = newHubsVec[i];
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


    public Sentiment propagateSentiment(Review[] reviews) {

        // for which review?
        int testNode = 20;
        Review curRev = reviews[2];
        caculateSentiment(curRev);

        return null;
    }

    private int[] getTopKReviews(double[] scoreVec) {
        // for each node the same?
        topKReviews = new int[(int) (weightedGraph.length * topK)];
        double[] tmpTopKScores = new double[(int) (weightedGraph.length * topK)];
        for (int i = 0; i < topKReviews.length; i++) {
            topKReviews[i] = -1;
        }
        double score;
        double cur;
        double next;
        for (int i = 0; i < scoreVec.length; i++) {
            score =scoreVec[i];
            for (int j = 0; j < topKReviews.length-1; j++) {
                if(topKReviews[j] == -1){
                    // no value yet
                    topKReviews[j] = i;
                    break;
                }else{
                cur =scoreVec[topKReviews[j]];
                next = scoreVec[topKReviews[j+1]];
                if (score < scoreVec[topKReviews[j]]) {
                    break;
                } else if (score > scoreVec[topKReviews[j+1]]) {
                    continue;
                } else if (score== scoreVec[topKReviews[j+1]]) {
                    System.out.println("OH oh...");

                } else if ((score < scoreVec[topKReviews[j+1]] && score >  scoreVec[topKReviews[j]])  ) {
                    int tmp = topKReviews[j];
                    int tmp2;
                    topKReviews[j] = i;
                    for (int k = j - 1; k >= 0; k--) {
                        tmp2 = topKReviews[k];
                        topKReviews[k] = tmp;
                        tmp = tmp2;
                    }

                }}

            }

        }
        return topKReviews;
    }

    private Sentiment caculateSentiment(Review reviews) {
        double sentiment= 0.0;
        //idea = weight * label over all top K nodes avg by number of nodes
        // SIGMA sim measure * label) /k
        // for which review?
        int row =reviews.getId();
        int column;
        for (int i = 0; i < topKReviews.length; i++) {
            column = topKReviews[i];
            sentiment += weightedGraph[row][topKReviews[i]]  * 11;
        }
        return null;
    }
}
