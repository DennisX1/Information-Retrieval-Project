package SimMeasuresUtils;

import data.Review;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Class offering operation to calculate the cosine simarility
 * between documents based on TF-IDF values
 * @author Dennis
 */

public class TFIDFUtils {


    private HashMap<String, Double>[] tfValuesForDocumentsMap;
    private HashMap<String, Integer> termDocumentCountMap;
    private HashMap<String, Double> idfValuesForTermMap;
    private int amountOfDocuments;

    /**
     * Static method to get back the cosine similarity btw a set of reviews
     * based on TF-IDF values
     * @param reviews set of reviews
     * @return double array that represent the cosine sim. btw. each document
     */
    public static double[][] computeSimilarities(Review[] reviews) {


        TFIDFUtils tfidfUtils = new TFIDFUtils();
        return tfidfUtils.calculate(reviews);
    }

//    public static double[][] computeSimilaritiesTEST(String[] reviews) {
//
//
//        TFIDFUtils tfidfUtils = new TFIDFUtils();
//        return tfidfUtils.calculateTesting(reviews);
//    }


    /**
     *
     * Method that combines the necessary steps to calculate the cosine sim
     * between documents based on TF-IDF values
     * @param reviews Array representing the reviews
     * @return double array that represent the cosine sim. btw. each document
     */
    private double[][] calculate(Review[] reviews) {

        int maxValueDocCounter = 0;
        int documentIndex = 0;
        tfValuesForDocumentsMap = new HashMap[reviews.length];
        for (int i = 0; i < reviews.length; i++) {
            tfValuesForDocumentsMap[i] = new HashMap<>();
        }
        termDocumentCountMap = new HashMap<>();
        idfValuesForTermMap = new HashMap<>();
        amountOfDocuments = reviews.length;

        for (int i = 0; i < reviews.length; i++) {
            //split string , count double words  Result  HashMap  "Term"--> Count
            Map<String, Long> wordsAndCounts =
                    Arrays.stream(reviews[i].getText().split("\\s")).
                            collect(Collectors.groupingBy(
                                    Function.identity(),
                                    Collectors.counting()
                            ));        // show-->3 , read -->5 , listen -->10

            for (Map.Entry<String, Long> entry : wordsAndCounts.entrySet()) {
                if (entry.getValue() > maxValueDocCounter) {
                    maxValueDocCounter = Math.toIntExact(entry.getValue());
                }
                if (termDocumentCountMap.containsKey(entry.getKey())) {

                    termDocumentCountMap.put(entry.getKey(), termDocumentCountMap.get(entry.getKey()) + 1);
                } else {
                    termDocumentCountMap.put(entry.getKey(), 1);
                }


            }

            calculateTFValues(documentIndex, maxValueDocCounter, wordsAndCounts);
            maxValueDocCounter = 0;
            documentIndex++;

        }

        calculateIDFValues();

        System.out.println(idfValuesForTermMap);

        return calculateCosineSim(reviews.length);


    }

    /**
     * Method to calculate the TF values
     * @param documentindex represent the actual index of the review array that is currently processed
     * @param maxValueDocCounter value that represents the most frequent word in the review
     * @param wordsAndCounts HashMap<String,Long> represents the frequency of each word in the review
     */

    private void calculateTFValues(int documentindex, int maxValueDocCounter, Map<String, Long> wordsAndCounts) {

        for (Map.Entry<String, Long> entry : wordsAndCounts.entrySet()) {

            tfValuesForDocumentsMap[documentindex].put
                    (entry.getKey(),
                            ((1 + Math.log10((double) entry.getValue())) / (1 + Math.log10((double) maxValueDocCounter))));


        }
        System.out.println("Doc: " + documentindex + "    " + tfValuesForDocumentsMap[documentindex]);
        System.out.println("Doc length" + tfValuesForDocumentsMap[documentindex].size());


    }

