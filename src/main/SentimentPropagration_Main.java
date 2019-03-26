package main;

import data.Review;
import data.ReviewGraph;
import io.UtilsJson;

public class SentimentPropagration_Main {
    private static final int QUANTITY_REVIEWS= 120;
    private static final int PERCENTAGE_KOWNLABELS = 50;

    public static void main(String[] args) {
        /*** get random dataset **/
        Review[] reviews = new Review[QUANTITY_REVIEWS];
        try {
            reviews = UtilsJson.getReviewsFromDataset(
                    QUANTITY_REVIEWS, PERCENTAGE_KOWNLABELS, UtilsJson.Dataset.AMAZON_INSTANT_VIDEO);

        } catch (Exception e) {
            e.printStackTrace();
        }


        if (reviews == null ){
            System.exit(1);
        }


        /*** create Graph for nodes */
        ReviewGraph graph = new ReviewGraph(reviews);

        /*** run PageRank Algo */

        /*** run HITS ALgo */



    }
}
