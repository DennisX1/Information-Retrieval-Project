package data;

public class Review {
    private static int runningNumber = 0;
    private int id;
    private String text;
    private double realRating;
    private double predictedRating;
    private boolean isKnown;
    private double normalizedRating;

    public double getNormalizedRating() {
        return normalizedRating;
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
