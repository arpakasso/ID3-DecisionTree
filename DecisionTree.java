/**
 * Binary Decision Tree. This class describes an ID3 Decision Tree that accepts binary classifications.
 */

import java.util.*;

public class DecisionTree {
    private DTNode root;            // reference to the tree root node
    private String[] attributes;    // attribute array; contains attribute names
    private int genVal = -1;        // the dominant class value of the whole data
                                    // only calculated when needed to break ties

    /**
     * Constructor.
     *
     * @param attr attribute array
     */
    public DecisionTree(String[] attr) {
        attributes = attr;
    }

    /**
     * Finds the highest information gain (IG) of the available attributes
     *
     * @param atrVal values for attributes
     * @param indices indices of relevant values; used to reference atrVal
     * @param usedAttributes marks whether an attribute has been used
     *                       true values mean the attribute has beed used
     * @return returns index of attribute with highest IG; used to reference attribute array
     */
    private int calcHighIG(ArrayList<Integer>[] atrVal, ArrayList<Integer> indices, boolean[] usedAttributes) {
        double[] IGs = new double[attributes.length-1];
        int pos = 0;
        int neg = 0;

        //find entropy for the current node
        for (int i = 0; i < indices.size(); i++) {
            if (atrVal[attributes.length-1].get(indices.get(i)) == 1) {
                pos++;
            } else {
                neg++;
            }
        }

        double H = calcH(pos, neg);
        for (int i = 0; i < IGs.length; i++) {
            int posL = 0;
            int negL = 0;
            int posR = 0;
            int negR = 0;
            if (usedAttributes[i] == false) {
                for (int k = 0; k < indices.size(); k++) {
                    int clss = atrVal[attributes.length-1].get(indices.get(k));
                    int atr = atrVal[i].get(indices.get(k));
                    if (atr == 1 && clss == 1) {
                        posL++;
                    }
                    else if (atr == 1 && clss == 0) {
                        negL++;
                    }
                    else if (atr == 0 && clss == 1) {
                        posR++;
                    }
                    else if (atr == 0 && clss == 0) {
                        negR++;
                    }
                }
                IGs[i] = calcIG(calcH(posL, negL), posL+negL, calcH(posR, negR), posR+negR, H);
            } else {
                IGs[i] = -1;
            }
        }

        // find the index of attribute with the highest IG
        int IGindex = 0;
        double highIG = -1;
        for (int hi = 0; hi < IGs.length; hi++) {
            if (IGs[hi] > highIG) {
                highIG = IGs[hi];
                IGindex = hi;
            }
        }
        // if node is pure, or all attributes have been used, the node is a leaf
        return IGindex;
    }

