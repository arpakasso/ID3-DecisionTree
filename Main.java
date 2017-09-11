import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException{
        //if (args.length == 2) {
            Scanner trainIn = new Scanner(new File("src/train.dat"));
            String line;
            do {
                line = trainIn.nextLine().trim();
            } while(line.isEmpty());
            String[] attributes = line.split("\\s+");
            ArrayList<Integer>[] atrVal = readData(trainIn, attributes.length);

            DecisionTree tree = new DecisionTree(attributes);
            tree.train(atrVal);
            tree.printTree();
            DecisionTree tree = new DecisionTree(attributes);
            tree.train(atrVal);
            tree.printTree();
            int[] accuracyArr = tree.classify(atrVal);
            double percent = (accuracyArr[0]*1.0/(accuracyArr[1]+accuracyArr[0])) * 100;
            System.out.printf("Accuracy on training set (%d instances): %.1f%%%n", atrVal[0].size(), percent);
            System.out.println();
            accuracyArr = tree.classify(atrVal);
            System.out.printf("Accuracy on test set (%d instances): %.1f%%%n", atrVal[0].size(), percent);

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
