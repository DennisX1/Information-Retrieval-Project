package LinkAnalysis;

public class HITS_Scores {
    private double authorityScore;
    private double hubScore;

    public HITS_Scores() {
        hubScore = 1.0;
        authorityScore = 1.0;
    }

    public void updateScores(double auth, double hub) {
        authorityScore = auth;
        hubScore = hub;
    }

    public double getHubScore() {
        return hubScore;
    }


    public int compareTo(HITS_Scores otherScore) {
        if (this.authorityScore > otherScore.authorityScore) {
            return 1;
        }
        if (this.authorityScore < otherScore.authorityScore) {
            return -1;
        }
        return 0;
    }

        @Override
    public String toString() {
        return "HITS Scores  Auth: " + authorityScore + " Hub: " + hubScore;
    }
}
