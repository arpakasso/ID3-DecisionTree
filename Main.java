/**CS 4375 Assignment 1: Decision Tree Induction
 * Authors: Krithika Suresh
 *          Elizabeth Trinh
 *
 * Command line arguments (2): file name for training data and file name for test data, in that order.
 *
 * Function: The program will output to stdout the formatted decision tree created with the training data.
 *           It will report the accuracy of the decision tree on training data, then the accuracy on test data.
 *
 * The program reads in each file with a helper method, readData, which stores values in an array of ArrayLists.
 * The array of ArrayLists corresponds to the array holding the attribute names/labels.
 * Each ArrayList holds values for its corresponding attribute.
 * Creates a DecisionTree object to make, store, and use the decision tree.
 */

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException{
        // checks for the correct amount of commandline arguments
        if (args.length == 2) {
            // read in training data
            Scanner trainIn = new Scanner(new File(args[0]));
            // ignore lines with only whitespace characters
            String line;
            do {
                line = trainIn.nextLine().trim();
            } while(line.isEmpty());
            String[] attributes = line.split("\\s+");
            ArrayList<Integer>[] atrVal = readData(trainIn, attributes.length);

            // instantiate, train, and print the decision tree
            DecisionTree tree = new DecisionTree(attributes);
            tree.train(atrVal);
            tree.printTree();

            // calculate accuracy of tree on training data
            int[] accuracyArr = tree.classify(atrVal);
            double percent = (accuracyArr[0]*1.0/(accuracyArr[1]+accuracyArr[0])) * 100;
            System.out.printf("Accuracy on training set (%d instances):  %.1f%%%n", atrVal[0].size(), percent);
            System.out.println();

            // read in test data
            Scanner testIn = new Scanner(new File(args[1]));
            // ignore lines with only whitespace characters
            do {
                line = testIn.nextLine().trim();
            } while(line.isEmpty());
            attributes = line.split("\\s+");
            atrVal = readData(testIn, attributes.length);

            // calculate accuracy of tree on test data
            accuracyArr = tree.classify(atrVal);
            percent = (accuracyArr[0]*1.0/(accuracyArr[1]+accuracyArr[0])) * 100;
            System.out.printf("Accuracy on test set (%d instances):  %.1f%%%n", atrVal[0].size(), percent);
        }
        else {
            System.out.println("Incorrect number of arguments. Requires two arguments: training file and test file.");
        }
    }

    /**
     * Reads in attribute data and stores it for later reference.
     *
     * @param in scanner to read the appropriate file
     * @param len length of the attribute array
     *            used to instantiate the data structure that holds the attribute values
     * @return returns the data structure that holds the values for attributes
     * @throws FileNotFoundException
     */
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
