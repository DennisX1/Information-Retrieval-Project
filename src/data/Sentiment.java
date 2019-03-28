package data;

import LinkAnalysis.HITS_Scores;

import java.util.Map;

public class Sentiment {
    private double predicetedLabel;
    private Map<Integer, HITS_Scores> originScores;

    public Sentiment(double label, Map<Integer, HITS_Scores> topK){
        predicetedLabel = label;
        originScores = topK;

    }
}
