package io;

public class MatrixUtils {

    public static void printMatrixDouble(double[][] matrix) {
        System.out.println(matrixToString(matrix));

    }

    public static void printMatrixDouble(double[][] matrix, String additionalInfo) {
        System.out.println(additionalInfo);
        System.out.println(matrixToString(matrix));

    }

    public static String matrixToString(double[][] matrix) {
        StringBuilder print = new StringBuilder("Graph: \n");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) { // i=j not good for printing
                print.append(matrix[i][j] + "\t\t");
            }
            print.append("\n");
        }
        return print.toString();
    }

    public static double[][] transposeMatrix(double[][] weights) {
        double[][] tmp = new double[weights.length][weights[1].length];
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                tmp[j][i] = weights[i][j];
            }
        }
        return tmp;
    }

    public static double[][] matrixMultiplicationSameSize(double[][] matrix1, double[][] matrix2) {

    if(matrix1.length ==matrix2.length)

    {
        double[][] tmp = new double[matrix1.length][matrix1.length];
        for (int i = 0; i < matrix1.length; i++) { // zeile
            for (int j = 0; j < matrix2.length; j++) { // spalte
                tmp[i][j] = 0;
                for (int k = 0; k < matrix1.length && k < matrix2.length; k++) { // iterator

                    tmp[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
        return tmp;
    }
        return null;
}
}