    /**
     * Public facing method. Call to create the tree with training data.
     *
     * @param atrVal values for attributes
     */
    public void train(ArrayList<Integer>[] atrVal) {
        boolean[] attr = new boolean[attributes.length-1];
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < atrVal[0].size(); i++) {
            indices.add(i);
        }
        root = makeTree(atrVal, indices, attr);
    }

    /**
     * Public facing method. Call to test data on the tree and get accuracy of decision tree.
     *
     * @param atrVal values for attributes
     * @return int[0] contains # of records that resolve to the correct class value
     *         int[1] contains # of records that resolve to the incorrect class value
     */
    public int[] classify(ArrayList<Integer>[] atrVal) {
        int[] accuracyArr = new int[2];

        for (int i = 0; i < atrVal[0].size(); i++) {
            traverse(atrVal, i, root, accuracyArr);
        }

        return accuracyArr;
    }

    /**
     * Private method to traverse the tree and find correct and incorrect classifications.
     *
     * @param atrVal values for attributes
     * @param row index for the record being checked; used to reference atrVal
     * @param root current node visited in the tree
     * @param accuracyArr number of correct and incorrect classifications
     */
    private void traverse(ArrayList<Integer>[] atrVal, int row, DTNode root, int[] accuracyArr) {
        if (root.isLeaf == true) {
            //take root value and compare it to class value
            if (root.val == atrVal[atrVal.length-1].get(row)) {
                accuracyArr[0]++;
            } else {
                accuracyArr[1]++;       //when they don't match
            }
        }
        else {
            if (atrVal[root.index].get(row) == 0) {
                traverse(atrVal, row, root.left, accuracyArr);
            } else {
                traverse(atrVal, row, root.right, accuracyArr);
            }
            return;
        }
    }

    /**
     * Private recursive method to make the decision tree.
     *
     * @param atrVal values for attributes
     * @param indices indices of relevant values; used to reference atrVal
     * @param usedAttributes marks whether an attribute has been used
     *                       true values mean the attribute has beed used
     * @return returns the current node
     */
    private DTNode makeTree(ArrayList<Integer>[] atrVal, ArrayList<Integer> indices, boolean[] usedAttributes) {
        // curr node will have the attribute with the highest IG
        int attr = calcHighIG(atrVal, indices, usedAttributes);
        DTNode curr = new DTNode(attributes[attr], attr, false);
        // checks for a pure node
        int posH = 0;
        int negH = 0;
        for (int i = 0; i < indices.size(); i++) {
            if (atrVal[atrVal.length-1].get(indices.get(i)) == 0) {
                negH++;
            }
            else {
                posH++;
            }
        }
        double currH = calcH(posH, negH);
        if (currH == 0) {
            int currVal = 0;
            if (posH > negH) {
                currVal = 1;
            }
            DTNode rtn = new DTNode(attributes[attributes.length-1], attributes.length-1, true);
            rtn.val = currVal;
            return rtn;
        }

        // checks if all the attributes have been used
        boolean allUsed = true;
        for (int j = 0; j < usedAttributes.length; j++) {
            if (usedAttributes[j] == false)
                allUsed = false;
        }

        // checks if all the attribute values are the same (there are inconsistent records)
        boolean attrAllSame = true;
        for (int i = 0; i < atrVal.length-1 && attrAllSame; i++) {
            for (int j = 0; j < indices.size()-1 && attrAllSame; j++) {
                if (atrVal[i].get(indices.get(j)) != atrVal[i].get(indices.get(j+1))) {
                    attrAllSame = false;
                }
            }
        }

        if (allUsed || attrAllSame) {
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
            DTNode rtn = new DTNode(attributes[attributes.length-1], attributes.length-1, true);
            if (fal > tru) {
                rtn.val = 0;
            }
            else if (tru > fal) {
                rtn.val = 1;
            }
            // else if the values are even, assign to the overall dominant value
            else {
                if (genVal == -1) {
                    calcGenVal(atrVal);
                }
                rtn.val = genVal;
            }
            return rtn;
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
            curr.left = makeTree(atrVal, falseIndices, usedAttributes);
            curr.right = makeTree(atrVal, trueIndices, usedAttributes);
            // unmark before returning the current node
            usedAttributes[attr] = false;
            return curr;
        }
    }

    /**
     * Calculate the overall dominant value; stored in genVal class variable.
     * Used to tiebreak when examples are equally split among classes at a leaf node.
     *
     * @param atrVal values for attributes
     */
    private void calcGenVal(ArrayList<Integer>[] atrVal) {
        int pos = 0;
        int neg = 0;
        for (int i = 0; i < atrVal[atrVal.length-1].size(); i++) {
            if (atrVal[atrVal.length-1].get(i) == 0) {
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

    /**
     * Calculates entropy of the "node", given pos and neg values.
     *
     * @param pos number of true/positive class values
     * @param neg number of false/negative class values
     * @return returns entropy value
     */
    private double calcH(int pos, int neg) {
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

    /**
     *  Calculates information gain for a particular attribute with respect to the current attribute.
     *
     * @param HL entropy of left child node
     * @param totalL # of values in left node
     * @param HR entropy of right child node
     * @param totalR # of values in right node
     * @param H entropy of current node
     * @return returns information gain for that particular attribute
     */
    private double calcIG(double HL, double totalL, double HR, double totalR, double H ) {
        double total = totalL + totalR;
        double PR = totalR/total;
        double PL = totalL/total;

        return H - (HL*PL + HR*PR);
    }

    /**
     * Public facing method to print the tree.
     */
    public void printTree() {
        printTree(root, 0);
        System.out.println();
    }

    /**
     * Private recursive method.
     * Traverses the tree to print each node's attribute and relevant values in the proper format.
     *
     * @param curr the current node visited
     * @param height the height of the node in the tree, used for printing pipes (|)
     */
    private void printTree(DTNode curr, int height) {
        for(int i = 0; i < height; i++) {
            System.out.print("| ");
        }
        System.out.printf("%s = 0 :",curr.name);
        if(!curr.left.isLeaf) {
            System.out.println();
            printTree(curr.left, height+1);
        }
        else {
            System.out.printf("  %d%n", curr.left.val);
        }

        for(int i = 0; i < height; i++) {
            System.out.print("| ");
        }
        System.out.printf("%s = 1 :",curr.name);
        if(!curr.right.isLeaf) {
            System.out.println();
            printTree(curr.right, height + 1);
        }
        else {
            System.out.printf("  %d%n", curr.right.val);
        }
    }
}
