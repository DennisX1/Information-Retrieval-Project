package LinkAnalysis;

import data.ReviewGraph;
import io.MatrixUtils;
import main.Main;

import java.util.HashMap;
import java.util.Map;

public class HITS {
    private static double[][] weightedGraph;
    private static double[][] updateAuthMatrix;
    private static double[][] updateHubMatrix;
    private static Map<Integer, HITS_Scores> scoreCollection;


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
    private void resetMatrices(){
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
    private void resetMatricesNoTransposition(){
        updateAuthMatrix = weightedGraph;
        updateHubMatrix =  weightedGraph;
        scoreCollection.clear();
    }

    public void runHITS(int iterations, double exclusionThreshold) {
        double newAuth = 0.0;
        double newHub = 0.0;
        double[] newHubsVec;
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
            newHubsVec = MatrixUtils.multiplyMatrixVectorWeighted (updateHubMatrix, vecX, weightedGraph);

            // normalize
            newAuthsVec = normalizeScores(newAuthsVec);
            //newHubsVec = normalizeScores(newHubsVec);
            updateScoresAllNodes(newAuthsVec, newHubsVec, exclusionThreshold);
        }

        System.out.println("TEST   2:");
        MatrixUtils.printVectorDouble(newAuthsVec);
    }

    private double[] normalizeScores(double[] scoreVec) {
        double sumScore = 0.0;
        for (int i = 0; i < scoreVec.length; i++) {
            sumScore += scoreVec[i];
        }
        for (int i = 0; i < scoreVec.length; i++) {
            scoreVec[i] = scoreVec[i] / sumScore;
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


    public static final int[][] graph = {{0, 0, 0, 1, 0, 0, 0, 0}, //A
            {0, 0, 1, 0, 1, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 0, 1, 0, 0},
            {0, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 1, 0, 0, 0, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0}, // H
    };


}
