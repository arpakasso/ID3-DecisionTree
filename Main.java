import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException{
        //if (args.length == 2) {
            Scanner trainIn = new Scanner(new File(/*args[0]*/"src/train.dat"));
            String line;
            do {
                line = trainIn.nextLine().trim();
            } while(line.isEmpty());
            String[] attributes = line.split("\\s+");
            ArrayList<Integer>[] atrVal = readData(trainIn, attributes.length);

            DecisionTree tree = new DecisionTree(attributes);
            tree.train(atrVal);
            tree.printTree();
            System.out.println("Finish");

            Scanner testIn = new Scanner(new File(/*args[1]*/"src/test.dat"));
            do {
                line = testIn.nextLine().trim();
            } while(line.isEmpty());
            attributes = line.split("\\s+");
            atrVal = readData(testIn, attributes.length);


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
