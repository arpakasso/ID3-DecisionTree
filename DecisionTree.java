import java.util.*;
public class DecisionTree {
    private DTNode root;
    private String[] attributes;
    private ArrayList<Integer>[] atrVal;

    public DecisionTree(DTNode r, String[] attr) {
        root = r;
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
            if (atrVal[indices.get(i)].get(attributes.length-1) == 1) {
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
               for (int k = 0; k < atrVal[i].size(); k++) {
                   int clss = atrVal[atrVal[0].size()-1].get(k);
                   int atr = atrVal[i].get(k);
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
               IGs[i] = 100;
           }
        }
        
        int IGindex = 0; 
        double highIG = Integer.MIN_VALUE;
        for (int hi = 0; hi < IGs.length; hi++) {
            if (IGs[hi] > highIG) {
                highIG = IGs[hi];
                IGindex = hi;
            }
        }
        
        return IGindex;
        
//        double[] IGs = new double[attributes.length-1];
//        int posL = 0;
//        int negL = 0;
//        int posR = 0;
//        int negR = 0;
//        
//        for (int i = 0; i < IGs.length; i++) {
//            
//            for (int j = 0; j < atrVal[i].size(); j++) {
//                if (atrVal[i].get(j) == 1 && atrVal[atrVal[0].size()-1].get(j) == 1) {
//                    posL++;
//                }
//                else if (atrVal[i].get(j) == 1 && atrVal[atrVal[0].size()-1].get(j) == 0) {
//                    negL++;
//                }
//                else if (atrVal[i].get(j) == 0 && atrVal[atrVal[0].size()-1].get(j) == 1) {
//                    posR++;
//                }
//                else if (atrVal[i].get(j) == 0 && atrVal[atrVal[0].size()-1].get(j) == 0) {
//                    negR++;
//                }
//            }
//            
//            IGs[i] = calcIG(calcH(posL, negL), posL+negL, calcH(posR, negR), posR+negR, parent.entropy);
//        }
        
        
//        return 0;
    }
    
    
    
    //imports training data and attributes
    public void train(String[] attributes, ArrayList<Integer>[] atrVal) {
        this.attributes = attributes;
        this.atrVal = atrVal;
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
