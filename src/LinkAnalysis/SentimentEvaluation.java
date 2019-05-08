package LinkAnalysis;

import data.Review;


public class SentimentEvaluation {
    private Review[] allReviews;
    private double meanSqrError;
    private double absSqrError;
    private double pearsonCoefficient;

    public SentimentEvaluation(Review[] reviews){
        allReviews= reviews;
        meanSqrError =0.0;
        absSqrError =0.0;
        pearsonCoefficient = 0.0;
    }

    public void createEvaluationMSE(){
        int counter =0;
        for (Review rev : allReviews) {
            if(!rev.isKnown()){
                counter++;
                meanSqrError += Math.pow ((rev.getRealRating() - rev.getPredictedRating()), 2);

            }
        }
         meanSqrError = meanSqrError /counter;
    }

    public void printMSE(String additionalText){
        System.out.println(additionalText + "Squared Mean Error is: " + meanSqrError);
    }


}
