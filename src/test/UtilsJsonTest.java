package test;

import io.MatrixUtils;
import io.UtilsJson;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class UtilsJsonTest {

    @Test
    public void failTest() {
        // fail();
    }

    @Test
    public void successTest() {
        assertEquals(1, 1);
    }

    @Test
    public void matrixMultTest() {
        double[][] a = new double[3][3];
        double[][] b = new double[3][3];
        a[0][0] = 1;
        a[1][0] = 4;
        a[2][0] = 6;
        a[0][1] = 2;
        a[1][1] = 3;
        a[2][1] = 5;
        a[0][2] = 0;
        a[1][2] = 7;
        a[2][2] = 4;

        b[0][0] = 2;
        b[1][0] = 4;
        b[2][0] = 2;
        b[0][1] = 3;
        b[1][1] = 1;
        b[2][1] = 2;
        b[0][2] = 8;
        b[1][2] = 0;
        b[2][2] = 9;

        // (1 4 6)   (2 4 2)   (62 8  64)
        // (2 3 5) * (3 1 2) = (53 11 55)
        // (0 7 4)   (8 0 9)   (53 7  50)

        double[][] c = UtilsJson.matrixMult(a, b);
        assertEquals(62, c[0][0], 0.01);
        assertEquals(8, c[1][0], 0.01);
        assertEquals(64, c[2][0], 0.01);
        assertEquals(53, c[0][1], 0.01);
        assertEquals(11, c[1][1], 0.01);
        assertEquals(55, c[2][1], 0.01);
        assertEquals(53, c[0][2], 0.01);
        assertEquals(7, c[1][2], 0.01);
        assertEquals(50, c[2][2], 0.01);
    }



    @Test
    public void vectorMatrixMult() {
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

        double[] c = UtilsJson.vectorMatrixMult(a, b);
        assertEquals(32, c[0], 0.001);
        assertEquals(40, c[1], 0.001);
    }

}