    /**
     * Method to calculate the IDF values
     */
    private void calculateIDFValues() {

        for (Map.Entry<String, Integer> entry : termDocumentCountMap.entrySet()) {
            idfValuesForTermMap.put(entry.getKey(), (Math.log10(amountOfDocuments / (double) entry.getValue())));
        }
//        System.out.println( "IDF" +idfValuesForTermMap);
//        System.out.println ("IDF END");

    }


    /**
     * Method to calculate the cosine similarity btw. each document
     * @param arraysize number of reviews to set the size of the cosineArray
     * @return double array that represent the cosine sim. btw. each document
     */
    private double[][] calculateCosineSim(int arraysize) {

        DecimalFormat df = new DecimalFormat("#.####");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(dfs);

        double[][] cosineArray = new double[arraysize][arraysize];


        for (int row = 0; row < cosineArray.length; row++) {
            for (int col = 0; col < cosineArray[row].length; col++) {

                if (row == col) {
                    cosineArray[row][col] = 1;
                } else {


                    ArrayList<Double> rowsList = new ArrayList<>();
                    ArrayList<Double> colsList = new ArrayList<>();

                    double numeratorCosine = 0;
                    double denominatorFirst = 0;
                    double denominatorSecond = 0;


                    for (Map.Entry<String, Double> entry : tfValuesForDocumentsMap[row].entrySet()) {

                        denominatorFirst += Math.pow(entry.getValue() * idfValuesForTermMap.get(entry.getKey()), 2);

                        if (tfValuesForDocumentsMap[col].containsKey(entry.getKey())) {

                            rowsList.add(entry.getValue() * idfValuesForTermMap.get(entry.getKey()));
                            colsList.add(tfValuesForDocumentsMap[col].get(entry.getKey()) * idfValuesForTermMap.get(entry.getKey()));


                        }
                    }

                    for (Map.Entry<String, Double> entry : tfValuesForDocumentsMap[col].entrySet()) {

                        denominatorSecond += Math.pow(entry.getValue() * idfValuesForTermMap.get(entry.getKey()), 2);

                    }


                    for (int k = 0; k < rowsList.size(); k++) {
                        numeratorCosine += (rowsList.get(k) * colsList.get(k));
                        //   System.out.println(rowsList.get(k) + "  " + colsList.get(k));

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


//    private double[][] calculateTesting(String[] reviews) {
//
//        int maxValueDocCounter = 0;
//        int documentIndex = 0;
//        tfValuesForDocumentsMap = new HashMap[reviews.length];
//        for (int i = 0; i < reviews.length; i++) {
//            tfValuesForDocumentsMap[i] = new HashMap<>();
//        }
//        termDocumentCountMap = new HashMap<>();
//        idfValuesForTermMap = new HashMap<>();
//        amountOfDocuments = reviews.length;
//
//        for (int i = 0; i < reviews.length; i++) {
//            //split string , count double words  Result  HashMap  "Term"--> Count
//            Map<String, Long> wordsAndCounts =
//                    Arrays.stream(reviews[i].split("\\s")).
//                            collect(Collectors.groupingBy(
//                                    Function.identity(),
//                                    Collectors.counting()
//                            ));        // show-->3 , read -->5 , listen -->10
//
//            for (Map.Entry<String, Long> entry : wordsAndCounts.entrySet()) {
//                if (entry.getValue() > maxValueDocCounter) {
//                    maxValueDocCounter = Math.toIntExact(entry.getValue());
//                }
//                if (termDocumentCountMap.containsKey(entry.getKey())) {
//
//                    termDocumentCountMap.put(entry.getKey(), termDocumentCountMap.get(entry.getKey()) + 1);
//                } else {
//                    termDocumentCountMap.put(entry.getKey(), 1);
//                }
//
//
//            }
//
//            calculateTFValues(documentIndex, maxValueDocCounter, wordsAndCounts);
//            maxValueDocCounter = 0;
//            documentIndex++;
//
//        }
//
//        calculateIDFValues();
//
//        System.out.println("IDF VALUES: " + idfValuesForTermMap);
//
//        return calculateCosineSim(reviews.length);
//
//
//    }


}
