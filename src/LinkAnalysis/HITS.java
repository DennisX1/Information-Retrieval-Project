package LinkAnalysis;

import data.ReviewGraph;
import io.MatrixUtils;
import main.Main;

public class HITS {
    private static double[][] transposedGraph;
    private static double[][] updateAuthMatrix;
    private static double[][] updateHubsMatrix;


    public HITS(ReviewGraph graph) {
        int limit = new Main().getLIMIT();
        transposedGraph = transposeMatrix(graph.getGraph());
        MatrixUtils.printMatrixDouble(graph.getGraph(), "Initial Graph");
        MatrixUtils.printMatrixDouble(transposedGraph, "Transposed Graph");

        updateAuthMatrix = createUpdateMatrix(graph.getGraph(),transposedGraph );
        MatrixUtils.printMatrixDouble(updateAuthMatrix, "L^T*L");
        updateHubsMatrix = createUpdateMatrix(transposedGraph, graph.getGraph());
        MatrixUtils.printMatrixDouble(updateHubsMatrix, "L*L^T");
        updateHubsMatrix = transposeMatrix(updateAuthMatrix);
        MatrixUtils.printMatrixDouble(updateHubsMatrix, "L*L^T V2");
    }

    private double[][] createUpdateMatrix(double[][] matrix1, double[][] matrix2) {
        if (matrix1.length ==  matrix2.length){
            double [][] tmp = new double [matrix1.length][matrix1.length];
            for (int i = 0; i < matrix1.length; i++) { // zeile
                for (int j = 0; j < matrix2.length; j++) { // spalte
                    tmp[i][j] =0;
                    for (int k = 0; k < matrix1.length && k< matrix2.length ; k++) { // iterator
                        //double n1 =weights[i][k] ;
                        //double n2 =transposedGraph[k][j] ;
                        //tmp[i][j] += n1* n2;
                        tmp[i][j] += matrix1[i][k] * matrix2[k][j] ;
                    }
                }
            }
            return tmp;
        }
        return null;

    }


    private double[][] transposeMatrix(double[][] weights){
        double [][] tmp = new double[weights.length][weights[1].length];
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                tmp[j][i] = weights[i][j];
            }
        }


        return tmp;

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

   /* public static Map<String, Integer> incomingArcs = new HashMap<String, Integer>();
    public static Map<String, Integer> outgoingArcs = new HashMap<String, Integer>();
    public static Map<String, Hyperlinks > hyperlinks = new HashMap<String, Hyperlinks>();

    public static Map<String, HitsScore> scoreMap = new HashMap<String, HitsScore>();
    public static String[] NAME = {"A", "B", "C", "D", "E", "F", "G", "H"};


    public static void main (String[] args){
        for (int i = 0; i < NAME.length ; i++) {
            HitsScore tmp = new HitsScore(String.valueOf(i));

            scoreMap.put(NAME[i], tmp );
            for (int j = 0; j < NAME.length ; j++) {
               // erstmal zählen



               if ( graph[i][j] != 0){
                   // hier evtl mit Objekten arbeiten



                   *//*if (outgoingArcs.containsKey(NAME[i])){
                       outgoingArcs.put(NAME[i], outgoingArcs.get(NAME[i]) +1);
                   } else {
                       outgoingArcs.put(NAME[i], 1);
                   }


                   if (incomingArcs.containsKey(NAME[j]) ){
                       incomingArcs.put(NAME[j], incomingArcs.get(NAME[j]) +1);
                   } else {
                       incomingArcs.put(NAME[j], 1);
                   }*//*


                   addEdgeOut(j,i);
                   addEdgeIn(i, j);




               }



            }

        }
        System.out.println("huhu");





    }

    private static void addEdges (int to, int from, boolean incomingArc){
        if (hyperlinks.containsKey(NAME[from])){
            hyperlinks.get(NAME[from]).addEdge( NAME[to], incomingArc);
        } else {
            Hyperlinks hyper = new Hyperlinks();
            hyper.addEdge(NAME[to], incomingArc);
            hyperlinks.put(NAME[from], hyper);
        }
    }
    private static void addEdgeOut (int to, int from){
        addEdges(to, from , FALSE);
    }
    private static void addEdgeIn (int to, int from){
        addEdges(to, from, TRUE);
    }

    private static double calculateAuthority(String node){
        double auth = 0.0;
        Map<String, Double> links = hyperlinks.get(node).getFromNodeS();

        Iterator it = links.entrySet().iterator();

        while   (it.hasNext()){

        }
        return 0.0;
    }*/
}
