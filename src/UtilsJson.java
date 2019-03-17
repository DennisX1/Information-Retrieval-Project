

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.json.JSONObject;

public class UtilsJson {
    private static final String PATH = "";

    public static Review[] readJSON() throws IOException {
        //No performance issues here so no we use arraylist over a set to keep the ordering consistent
        ArrayList<Review> reviews = new ArrayList<>();
        JSONObject json;
        String text;
        BufferedReader br = new BufferedReader(new FileReader("data/Amazon_Instant_Video_5.json"));
        String line = br.readLine();
        while (line != null ) {
            json = new JSONObject(line);
            text = json.optString("reviewText", null);
            if (text != null) {
                reviews.add(new Review(text));
            }
            line = br.readLine();
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
