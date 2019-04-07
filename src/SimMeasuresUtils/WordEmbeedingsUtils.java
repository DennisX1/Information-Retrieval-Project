package SimMeasuresUtils;

import Preprocessing.StopWordRemovalUtils;
import data.Review;
import io.UtilsJson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static io.UtilsJson.getReviewsFromDataset;

public class WordEmbeedingsUtils {


    private HashMap<String, double[]> denseVectorforWords;
    private static WordEmbeedingsUtils instance;

    public static double[][] calculateSimWordEmbeedingsUtils(Review[] reviews, List<String> stopWordList) {

        if (instance == null) {
            instance = new WordEmbeedingsUtils();
            instance.loadData(stopWordList);
        }


        return null;


    }


    private void loadData(List<String> stopWordList) {
        denseVectorforWords = new HashMap<>();
        int tempCounter = 1;

        try (BufferedReader br = new BufferedReader(new FileReader("data/wiki-news-300d-1M.vec"))) {
            String line;
            while ((line = br.readLine()) != null) {

                String[] tokens = line.split(" ");
                String temp = tokens[0];

                if (!stopWordList.contains(temp)) {
                    double[] vectorValues = new double[tokens.length - 1];

                    for (int i = 1; i < tokens.length - 1; i++) {
                        vectorValues[i] = Double.parseDouble(tokens[i]);
                    }
                    System.out.println(tempCounter);
                    tempCounter++;

                    denseVectorforWords.put(temp, vectorValues);

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("DONEEEEEEEE");
    }


    private void calculateEmbeedings() {


    }

    private double[][] calculateCosineSim() {

        return null;

    }


    public static void main(String[] args) {

        try {
            Review[] reviews = getReviewsFromDataset(3, 50, UtilsJson.Dataset.AMAZON_INSTANT_VIDEO);
            Review[] clean = StopWordRemovalUtils.removeStopWords(reviews);
            List<String> stopWordList = StopWordRemovalUtils.getInstance().getStopWordList();
            WordEmbeedingsUtils wordEmbeedingsUtils = new WordEmbeedingsUtils();
            wordEmbeedingsUtils.loadData(stopWordList);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
