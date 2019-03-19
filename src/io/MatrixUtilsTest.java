package io;

import org.junit.Test;

import static org.junit.Assert.*;

public class MatrixUtilsTest {

    @Test
    public void matrixMultiplicationSameSizeTest() {

    }

    // TODO @Nadja
    public void multiplyMatrixVectorWeighted() {
    }

    @Test
    public void multiplyMatrixVectorTest() {
        double[] a = new double[3];
        double[][] b = new double[2][3];
        a[0] = 1;
        a[1] = 2;
        a[2] = 5;

        b[0][0] = 1;
        b[1][0] = 2;
        b[0][1] = 3;
        b[1][1] = 4;
        b[0][2] = 5;
        b[1][2] = 6;

        double[] c = MatrixUtils.multiplyMatrixVector(b, a);
        assertEquals(32, c[0], 0.001);
        assertEquals(40, c[1], 0.001);
    }
}