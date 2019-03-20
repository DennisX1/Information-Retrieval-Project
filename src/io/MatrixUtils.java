package io;

/**
 * Class offering different matrix operations, as well as print operations.
 * @author N. Seemann
 */
public class MatrixUtils {

    public static void printMatrixDouble(double[][] matrix) {
        System.out.println(matrixToString(matrix));

    }

    public static void printVectorDouble(double[] vector){
        StringBuilder print = new StringBuilder("Vector \n");
        for (int i = 0; i < vector.length; i++) {
            print.append(vector[i] + "\t\t");
            print.append("\n");
        }
        System.out.println(print.toString());
    }

    public static void printMatrixDouble(double[][] matrix, String additionalInfo) {
        System.out.println(additionalInfo);
        System.out.println(matrixToString(matrix));

    }

    public static String matrixToString(double[][] matrix) {
        StringBuilder print = new StringBuilder();
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
        if (matrix1.length == matrix2.length) {
            double[][] tmp = new double[matrix1.length][matrix1.length];
            for (int i = 0; i < matrix1.length; i++) { // zeile
                for (int j = 0; j < matrix2[0].length; j++) { // spalte
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

    public static double[] multiplyMatrixVectorWeighted(double[][] matrix, double[] vector,double[][] weights){
        double[] updatedVec = new double[matrix.length];
        for (int row = 0; row < matrix.length; row++) {
            double sum = 0;
            for (int column = 0; column < matrix[0].length; column++) {
                //System.out.println("[" + row + ", "+ column + "]:"+ weights[row][column]);
                double weight =  weights[row][column] ;
                sum += matrix[row][column] * (vector[column] * weights[row][column]) ;
            }
            updatedVec[row] = sum;
        }
        return updatedVec;
    }
    public static double[] multiplyMatrixVector(double[][] matrix, double[] vector){
        double[] updatedVec = new double[matrix.length];

        for (int row = 0; row < matrix.length; row++) {
            double sum = 0;
            for (int column = 0; column < matrix[0].length; column++) {
                sum += matrix[row][column] * vector[column] ;
            }
            updatedVec[row] = sum;
        }
        //MatrixUtils.printVectorDouble(updatedVec);
        return updatedVec;
    }
}
