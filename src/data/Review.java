package data;

import java.util.Random;

public class Review {
    private static int runningNumber = 0;
    private static int runningNumber2 = 0;

    private boolean evalReview;
    private int id;
    private String text;
    private double realRating;
    private double predictedRating;
    private boolean isKnown;
    private double normalizedRating;


    public boolean isEvalReview() {
        return evalReview;
    }

    public void setEvalReview(boolean evalReview) {
        this.evalReview = evalReview;
    }

    public double getNormalizedRating() {
        return normalizedRating;
    }

    public static void addKnownPercentage(int percentageIncrease, Review[] reviews) throws Exception {
        int tries = 200000;
        int increase = (int) (reviews.length * percentageIncrease / 100.0);
        int increased = 0;
        Random rnd = new Random(runningNumber + runningNumber2++);
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

    public double getPredictedRating() {
        return predictedRating;
    }

    public void setPredictedRating(double predictedRating) {
        this.predictedRating = predictedRating;
    }


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
