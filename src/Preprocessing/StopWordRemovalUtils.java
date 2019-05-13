package Preprocessing;

import data.Review;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Class offering operations to remove stop words from a set of reviews
 *
 * @author Dennis
 */
public class StopWordRemovalUtils {

    private static StopWordRemovalUtils instance;
    private List<String> stopWordList;
    private Review[] reviews;

    private StopWordRemovalUtils() {

    }


    /**
     * Static method returning a "cleaned" list of reviews
     *
     * @param reviewString set of reviews
     * @return set of reviews without stop words
     */
    public static Review[] removeStopWords(Review[] reviewString) {

        if (StopWordRemovalUtils.instance == null) {
            StopWordRemovalUtils.instance = new StopWordRemovalUtils();
            instance.initializeStopWordList(reviewString);

        }


        return instance.remove(reviewString);

    }

    /**
     * Method initializing the stopWordList
     *
     * @param reviewString set of reviews
     */
    public void initializeStopWordList(Review[] reviewString) {
        stopWordList = new ArrayList<>();


        try (BufferedReader br = new BufferedReader(new FileReader("data/stopwords_en.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                stopWordList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Method to remove the stopWords + punctuation marks
     *
     * @param reviewString set of reviews
     * @return "cleaned" reviews
     */
    public Review[] remove(Review[] reviewString) {

        reviews = reviewString;

        for (int i = 0; i < reviews.length; i++) {
            String temp = reviews[i].getText().toLowerCase();
            //  System.out.println("ALT  :" + temp);
            for (int j = 0; j < stopWordList.size(); j++) {
                if (temp.contains(stopWordList.get(j))) {
                    String regex = "\\s*\\b" + stopWordList.get(j) + "\\b\\s*";
                    temp = temp.replaceAll(regex, " ");
                }
            }
            temp = temp.replaceAll("\\W+|\\s+", " ");
            temp = temp.trim();
            reviewString[i].setText(temp);

            //     System.out.println("NEU  :" + reviews[i].getText());

        }
        return reviews;


    }

}
