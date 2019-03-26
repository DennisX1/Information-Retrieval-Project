package LinkAnalysis;

public class HITS_Scores {
    private double authorityScore;
    private double hubScore;
    public static int ITERATIONS=30;
    public static double THRESHOLD_FACTOR = 2;
    public static double THRESHOLD = 0.00015;

    /*private double authorityScore_prevK;
    private double hubScore_prevK;
    public double getAuthorityScore_prevK() {
        return authorityScore_prevK;
    }

    public void setAuthorityScore_prevK(double authorityScore_prevK) {
        this.authorityScore_prevK = authorityScore_prevK;
    }

    public double getHubScore_prevK() {
        return hubScore_prevK;
    }

    public void setHubScore_prevK(double hubScore_prevK) {
        this.hubScore_prevK = hubScore_prevK;
    }*/

    public HITS_Scores() {
        hubScore = 1.0;
        authorityScore = 1.0;
        //hubScore_prevK = -1.0;
        //authorityScore_prevK = -1.0;
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

    public double getAuthorityScore() {
        return authorityScore;
    }

    @Override
    public String toString() {
        return "HITS Scores  Auth: " + authorityScore + " Hub: " + hubScore;
    }
}
