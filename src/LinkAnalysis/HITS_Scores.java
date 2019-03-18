package LinkAnalysis;

public class HITS_Scores {
    private double authorityScore;
    private double hubScore;

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
    }

    private double authorityScore_prevK;
    private double hubScore_prevK;
    public static int ITERATIONS=10;
    public static double THRESHOLD = 0.2;

    public static double getTHRESHOLD() {
        return THRESHOLD;
    }

    public static int getITERATIONS() {
        return ITERATIONS;
    }

    public double getHubScore() {
        return hubScore;
    }

    public void setHubScore(double hubScore) {
        this.hubScore = hubScore;
    }

    public double getAuthorityScore() {
        return authorityScore;
    }

    public void setAuthorityScore(double authorityScore) {
        this.authorityScore = authorityScore;
    }

    public HITS_Scores() {
        hubScore = 1.0;
        authorityScore = 1.0;
        hubScore_prevK = -1.0;
        authorityScore_prevK = -1.0;
    }

}
