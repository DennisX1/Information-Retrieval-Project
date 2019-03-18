package LinkAnalysis;

import data.ReviewGraph;
import io.MatrixUtils;
import main.Main;

import java.util.HashMap;
import java.util.Map;

public class HITS {
    private static double[][] weightedGraph;
    private static double[][] updateAuthMatrix;
    private static double[][] updateHubsMatrix;
    private static Map<Integer, HITS_Scores> scoreCollection;


    public HITS(ReviewGraph graph) {
        scoreCollection = new HashMap<>();
        int limit = new Main().getLIMIT();
        weightedGraph = graph.getGraph();
        double[][] transposedGraph = MatrixUtils.transposeMatrix(graph.getGraph());
        MatrixUtils.printMatrixDouble(graph.getGraph(), "Initial Graph");
        MatrixUtils.printMatrixDouble(transposedGraph, "Transposed Graph");

        updateAuthMatrix = createUpdateMatrix(graph.getGraph(),transposedGraph );
        MatrixUtils.printMatrixDouble(updateAuthMatrix, "L^T*L");
        updateHubsMatrix = createUpdateMatrix(transposedGraph, graph.getGraph());
        MatrixUtils.printMatrixDouble(updateHubsMatrix, "L*L^T");
        updateHubsMatrix = MatrixUtils.transposeMatrix(updateAuthMatrix);
        MatrixUtils.printMatrixDouble(updateHubsMatrix, "L*L^T V2");
        System.out.println( "CHECK if always symmetric and than reduce calculations and sizes");


    }

    public void runHITS(int iterations, double exclusionThreshold){
        double newAuth= -1.0;
        double newHub=-1.0;
        for (int i = 0; i < iterations ; i++) {
            //*** update auth
            double[] vecY = new double[weightedGraph[0].length];
            double[] vecX = new double[weightedGraph[0].length];
            for (int j = 0; j <weightedGraph[0].length ; j++) { // equal to number of nodes
                for (int k = 0; k < weightedGraph[0].length ; k++) { // vector position
                    HITS_Scores otherNodes = scoreCollection.get(k);
                    vecX[k]= otherNodes.getAuthorityScore();
                    vecY[k]= otherNodes.getHubScore();
                }
                //Multiply and save in HitScores
                //TODO

                HITS_Scores curNode = scoreCollection.get(j);
                curNode.updateScores(newAuth,  newHub);
            }


            if(newAuth <exclusionThreshold && newAuth != -1.0){
                newAuth = 0.0;
            }
            //*** update hubs
            if(newHub <exclusionThreshold && newHub != -1.0 ){
                newHub = 0.0;
            }
        }
    }

    private double[][] createUpdateMatrix(double[][] matrix1, double[][] matrix2) {
        return MatrixUtils.matrixMultiplicationSameSize(matrix1, matrix2);
    }




    public static final int[][] graph ={    {0,0,0,1,0,0,0,0}, //A
                                            {0,0,1,0,1,0,0,0},
                                            {1,0,0,0,0,0,0,0},
                                            {0,1,1,1,0,1,0,0},
                                            {0,1,1,0,0,0,0,0},
                                            {0,0,1,0,0,0,0,1},
                                            {1,0,1,0,0,0,0,0},
                                            {1,0,0,0,0,0,0,0}, // H
                                        };


}
