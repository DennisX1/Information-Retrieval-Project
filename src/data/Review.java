package data;

public class Review {
    private static int runningNumber = 0;
    private int id;
    private String text;
    private double realRating;
    private double predictedRating;
    private boolean isKnown;

    public Review(String text, double realRating, boolean isKnown) {
        this.text = text;
        this.id = runningNumber++;
        this.realRating = realRating;
        this.isKnown = isKnown;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", text='" + text.substring(0, 40) + "[...]" + '\'' +
                ", realRating=" + realRating +
                ", predictedRating=" + predictedRating +
                ", isKnown=" + isKnown +
                '}';
    }
}
