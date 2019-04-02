package data;

import LinkAnalysis.HITS_Score;

import java.util.Map;

public class Sentiment {
    private double predicetedLabel;
    private Map<Integer, HITS_Score> originScores;

    public Sentiment(double label, Map<Integer, HITS_Score> topK){
        predicetedLabel = label;
        originScores = topK;

    }
}
