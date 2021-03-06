package LinkAnalysis;

import data.Review;

/**
 * Class to evaluate the predication by the two Link Analysis Algorithms.
 *
 * @author R.Scholz
 */
public class Evaluation {

    /**
     * Calculate the mean absolute error
     *
     * @param reviews
     */
    public static double calculateMAE(Review[] reviews) {
        int counter = 0;
        double sum = 0;
        for (Review r : reviews) {
            if (!r.isEvalReview()) {
                continue;
            } else {
                counter++;
                sum = sum + Math.abs(r.getRealRating() - r.getPredictedRating());
            }
        }
        sum = sum / counter;
        return sum;
    }

    public static double calculateMSE(Review[] reviews) {
        double meanSqrError = 0;
        int counter = 0;
        for (Review rev : reviews) {
            if (rev.isEvalReview()) {
                counter++;
                meanSqrError += Math.pow((rev.getRealRating() - rev.getPredictedRating()), 2);
            }
        }
        return meanSqrError / counter;
    }


    public static double calculatePCC(Review[] reviews) {
        // X = predicted rating
        // Y = real rating
        double xAvg = 0, yAvg = 0;
        int counter = 0;
        double top = 0;
        double bottomLeft = 0, bottomRight = 0;
        for (Review r : reviews) {
            if (!r.isEvalReview()) {
                continue;
            } else {
                counter++;
                xAvg = xAvg + r.getPredictedRating();
                yAvg = yAvg + r.getRealRating();
            }
        }
        xAvg = xAvg / counter;
        yAvg = yAvg / counter;
        for (Review r : reviews) {
            if (!r.isEvalReview()) {
                continue;
            } else {
                top = top + (xAvg - r.getPredictedRating()) * (yAvg - r.getRealRating());
                bottomLeft = bottomLeft + Math.pow(xAvg - r.getPredictedRating(), 2);
                bottomRight = bottomRight + Math.pow(yAvg - r.getRealRating(), 2);
            }
        }
        return top / (Math.sqrt(bottomLeft) * Math.sqrt(bottomRight));
    }

    public static void calcAndPrintMAE(Review[] reviews) {
        System.out.println("\nMean absolute error is: " + calculateMAE(reviews));
    }

    public static void calcuAndPrintPCC(Review[] reviews) {
        System.out.println("\nPearson correlation coefficient: " + calculatePCC(reviews));
    }

    public static void calcAndPrintMSE(Review[] reviews) {
        System.out.println("Mean square error is: " + calculateMSE(reviews));
    }







    //*****Further Evaluations
    //****Regular Calculation of Evaluation not considering the the same reviews
    public static double calculateMSE_standard(Review[] reviews) {
        double meanSqrError = 0;
        int counter = 0;
        for (Review rev : reviews) {
            if (!rev.isKnown()) {
                counter++;
                meanSqrError += Math.pow((rev.getRealRating() - rev.getPredictedRating()), 2);

            }
        }
        return meanSqrError / counter;
    }

    /**
     * Calculate the mean absolute error
     *
     * @param reviews
     */
    public static double calculateMAE_standard(Review[] reviews) {
        int counter = 0;
        double sum = 0;
        for (Review r : reviews) {
            if (r.isKnown()) {
                continue;
            } else {
                counter++;
                sum = sum + Math.abs(r.getRealRating() - r.getPredictedRating());
            }
        }
        sum = sum / counter;
        return sum;
    }

    public static double calculatePCC_standard(Review[] reviews) {
        // X = predicted rating
        // Y = real rating
        double xAvg = 0, yAvg = 0;
        int counter = 0;
        double top = 0;
        double bottomLeft = 0, bottomRight = 0;
        for (Review r : reviews) {
            if (r.isKnown()) {
                continue;
            } else {
                counter++;
                xAvg = xAvg + r.getPredictedRating();
                yAvg = yAvg + r.getRealRating();
            }
        }
        xAvg = xAvg / counter;
        yAvg = yAvg / counter;
        for (Review r : reviews) {
            if (r.isKnown()) {
                continue;
            } else {
                top = top + (xAvg - r.getPredictedRating()) * (yAvg - r.getRealRating());
                bottomLeft = bottomLeft + Math.pow(xAvg - r.getPredictedRating(), 2);
                bottomRight = bottomRight + Math.pow(yAvg - r.getRealRating(), 2);
            }
        }
        return top / (Math.sqrt(bottomLeft) * Math.sqrt(bottomRight));
    }

    public static void calcAndPrintMAE_standard(Review[] reviews) {
        System.out.println("\nMean absolute error (STD) is: " + calculateMAE(reviews));
    }

    public static void calculateAndPrintPCC_standard(Review[] reviews) {
        System.out.println("\nPearson correlation coefficient (STD): " + calculatePCC(reviews));
    }

    public static void calcAndPrintMSE_standard(Review[] reviews) {
        System.out.println("Mean square error (STD) is: " + calculateMSE(reviews));
    }
}

