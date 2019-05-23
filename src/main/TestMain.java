package main;

import Preprocessing.Stemmer;
import Preprocessing.StopWordRemovalUtils;
import SimMeasuresUtils.TFIDFUtils;
import data.Review;

public class TestMain {


    public static void main(String[] args) {
        /*String a = "Frodo accidentally stab Sam and then some orc";
        String b = "Frodo was stab regular orc but never stab super orc – Uruk-Hais";
        String c = "Sam was having a barbecue with some friendly orc";

        String d = "Shipment of gold damaged in a fire";
        String e = "Delivery of silver arrived in a silver truck";
        String f = "Shipment of gold arrived in a truck";

        String g = "Saturn is the gas planet with rings";
        String h = "Jupiter is the largest gas planet";
        String l = "Saturn is the Roman god of sowing";
        String [] testAray = {a,b,c};
        String [] testArray1 = {g,h,l};

        Review[] orginalReview = new Review[3];
        orginalReview[0] =  new Review("Saturn is the gas planet with rings.",5.0,true);
        orginalReview[1] = new Review("Jupiter is the largest gas planet.",5.0,true);
        orginalReview[2] = new Review ("Saturn is the Roman god of sowing",5.0,true);*/

        /*Review [] cleaned = StopWordRemovalUtils.removeStopWords(orginalReview);
        Review [] stemmed = Stemmer.stemReviews(cleaned);
       TFIDFUtils.computeSimilarities(stemmed);*/

       String aa= "super cool";
       String bb = "Totally awesome";
       Review[]  rev = new Review[2];
       rev[0] = new Review(aa, 3.0, true);
       rev[1] = new Review(bb, 3.0, true);
        Review [] cleanedT = StopWordRemovalUtils.removeStopWords(rev);
        Review [] stemmedT = Stemmer.stemReviews(cleanedT);
        double [][] test = TFIDFUtils.computeSimilarities(stemmedT);
        System.out.println("Hello World!");
    }

}
