import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException{
        //if (args.length == 2) {
            Scanner trainIn = new Scanner(new File(/*args[0]*/"src/train.dat"));
            String[] attributes = trainIn.nextLine().split("\\s+");
            ArrayList<Integer>[] atrVal = (ArrayList<Integer>[])new ArrayList[attributes.length];
            for (int z = 0; z < atrVal.length; z++) {
                atrVal[z] = new ArrayList<>();
            }
            int pos = 0;
            while(trainIn.hasNextInt()) {
                atrVal[pos].add(trainIn.nextInt());

                // if pos reaches the class attribute, reset pos; else increment pos
                pos = pos == atrVal.length-1?0:pos+1;
            }

            ArrayList<Integer> tList = new ArrayList<Integer>();
            ArrayList<Integer> fList = new ArrayList<Integer>();
            for(int y = 0; y < atrVal[0].size(); y++) {
                if(atrVal[atrVal.length-1].get(y) == 0) {
                    fList.add(y);
                }
                else {
                    tList.add(y);
                }
            }
            DecisionTree tree = new DecisionTree(new DTNode("Class", true, calcH(tList.size(), fList.size()), tList, fList));

        //}
        //else {
        //    System.out.println("Incorrect number of arguments. Requires two arguments: training file and test file.");
        //}
    }

    public static double calcH(int pos, int neg) {
        double total = pos + neg;

        //System.out.println("Total is " + total);
        double N = neg/total;
        double P = pos/total;
        //System.out.println("N is " + N + " and P is " + P);
        if (pos == 0 || neg == 0) {
            return 0;
        } else {
            return -(P) * (Math.log(P)/Math.log(2)) - (N * Math.log(N)/Math.log(2));
        }

    }

    public static double calcIG(double HL, double totalL, double HR, double totalR, double H ) {
        double total = totalL + totalR;
        double PR = totalR/total;
        double PL = totalL/total;

        return H - (HL*PL + HR*PR);
    }
}
