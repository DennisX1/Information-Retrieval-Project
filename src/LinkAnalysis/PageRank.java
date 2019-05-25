package LinkAnalysis;

import data.Review;
import io.UtilsJson;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;

/**
 * Class the represents the PageRank Algorithm.
 *
 * @author R.Scholz
 */
public class PageRank {
    public static final double EPSILON = 0.001;
    public static final int MAX_ITERATIONS = 200;

    private static double[][] normalizeWeights(double[][] weights) {
        double[] sum = new double[weights[0].length];
        for (int y = 0; y < weights[0].length; y++) {
            sum[y] = 0;
            for (int x = 0; x < weights.length; x++) {
                if (x == y) {
                    continue;
                }
                sum[y] = sum[y] + weights[x][y];
            }
        }
        double[][] ret = new double[weights.length][weights[0].length];
        for (int x = 0; x < weights.length; x++) {
            for (int y = 0; y < weights[x].length; y++) {
                if (x == y) {
                    ret[x][y] = 0;
                    continue;
                }
                if (sum[y] == 0 || weights[x][y] == 0) {
                    ret[x][y] = 0;
                } else {
                    ret[x][y] = weights[x][y] / sum[y];
                }
            }
        }
        return ret;
    }

    public static double[] performCalculations(Review[] reviews, double[][] weights) throws OutOfMemoryError {
        return performCalculations(reviews, weights, 3.0);
    }

    public static double[] performCalculations(Review[] reviews, double[][] weights, double initialValue) throws OutOfMemoryError {
        weights = normalizeWeights(weights);
        double[] rankNew = new double[reviews.length];
        double[] rank;
        int counter = 0;
        for (int i = 0; i < rankNew.length; i++) {
            rankNew[i] = initialValue;
        }
        do {
            rank = rankNew;
            rankNew = UtilsJson.vectorMatrixMult(rank, weights);
            setKnownReviews(rankNew, reviews);

        } while (counter++ < MAX_ITERATIONS && !PageRank.isConverged(rank, rankNew, EPSILON));
        for (int i = 0; i < rankNew.length; i++) {
            reviews[i].setPredictedRating(rankNew[i]);
        }
        return rankNew;
    }

    private static void setKnownReviews(double[] rank, Review[] reviews) {
        for (int i = 0; i < reviews.length; i++) {
            if (reviews[i].isKnown()) {
                rank[i] = reviews[i].getRealRating();
            }
        }
    }

    private static void printRank(double[] rank) {
        System.out.print("\nRanks: [");
        DecimalFormat df = new DecimalFormat("#.##");

        for (int i = 0; i < rank.length; i++) {
            System.out.print(df.format(rank[i]));
            if (i < rank.length - 1) System.out.print("|");
        }
        System.out.print("]");
    }

    public static boolean isConverged(double[] oldRank, double[] newRank, double epsilon) {
        for (int i = 0; i < oldRank.length; i++) {
            if (Math.abs(oldRank[i] - newRank[i]) > epsilon) {
                return false;
            }
        }
        return true;
    }
}


