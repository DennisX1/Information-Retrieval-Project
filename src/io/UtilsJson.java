package io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import data.Review;

import org.json.JSONObject;

public class UtilsJson {
    private static final String PATH = "";

    public static Review[] readJSON() throws IOException {
        //No performance issues here so no we use arraylist over a set to keep the ordering consistent
        ArrayList<Review> reviews = new ArrayList<>();
        JSONObject json;
        String text;
        Double label;
        Boolean known= true;
        BufferedReader br = new BufferedReader(new FileReader("data/Amazon_Instant_Video_5.json"));
        String line = br.readLine();
        while (line != null ) {
            json = new JSONObject(line);
            text = json.optString("reviewText", null);
            label= json.optDouble("overall", -1);
            if (text != null) {
                reviews.add(new Review(text, label, true));
            }
            line = br.readLine();
        }
        br.close();
        return reviews.toArray(new Review[0]);
    }
    public static Review[] readJSONLimit(int limit) throws IOException {
        //No performance issues here so no we use arraylist over a set to keep the ordering consistent
        int i = 0;
        ArrayList<Review> reviews = new ArrayList<>();
        JSONObject json;
        String text;
        Double label;
        Boolean known= true;
        BufferedReader br = new BufferedReader(new FileReader("data/Amazon_Instant_Video_5.json"));
        String line = br.readLine();


        while (line != null && i < limit ) {
            json = new JSONObject(line);
            text = json.optString("reviewText", null);
            label= json.optDouble("overall", -1);
            if (text != null) {
                reviews.add(new Review(text, label, true));
            }
            line = br.readLine();
            i++;
        }
        br.close();



        return reviews.toArray(new Review[0]);

    }


    public static void main(String[] args) {
        try {
            Review[] reviews = readJSON();
            System.out.println(reviews.length);
            System.out.println(reviews[37120]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
