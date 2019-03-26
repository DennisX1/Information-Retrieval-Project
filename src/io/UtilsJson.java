package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import Preprocessing.Stemmer;
import Preprocessing.StopWordRemovalUtils;
import SimMeasuresUtils.TFIDFUtils;
import data.Review;


import org.json.JSONObject;


public class UtilsJson {

    /**
     * Gets the Reviews from the files.
     *
     * @param dataset         which should be loaded
     * @param amount          of reviews that should be loaded (can be only the maximum of the dataset)
     * @param percentageKnown
     * @return
     */
    public static Review[] getReviewsFromDataset(int amount, int percentageKnown, Dataset dataset) throws Exception {
        // Get all data first
        ArrayList<Review> reviews = readJSON(dataset);
        // Random with seeding so results stay consistent with the same amount but varies for different amounts
        Random rnd = new Random(314159265359l * amount);
        if (amount > reviews.size()) {
            throw new Exception("You chose to get an amount of " + amount + " while the dataset is only of the size " + reviews.size());
        }
        // Shuffle the data to get a good average
        Collections.shuffle(reviews, rnd);
        Review[] result = new Review[amount];
        ArrayList<Boolean> isKnown = new ArrayList<>();
        // produce an List with the specified percentage of known and unknown booleans
        for (int i = 0; i < amount; i++) {
            isKnown.add(i < amount * percentageKnown / 100.0);
        }
        // Shuffle it and give the reviews the data
        Collections.shuffle(isKnown);
        for (int i = 0; i < amount; i++) {
            result[i] = reviews.get(i);
            result[i].setKnown(isKnown.get(i));
        }
        return result;
    }


    /**
     * Loads the complete specified datatset into Review objects.
     *
     * @param dataset
     * @return
     * @throws IOException
     */
    private static ArrayList<Review> readJSON(Dataset dataset) throws IOException {
        //No performance issues here so no we use arraylist over a set to keep the ordering consistent
        ArrayList<Review> reviews = new ArrayList<>();
        JSONObject json;
        String text;
        int rating;
        BufferedReader br = new BufferedReader(new FileReader(dataset.path));
        String line = br.readLine();
        while (line != null) {
            json = new JSONObject(line);
            text = json.getString("reviewText");
            rating = (int) json.getDouble("overall");
            if (text != null) {
                reviews.add(new Review(text, rating, true));
            }
            line = br.readLine();
        }
        br.close();
        return reviews;
    }

    public static Review[] readJSONLimit(int limit) throws IOException {
        //No performance issues here so no we use arraylist over a set to keep the ordering consistent
        int i = 0;
        ArrayList<Review> reviews = new ArrayList<>();
        JSONObject json;
        String text;
        int rating;
        Boolean known = true;
        BufferedReader br = new BufferedReader(new FileReader("data/Amazon_Instant_Video_5.json"));
        String line = br.readLine();


        while (line != null && i < limit) {
            json = new JSONObject(line);
            text = json.optString("reviewText", null);
            rating = (int) json.getDouble("overall");
            if (text != null) {
                reviews.add(new Review(text, rating, true));
            }
            line = br.readLine();
            i++;
        }
        br.close();


        return reviews.toArray(new Review[0]);

    }

    public static void main(String[] args) {
        try {
            Review[] reviews = getReviewsFromDataset(100, 50, Dataset.AMAZON_INSTANT_VIDEO);
            //printReviews(reviews);
            System.out.println("NORMAL HIER");
            for (int i = 0; i < reviews.length; i++) {
                System.out.println(reviews[i].getText());
            }

            Review[] clean = StopWordRemovalUtils.removeStopWords(reviews);

            System.out.println("STOP WORD HIER");
            for (int i = 0; i < clean.length; i++) {
                System.out.println(clean[i].getText());
            }


            Review[] stemmed = Stemmer.stemWord(clean);


            System.out.println("STEMMING HIER");
            for (int i = 0; i < stemmed.length; i++) {
                System.out.println(stemmed[i].getText());
            }
              TFIDFUtils.computeSimilarities(stemmed);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printReviews(Review[] reviews) {
        for (Review r : reviews) {
            System.out.println(r);
        }
    }

    public enum Dataset {
        AMAZON_INSTANT_VIDEO("data/Amazon_Instant_Video_5.json");

        private String path;

        Dataset(String path) {
            this.path = path;
        }
    }
}
