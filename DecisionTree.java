public class DecisionTree {
    private DTNode root;
    private String[] attributes;

    public DecisionTree(DTNode r, String[] attr) {
        root = r;
        attributes = attr;
    }

    public int calcHighIG() {
        double[] IGs = new double[attributes.length];
        for (int i = 0; i < IGs.length; i++) {

            //IGs[i] = calcIG();
        }
        return 0;
    }

    public void train() {

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
