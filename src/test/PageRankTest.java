package test;

import LinkAnalysis.PageRank;
import data.Review;
import io.MatrixUtils;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class PageRankTest {

    @Test
    public void testPerformCalculations() {
        double[][] weights = new double[4][4];
        weights[0][0] = 0;
        weights[0][1] = 1/3.0;
        weights[0][2] = 1/3.0;
        weights[0][3] = 1/3.0;

        weights[1][0] = 0;
        weights[1][1] = 0;
        weights[1][2] = 0.5;
        weights[1][3] = 0.5;

        weights[2][0] = 1;
        weights[2][1] = 0;
        weights[2][2] = 0;
        weights[2][3] = 0;

        weights[3][0] = 0.5;
        weights[3][1] = 0;
        weights[3][2] = 0.5;
        weights[3][3] = 0;

        weights =  MatrixUtils.transposeMatrix(weights);

        Review[] reviews = new Review[4];

        for (int i = 0; i < reviews.length; i++) {
            reviews[i] = new Review("a", 0, false);
        }
        double[] rank = PageRank.performCalculations(reviews, weights, 0.25);

        assertEquals(rank[0],0.38,0.1);
        assertEquals(rank[1],0.12,0.1);
        assertEquals(rank[2],0.29,0.1);
        assertEquals(rank[3],0.19,0.1);
    }
}