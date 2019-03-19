package LinkAnalysis;

import data.Review;
import io.UtilsJson;

import java.text.DecimalFormat;
import java.util.Random;

public class PageRank {
    public static final double EPSILON = 0.001;
    public static final int MAX_ITERATIONS = 1000;


    public static void main(String[] args) {
        try {
            int amount = 50;
            Review[] dataset = UtilsJson.getReviewsFromDataset(amount, 7, UtilsJson.Dataset.AMAZON_INSTANT_VIDEO);
            Random rnd = new Random(123);
            double[][] weights = new double[amount][amount];
            double[] sum = new double[weights.length];
            for (int i = 0; i < weights.length; i++) {
                for (int j = 0; j < weights[i].length; j++) {
                    weights[i][j] = rnd.nextDouble();
                    sum[i] = sum[i] + weights[i][j];
                }
            }

            for (int i = 0; i < weights.length; i++) {
                for (int j = 0; j < weights[i].length; j++) {
                    weights[i][j] = weights[i][j] / sum[i];
                    weights[i][i] = 0;
                }
            }


            performCalculations(dataset, weights);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double[] performCalculations(Review[] reviews, double[][] weights) {
        double[] rankNew = new double[reviews.length];
        double[] rank;
        int counter = 0;
        //init all ranks with (1+2+3+4+5)/5=3
        for (int i = 0; i < rankNew.length; i++) {
            rankNew[i] = 3;
        }
        do {
            rank = rankNew;
            rankNew = UtilsJson.vectorMatrixMult(rank, weights);
            System.out.println("\nIteration " + counter++);
            setKnownReviews(rankNew, reviews);
            printRank(rankNew);

        } while (counter < MAX_ITERATIONS && !UtilsJson.isConverged(rank, rankNew, EPSILON));
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
        System.out.println("Ranks: [");
        DecimalFormat df = new DecimalFormat("#.##");

        for (int i = 0; i < rank.length; i++) {
            System.out.print(df.format(rank[i])+"|");
        }
        System.out.print("]");
    }

}


