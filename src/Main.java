public class Main {


    public static void main(String[] args) {
        String a = "Frodo accidentally stab Sam and then some orc";
        String b = "Frodo was stab regular orc but never stab super orc – Uruk-Hais";
        String c = "Sam was having a barbecue with some friendly orc";

        String d = "Shipment of gold damaged in a fire";
        String e = "Delivery of silver arrived in a silver truck";
        String f = "Shipment of gold arrived in a truck";
        String [] testAray = {a,b,c};
        String [] testArray1 = {d,e,f};
        TFIDFUtils.computeSimilarities(testArray1);
    }

}
