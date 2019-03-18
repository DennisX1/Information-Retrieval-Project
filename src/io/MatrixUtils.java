package io;

public class MatrixUtils {

    public static void printMatrixDouble(double[][] matrix){
        System.out.println( matrixToString(matrix));

    }
    public static void printMatrixDouble(double[][] matrix, String additionalInfo){
        System.out.println(additionalInfo);
        System.out.println( matrixToString(matrix));

    }

    public static String matrixToString(double[][] matrix){
        StringBuilder print = new StringBuilder("Graph: \n");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) { // i=j not good for printing
                print.append( matrix[i][j] + "\t\t");
            }
            print.append("\n");
        }
        return print.toString();
    }
}
