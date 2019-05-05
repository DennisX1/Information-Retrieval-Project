package test;

import LinkAnalysis.Evaluation;
import LinkAnalysis.HITS;
import data.Review;
import data.ReviewGraph;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EvaluationTest {

    @Test
    public void testCalculateMAE() {
        Review[] reviews = new Review[6];
        reviews[0] = new Review("a", 1.0, false);
        reviews[0].setPredictedRating(2.2);
        reviews[1] = new Review("a", 3.0, false);
        reviews[1].setPredictedRating(1.2);
        reviews[2] = new Review("a", 1.9, false);
        reviews[2].setPredictedRating(1.9);
        reviews[3] = new Review("a", 5.0, false);
        reviews[3].setPredictedRating(1.1);
        reviews[4] = new Review("a", 3, false);
        reviews[4].setPredictedRating(4);
        reviews[5] = new Review("a", 1.0, true);
        reviews[5].setPredictedRating(222112.2);

        assertEquals(1.58, Evaluation.calculateMAE(reviews), 0.0001);


    }

    @Test
    public void testCalculatePCC() {
        Review[] reviews = new Review[7];
        reviews[0] = new Review("a", 2.0, false);
        reviews[0].setPredictedRating(1);
        reviews[1] = new Review("a", 3.0, false);
        reviews[1].setPredictedRating(2);
        reviews[2] = new Review("a", 2.0, false);
        reviews[2].setPredictedRating(3);
        reviews[3] = new Review("a", 3.0, false);
        reviews[3].setPredictedRating(4);
        reviews[4] = new Review("a", 3.5, false);
        reviews[4].setPredictedRating(4.5);
        reviews[5] = new Review("a", 4.5, false);
        reviews[5].setPredictedRating(5);
        reviews[6] = new Review("a", 40.5, true);
        reviews[6].setPredictedRating(-5);

        assertEquals(0.7866, Evaluation.calculatePCC(reviews), 0.0001);
    }

}

