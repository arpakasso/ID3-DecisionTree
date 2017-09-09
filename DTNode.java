import java.util.*;

public class DTNode {
    protected DTNode left;
    protected DTNode right;
    String name;
    boolean val;
    protected double entropy;
    protected ArrayList<Integer> trueList;
    protected ArrayList<Integer> falseList;

    public DTNode(String n, boolean b, double e, ArrayList<Integer> tL, ArrayList<Integer> fL) {
        left = null;
        right = null;

        name = n;
        val = b;
        entropy = e;

        trueList = tL;
        falseList = fL;
    }
}