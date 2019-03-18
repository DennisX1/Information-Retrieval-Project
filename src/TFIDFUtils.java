import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.Math.toIntExact;

public class TFIDFUtils {


    private HashMap<String, Double>[] tfValuesForDocumentsMap;
    private HashMap<String, Integer> termDocumentCountMap;
    private HashMap<String, Double> idfValuesForTermMap;
    private int amountOfDocuments;

    public static double[][] computeSimilarities(String[] reviews) {


        TFIDFUtils tfidfUtils = new TFIDFUtils();
        return tfidfUtils.calculate(reviews);
    }


    private double[][] calculate(String[] reviews) {

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
                    Arrays.stream(reviews[i].split("\\s")).
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


    private void calculateTFValues(int documentindex, int maxValueDocCounter, Map<String, Long> wordsAndCounts) {

        for (Map.Entry<String, Long> entry : wordsAndCounts.entrySet()) {

            tfValuesForDocumentsMap[documentindex].put
                    (entry.getKey(),
                            ((1 + Math.log10((double) entry.getValue())) / (1 + Math.log10((double) maxValueDocCounter))));


        }
        System.out.println("Doc: " + documentindex + "    " + tfValuesForDocumentsMap[documentindex]);


    }

    private void calculateIDFValues() {

        for (Map.Entry<String, Integer> entry : termDocumentCountMap.entrySet()) {
            idfValuesForTermMap.put(entry.getKey(), (Math.log10(amountOfDocuments / (double) entry.getValue())));
        }
//        System.out.println( "IDF" +idfValuesForTermMap);
//        System.out.println ("IDF END");

    }

    private double[][] calculateCosineSim(int arraysize) {

        double[][] cosineArray = new double[arraysize][arraysize];


        for (int row = 0; row < cosineArray.length; row++) {
            for (int col = 0; col < cosineArray[row].length; col++) {

                if (row == col) {
                    cosineArray[row][col] = 1;
                } else {


                    ArrayList<Double> rowsList = new ArrayList<>();
                    ArrayList<Double> colsList = new ArrayList<>();


                    for (Map.Entry<String, Double> entry : tfValuesForDocumentsMap[row].entrySet()) {

                        if (tfValuesForDocumentsMap[col].containsKey(entry.getKey())) {

                            rowsList.add(entry.getValue() * idfValuesForTermMap.get(entry.getKey()));
                            colsList.add(tfValuesForDocumentsMap[col].get(entry.getKey()) * idfValuesForTermMap.get(entry.getKey()));


                        }
                    }

                    double numeratorCosine = 0;
                    double denominatorFirst = 0;
                    double denominatorSecond = 0;


                    for (int k = 0; k < rowsList.size(); k++) {
                        numeratorCosine += (rowsList.get(k) * colsList.get(k));
                        denominatorFirst += rowsList.get(k) * rowsList.get(k);
                        denominatorSecond += colsList.get(k) * colsList.get(k);
                        //   System.out.println(rowsList.get(k) + "  " + colsList.get(k));

                    }
                    if ((denominatorFirst != 0 && denominatorSecond != 0)) {
                        cosineArray[row][col] = numeratorCosine / (Math.sqrt(denominatorFirst) * Math.sqrt(denominatorSecond));

                    } else {

                        cosineArray[row][col] = 0;
                    }


                }

            }
        }


        System.out.println(Arrays.deepToString(cosineArray).replace("], ", "]\n"));
        return cosineArray;

    }


}
