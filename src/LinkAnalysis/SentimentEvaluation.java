package LinkAnalysis;

import data.Review;


public class SentimentEvaluation {
    private Review[] allReviews;
    private double sqrMeanError;

    public SentimentEvaluation(Review[] reviews){
        allReviews= reviews;
        sqrMeanError=0.0;
    }

    public void createEvaluationSME(){
        int counter =0;
        for (Review rev : allReviews) {
            if(!rev.isKnown()){
                counter++;
                sqrMeanError += Math.pow ((rev.getNormalizedRating() - rev.getPredictedRating()), 2);

            }
        }
         sqrMeanError= sqrMeanError/counter;
    }

    public void printSME(String addiionalText){
        System.out.println(addiionalText + "Squared Mean Error is: " + sqrMeanError);
    }


}
