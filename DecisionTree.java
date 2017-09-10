import java.util.*;

public class DecisionTree {
    private DTNode root;
    private String[] attributes;
    private int genVal = -1;
    
    public DecisionTree(String[] attr) {
        attributes = attr;
    }

    //find the highest IG of the available attributes
    // not sure if this works correctly
    public int calcHighIG(ArrayList<Integer>[] atrVal, ArrayList<Integer> indices, boolean[] usedAttributes) {
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

    // public facing method. Call to create the tree with training data.
    public void train(ArrayList<Integer>[] atrVal) {
        boolean[] attr = new boolean[attributes.length-1];
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < atrVal[0].size(); i++) {
            indices.add(i);
        }
        root = makeTree(atrVal, indices, attr);
    }

    // private recursive method to make the decision tree
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
        if (allUsed) {
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

    // calculate the overall dominant value
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
