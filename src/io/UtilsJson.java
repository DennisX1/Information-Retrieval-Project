package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

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
            //   Review[] reviews = getReviewsFromDataset(120, 50, Dataset.AMAZON_INSTANT_VIDEO);
            //     printReviews(reviews);

        } catch (Exception e) {
            e.printStackTrace();
        }
        double[][] a = new double[3][1];
        double[][] b = new double[2][3];
        a[0][0] = 1;
        a[1][0] = 4;
        a[2][0] = 6;

        b[0][0] = 1;
        b[1][0] = 2;
        b[0][1] = 3;
        b[1][1] = 4;
        b[0][2] = 5;
        b[1][2] = 6;

        double[][] c = matrixMult(a, b);
        System.out.println("[" + c[0][0] + "," + c[1][0] + "]");
        System.out.println(c.length);
    }

    public enum Dataset {
        AMAZON_INSTANT_VIDEO("data/Amazon_Instant_Video_5.json");

        private String path;

        Dataset(String path) {
            this.path = path;
        }
    }

    public static double[][] matrixMult(double[][] a, double[][] b) {
        double[][] c = new double[b.length][a[0].length];

        for (int i = 0; i < c.length; i++) {
            for (int j = 0; j < c[0].length; j++) {
                c[i][j] = 0;
            }
        }

        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                for (int k = 0; k < b[0].length; k++) {
                    c[i][j] += b[i][k] * a[k][j];
                }
            }
        }
        return c;
    }

    public static double[] vectorMatrixMult(double[] a, double[][] b) {
        double[] c = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            c[i] = 0;
            for (int j = 0; j < b[0].length; j++) {
                c[i] = c[i] + a[j] * b[i][j];
            }
        }
        return c;
    }


}
