package LinkAnalysis;

import data.Review;
import io.UtilsJson;

import java.text.DecimalFormat;
import java.util.Random;

public class PageRank {
    public static final double EPSILON = 0.001;
    public static final int MAX_ITERATIONS = 20000;


    public static void main(String[] args) throws Exception {

        Review[] dataset = null;
        double[][] weights = null;
        try {
            int amount = UtilsJson.Dataset.AMAZON_INSTANT_VIDEO.getMaxAmount();
            dataset = UtilsJson.getReviewsFromDataset(amount, 30, UtilsJson.Dataset.AMAZON_INSTANT_VIDEO);
            Random rnd = new Random(1283);
            weights = new double[amount][amount];

            for (int i = 0; i < weights.length; i++) {
                for (int j = 0; j < weights[i].length; j++) {
                    weights[i][j] = rnd.nextDouble();
                }
            }

            normalizeWeights(weights);
            performCalculationsLargeScale(dataset, weights, 3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double[] performCalculationsLargeScale(Review[] reviews, double[][] weights) {
        return performCalculationsLargeScale(reviews, weights, 3.0);
    }

    public static double[] performCalculationsLargeScale(Review[] reviews, double[][] weights, double initialValue) {
        long startTime = System.currentTimeMillis();
        double[] oldRanks;
        double[] newRanks = new double[reviews.length];

        for (int i = 0; i < newRanks.length; i++) {
            if (reviews[i].isKnown()) {
                newRanks[i] = reviews[i].getRealRating();
            } else {
                newRanks[i] = initialValue;
            }
        }

        normalizeWeights(weights);
        double sum;
        int counter = 0;

        do {
            oldRanks = newRanks;
            newRanks = new double[reviews.length];
            for (int i = 0; i < reviews.length; i++) {
                sum = 0;
                for (int j = 0; j < reviews.length; j++) {
                    sum = sum + oldRanks[j] * weights[j][i];
                }
                newRanks[i] = sum;
            }
        } while (counter++ < MAX_ITERATIONS && !isConverged(oldRanks, newRanks, EPSILON));
        long elapsedTime = System.currentTimeMillis() - startTime;
        printTime(elapsedTime);
        printRank(newRanks);
        return newRanks;
    }

    private static void normalizeWeights(double[][] weights) {
        double[] sum = new double[weights.length];

        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                sum[i] = sum[i] + weights[i][j];
            }
        }

        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j] = weights[i][j] / sum[i];
                weights[i][i] = 0;
            }
        }
    }

    public static double[] performCalculations(Review[] reviews, double[][] weights) throws OutOfMemoryError {
        return performCalculations(reviews, weights, 3.0);
    }

    public static double[] performCalculations(Review[] reviews, double[][] weights, double initialValue) throws OutOfMemoryError {
        long startTime = System.currentTimeMillis();

        double[] rankNew = new double[reviews.length];
        double[] rank;
        int counter = 0;
        for (int i = 0; i < rankNew.length; i++) {
            rankNew[i] = initialValue;
        }
        do {
            rank = rankNew;
            rankNew = UtilsJson.vectorMatrixMult(rank, weights);
            System.out.println("\nIteration " + ++counter);
            setKnownReviews(rankNew, reviews);
            printRank(rankNew);

        } while (counter < MAX_ITERATIONS && !PageRank.isConverged(rank, rankNew, EPSILON));
        long elapsedTime = System.currentTimeMillis() - startTime;
        printTime(elapsedTime);
        printRank(rankNew);
        return rankNew;
    }

    private static void printTime(long elapsedTime) {
        double seconds = elapsedTime / 1000.0;
        int s = (int) (seconds % 60);
        int m = (int) ((seconds / 60) % 60);
        int h = (int) ((seconds / (60 * 60)) % 24);
        System.out.println("\nExecution time Page Rank: ");
        System.out.println(String.format("%d:%02d:%02d", h, m, s));
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


