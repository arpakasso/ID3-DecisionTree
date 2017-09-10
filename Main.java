import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException{
        //if (args.length == 2) {
            Scanner trainIn = new Scanner(new File("src/train.dat"));
            String[] attributes = trainIn.nextLine().split("\\s+");
            ArrayList<Integer>[] atrVal = readData(trainIn, attributes.length);

            int tru = 0;
            int fal = 0;
            for(int y = 0; y < atrVal[0].size(); y++) {
                if(atrVal[atrVal.length-1].get(y) == 0) {
                    fal++;
                }
                else {
                    tru++;
                }
            }
            DecisionTree tree = new DecisionTree(new DTNode("Class", true, DecisionTree.calcH(tru, fal)), attributes);

        //}
        //else {
        //    System.out.println("Incorrect number of arguments. Requires two arguments: training file and test file.");
        //}
    }

    public static ArrayList<Integer>[] readData(Scanner in, int len)  throws FileNotFoundException{

        ArrayList<Integer>[] rtn = (ArrayList<Integer>[])new ArrayList[len];

        for (int z = 0; z < rtn.length; z++) {
            rtn[z] = new ArrayList<>();
        }
        int pos = 0;
        while(in.hasNextInt()) {
            rtn[pos].add(in.nextInt());

            // if pos reaches the class attribute, reset pos; else increment pos
            pos = pos == rtn.length-1?0:pos+1;
        }
        in.close();

        return rtn;
    }
}
