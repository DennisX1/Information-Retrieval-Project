public class Review {
    private static int runningNumber = 0;
    private int id;
    private String text;
    private double label;


    Review(String text) {
        this.text = text;
        this.id = runningNumber++;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Review number " + id + "with the text " + text.substring(0, 40) + "[...]";
    }
}