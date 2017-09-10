import java.util.ArrayList;

public class DecisionTree {
    private DTNode root;
    private String[] attributes;
    // most frequent class value in the entire training set
    private int genVal = -1;

    public DecisionTree(String[] attr) {
        attributes = attr;
    }

    public int calcHighIG(ArrayList<Integer>[] atrVal, ArrayList<Integer> indices, boolean[] usedAttributes) {

        return 0;
    }

    // public facing method. Call to create the tree with training data.
    public void train(ArrayList<Integer>[] atrVal) {
        boolean[] attr = new boolean[attributes.length];
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < atrVal[0].size(); i++) {
            indices.add(i);
        }
        root = makeTree(atrVal, indices, attr, -1);
    }

    // private recursive method to make the decision tree
    private DTNode makeTree(ArrayList<Integer>[] atrVal, ArrayList<Integer> indices, boolean[] usedAttributes, int val) {
        // get the attribute to split on
        int attr = calcHighIG(atrVal, indices, usedAttributes);
        DTNode curr = new DTNode(attributes[attr], val);
        // if the node is pure, return it
        if (attr == -1) {
            return curr;
        }
        // else you've run out of attributes to use
        else if (attr == -2) {
            // get true and false values of the node
            int tru = 0;
            int fal = 0;
            for (int index:indices) {
                if (atrVal[attributes.length-1].get(index) == 0) {
                    fal++;
                }
                else {
                    tru++;
                }
            }
            // assign value according to which value is dominant
            if (fal > tru) {
                curr = new DTNode(attributes[attr], 0);
            }
            else if (tru > fal) {
                curr = new DTNode(attributes[attr], 1);
            }
            // else if the values are even, assign to the overall dominant value
            else {
                if (genVal == -1) {
                    calcGenVal(atrVal);
                }
                curr = new DTNode(attributes[attr], genVal);
            }
            return curr;
        }
        // else recurse to make the tree
        else {
            // sort the positive and negative records of that attribute
            ArrayList<Integer> falseIndices = new ArrayList<>();
            ArrayList<Integer> trueIndices = new ArrayList<>();
            for (int index : indices) {
                if (atrVal[attr].get(index) == 0) {
                    falseIndices.add(index);
                } else {
                    trueIndices.add(index);
                }
            }
            // mark the attribute as used
            usedAttributes[attr] = true;
            // build the tree left and right
            curr.left = makeTree(atrVal, falseIndices, usedAttributes, 0);
            curr.right = makeTree(atrVal, trueIndices, usedAttributes, 1);
            // unmark before returning the current node
            usedAttributes[attr] = false;
            return curr;
        }
    }

    // calculate the overall dominant value
    private void calcGenVal(ArrayList<Integer>[] atrVal) {
        int pos = 0;
        int neg = 0;
        for (int i = 0; i < atrVal[attributes.length-1].size(); i++) {
            if (atrVal[attributes.length-1].get(i) == 0) {
                neg++;
            }
            else {
                pos++;
            }
        }
        if (pos > neg) {
            genVal = 1;
        }
        else {
            genVal = 0;
        }
    }

    // calculates entropy
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

    // calculates information gain
    // (entropy of left child node, # of values in left node, entropy of right child node, # of values in right node, entropy of current node)
    public static double calcIG(double HL, double totalL, double HR, double totalR, double H ) {
        double total = totalL + totalR;
        double PR = totalR/total;
        double PL = totalL/total;

        return H - (HL*PL + HR*PR);
    }
}
