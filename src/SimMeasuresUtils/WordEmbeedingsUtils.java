package SimMeasuresUtils;

import Preprocessing.StopWordRemovalUtils;
import data.Review;
import io.UtilsJson;
import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import static io.UtilsJson.getReviewsFromDataset;

/**
 * Class offering operation to calculate
 * @author Dennis
 */

public class WordEmbeedingsUtils {


    private HashMap<String, double[]> denseVectorForWords;
    private static WordEmbeedingsUtils instance;
    private HashMap<Integer, double[]> denseVectorForDocument;
    private double[][] cosineArray;

    public static double[][] calculateSimWordEmbeedingsUtils(Review[] reviews, List<String> stopWordList) {

        if (instance == null) {
            instance = new WordEmbeedingsUtils();
            instance.loadData(stopWordList);
        }

        instance.createDenseVectorForReview(reviews);


        return instance.calculateCosineSimForDense(reviews.length);


    }

    /**
     * Method to reduce the size of the original FastText file
     */
    private void createFile() {
        int counter = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("data/wiki-news-300d-1M.vec"))) {
            File file = new File("data/DenseVector.txt");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            String line;
            br.readLine();
            while ((line = br.readLine()) != null && counter <= 300000) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
                counter++;
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * Method to load the dense vectors from the file
     * Each line of the file consists of a word followed by 300 double values
     * Method reads the file, split it into tokens, and store it into a HashMap<String,[]Double>
     *
     * @param stopWordList list including stop words
     */
    private void loadData(List<String> stopWordList) {

        denseVectorForWords = new HashMap<>();
        int tempCounter = 1;

        try (BufferedReader br = new BufferedReader(new FileReader("data/DenseVector.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {

                String[] tokens = line.split("\\s");
                String temp = tokens[0];

                if (!stopWordList.contains(temp)) {
                    double[] vectorValues = new double[tokens.length - 1];

                    for (int i = 1; i < tokens.length; i++) {
                        vectorValues[i - 1] = Double.parseDouble(tokens[i]);
                    }
                    System.out.println("Creating HashMap Count: " + tempCounter);
                    tempCounter++;

                    denseVectorForWords.put(temp, vectorValues);

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * Method to create the averaged dense vector for each Review
     * @param reviews array containing reviews
     *
     */
    private void createDenseVectorForReview(Review[] reviews) {

        denseVectorForDocument = new HashMap<>();

        //iterate over reviews
        for (int i = 0; i < reviews.length; i++) {
            int counter = 0;
            double[] averagedValues = new double[300];
            String review = reviews[i].getText();
            String[] tokenized = review.split("\\s");



            //iterate over words
            for (int j = 0; j < tokenized.length; j++) {

                if (denseVectorForWords.containsKey(tokenized[j])) {

                    //iterate over averagedValues
                    for (int k = 0; k < averagedValues.length; k++) {
                        double[] wordVector = denseVectorForWords.get(tokenized[j]);
                        averagedValues[k] += wordVector[k];

                    }

                    counter++;

                }


            }
            for (int j = 0; j < averagedValues.length; j++) {
                averagedValues[j] /= counter;

            }
            denseVectorForDocument.put(i, averagedValues);
        }

    }


    /**
     *
     * Method to calculate the Cosine Similarity
     * @param reviewCount counter for the reviews, used to set the size of the cosineArray
     * @return double array representing cosine sim btw. each document
     */
    private double[][] calculateCosineSimForDense(int reviewCount) {

        DecimalFormat df = new DecimalFormat("#.####");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(dfs);

        cosineArray = new double[reviewCount][reviewCount];

        for (int row = 0; row < cosineArray.length; row++) {
            for (int col = 0; col < cosineArray[row].length; col++) {

                if (row == col) {
                    cosineArray[row][col] = 1;
                } else {

                    double numeratorCosine = 0;
                    double denominatorFirst = 0;
                    double denominatorSecond = 0;

                    double[] arrayRow = denseVectorForDocument.get(row);
                    double[] arrayCol = denseVectorForDocument.get(col);

                    for (int i = 0; i < arrayRow.length; i++) {
                        numeratorCosine += arrayRow[i] * arrayCol[i];
                        denominatorFirst += Math.pow(arrayRow[i], 2);
                        denominatorSecond += Math.pow(arrayCol[i], 2);


                    }
                    if ((denominatorFirst != 0 && denominatorSecond != 0)) {
                        cosineArray[row][col] = Double.valueOf(df.format(numeratorCosine / (Math.sqrt(denominatorFirst) * Math.sqrt(denominatorSecond))));

                    } else {

                        cosineArray[row][col] = 0;
                    }

                }


            }

        }

        System.out.println(Arrays.deepToString(cosineArray).replace("], ", "]\n"));


        return cosineArray;

    }


    public static void main(String[] args) {

//        WordEmbeedingsUtils wordEmbeedingsUtils = new WordEmbeedingsUtils();
//        wordEmbeedingsUtils.createFile();

        try {
            Review[] reviews = getReviewsFromDataset(100, 50, UtilsJson.Dataset.AMAZON_INSTANT_VIDEO);
            Review[] clean = StopWordRemovalUtils.removeStopWords(reviews);
            List<String> stopWordList = StopWordRemovalUtils.getInstance().getStopWordList();
            WordEmbeedingsUtils.calculateSimWordEmbeedingsUtils(clean, stopWordList);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
