package LinkAnalysis;

public class HITS_Score {


    private double score;

    public HITS_Score() {
        score = 1.0;
    }
    public HITS_Score(double rating) {
        score = rating;
    }

    public double getScore() {
        return score;
    }
    public void updateScores(double updatedScore) {
        score = updatedScore;
    }
    
    public int compareTo(HITS_Score otherScore) {
        if (this.score > otherScore.score) {
            return 1;
        }
        if (this.score < otherScore.score) {
            return -1;
        }
        return 0;
    }

        @Override
    public String toString() {
        return "HITS Score: " + score;
    }
}
