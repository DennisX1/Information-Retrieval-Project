package LinkAnalysis;

import data.Review;
import io.UtilsJson;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;

public class PageRank {
    public static final double EPSILON = 0.001;
    public static final int MAX_ITERATIONS = 200;

    private static double[][] normalizeWeights(double[][] weights) {
        double[] sum = new double[weights.length];
        double[][] ret = new double[weights.length][weights[0].length];
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                if (i == j) {
                    continue;
                }
                sum[i] = sum[i] + weights[i][j];
            }
        }

        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                if (i == j) {
                    ret[i][i] = 0;
                    continue;
                }
                ret[i][j] = weights[i][j] / sum[i];
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


