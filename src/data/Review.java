package data;

import java.util.Random;

/**
 * Class to represent an Amazon Review.
 *
 * @author R. Scholz
 * @author N.Seemann
 */
public class Review {
    private static int runningNumber = 0;
    private static int runningNumber2 = 10;
    private boolean evalReview;
    private int id;
    private String text;
    private double realRating;
    private double predictedRating;
    private boolean isKnown;
    private double normalizedRating;

    /**
     * Method check if the Review is one of the evaluation Reviews.
     *
     * @return bool if Review is used for evaluation.
     */
    public boolean isEvalReview() {
        return evalReview;
    }

    /**
     * Method to set the Review as one of the evaluation Reviews.
     */
    public void setEvalReview(boolean evalReview) {
        this.evalReview = evalReview;
    }

    /**
     * Method to obtain the normalized rating. Used by HITS.
     *
     * @return double normalized review rating
     */
    public double getNormalizedRating() {
        return normalizedRating;
    }

    /**
     * Method to set the nuber of known labels.
     *
     * @author R.Scholz
     */
    public static void addKnownPercentage(int percentageIncrease, Review[] reviews) throws Exception {
        int tries = 200000;
        int increase = (int) (reviews.length * percentageIncrease / 100.0);
        int increased = 0;
        Random rnd = new Random(2019 * runningNumber2++);
        int i;
        while (increased < increase && tries > 0) {
            i = (int) (rnd.nextDouble() * reviews.length);
            tries--;
            if (!reviews[i].isKnown() && !reviews[i].isEvalReview()) {
                reviews[i].setKnown(true);
                increased++;
                tries = 200000;
            }
        }
        if (tries <= 0) {
            throw new Exception("Could not add known percentage");
        }
    }

    /**
     * Method to get the predicted label (rating) of a review.
     *
     * @return double predicted rating
     */
    public double getPredictedRating() {
        return predictedRating;
    }

    /**
     * Method to set the predicted label (rating) of a review.
     *
     * @param predictedRating double predicted rating
     */
    public void setPredictedRating(double predictedRating) {
        this.predictedRating = predictedRating;
    }

    /**
     * Constructor.
     *
     * @param text       review text
     * @param realRating actutal rating of a review
     * @param isKnown    should the label for this review be predicted or not
     */

    public Review(String text, double realRating, boolean isKnown) {
        this.text = text;
        this.id = runningNumber++;
        this.realRating = realRating;
        this.isKnown = isKnown;
        this.normalizedRating = realRating / 5;
        this.predictedRating = 0;
    }

    public int getId() {
        return id;
    }

    public boolean isKnown() {
        return isKnown;
    }

    public void setKnown(boolean known) {
        isKnown = known;
    }

    public double getRealRating() {
        return realRating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;

    }

    public static void printReviews(Review[] reviews) {
        for (Review r : reviews) {
            System.out.println(r);
        }
    }

    @Override
    public String toString() {
        if (text.length() < 40) {
            return "Review{" +
                    "id=" + id +
                    ", text='" + text + "[...]" + '\'' +
                    ", realRating=" + realRating +
                    ", predictedRating=" + predictedRating +
                    ", isKnown=" + isKnown +
                    '}';
        }
        return "Review{" +
                "id=" + id +
                ", text='" + text.substring(0, 40) + "[...]" + '\'' +
                ", realRating=" + realRating +
                ", predictedRating=" + predictedRating +
                ", isKnown=" + isKnown +
                '}';
    }
}
