package main;

import SimMeasuresUtils.TFIDFUtils;

public class TestMain {


    public static void main(String[] args) {
        String a = "Frodo accidentally stab Sam and then some orc";
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
      //  TFIDFUtils.computeSimilaritiesTEST(testArray1);
    }

}
