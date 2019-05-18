package test;

import data.Review;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class ReviewTest {

    @Test
    public void testAddKnownPercentage() {
        Review[] reviews = new Review[10];
        for (int i = 0; i < reviews.length; i++) {
            reviews[i] = new Review("", 1, false);
        }

        reviews[0].setKnown(false);
        reviews[0].setEvalReview(true);
        reviews[1].setKnown(true);
        reviews[1].setEvalReview(false);
        reviews[2].setKnown(false);
        reviews[2].setEvalReview(false);
        reviews[3].setKnown(false);
        reviews[3].setEvalReview(false);
        reviews[4].setKnown(false);
        reviews[4].setEvalReview(false);
        reviews[5].setKnown(false);
        reviews[5].setEvalReview(false);
        reviews[6].setKnown(false);
        reviews[6].setEvalReview(false);
        reviews[7].setKnown(false);
        reviews[7].setEvalReview(false);
        reviews[8].setKnown(false);
        reviews[8].setEvalReview(false);
        reviews[9].setKnown(false);
        reviews[9].setEvalReview(false);

        int counter = 0;
        for (int i = 0; i < 7; i++) {
            try {
                Review.addKnownPercentage(10, reviews);
                counter = 0;
                for (Review r : reviews) {
                    if (r.isKnown()) {
                        counter++;
                    }
                    if (r.isKnown() && r.isEvalReview()) {
                        fail(r + " was both known and mark as eval");
                    }
                }
                assertEquals(i + 2, counter);
            } catch (Exception e) {
                fail(e.getMessage());
            }

        }

    }
}
