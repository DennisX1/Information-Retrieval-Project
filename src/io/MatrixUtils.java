package io;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Class offering different matrix operations, as well as print options.
 *
 * @author N. Seemann
 */
public class MatrixUtils {

    /**
     * Method to print out an 2-dim. array/matrix.
     *
     * @param matrix double[][]
     */
    public static void printMatrixDouble(double[][] matrix) {
        System.out.println(matrixToString(matrix));
    }

    /**
     * Method to print out an 1-dim. array/vector.
     *
     * @param vector double[]
     */
    public static void printVectorDouble(double[] vector) {
        NumberFormat formatter = new DecimalFormat("#.########");
        StringBuilder print = new StringBuilder("Vector \n");
        for (int i = 0; i < vector.length; i++) {
            print.append(formatter.format(vector[i]) + "\t\t");
            print.append("\n");
        }
        System.out.println(print.toString());
    }

    public static void printMatrixDouble(double[][] matrix, String additionalInfo) {
        System.out.println(additionalInfo);
        System.out.println(matrixToString(matrix));

    }

    /**
     * Method to create a String for an 2-dim. array/matrix.
     *
     * @param matrix double[][]
     * @return String that represents the 2-dim. array/matrix.
     */
    public static String matrixToString(double[][] matrix) {
        NumberFormat formatter = new DecimalFormat("#.########");
        StringBuilder print = new StringBuilder();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) { // i=j not good for printing
                print.append(formatter.format(matrix[i][j]) + "\t\t");
            }
            print.append("\n");
        }
        return print.toString();
    }

    /**
     * Method to transpose a given Matrix.
     *
     * @param matrix double [][]  2-dim. array/matrix
     * @return double[][] transposed 2-dim. array/matrix
     */
    public static double[][] transposeMatrix(double[][] matrix) {
        double[][] tmp = new double[matrix.length][matrix[1].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                tmp[j][i] = matrix[i][j];
            }
        }
        return tmp;
    }

    /**
     * Method to multiply two 2-dim. arrays (Matrices) of the same size.
     *
     * @param matrix1 double [][]  2-dim. array/matrix
     * @param matrix2 double [][]  2-dim. array/matrix, same size as matrix1
     * @return double[][] transposed 2-dim. array/matrix
     */
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

    /**
     * Method to multiply a 2-dim. array/matrix with an 1-dim. array/vector
     *
     * @param matrix double[][] 2-dim. array/matrix
     * @param vector double[] 1-dim. array/vector
     * @return vector double[] 1-dim. array/vector
     */
    public static double[] multiplyMatrixVector(double[][] matrix, double[] vector) {
        double[] updatedVec = new double[matrix.length];
        for (int row = 0; row < matrix.length; row++) {
            double sum = 0;
            for (int column = 0; column < matrix[0].length; column++) {
                double adjWeight = matrix[row][column];
                double scr = vector[column];
                sum += matrix[row][column] * (vector[column]);
            }
            updatedVec[row] = sum;
        }
        return updatedVec;
    }
}
