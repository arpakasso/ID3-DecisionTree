import java.util.*;

public class DTNode {
    DTNode left;
    DTNode right;
    int index;
    String name;
    int val;
    boolean isLeaf;

    // Constructor, needs name, index in attribute array, and boolean denoting if the node is a leaf
    public DTNode(String n, int ind, boolean l) {
        left = null;
        right = null;

        name = n;
        index = ind;
        isLeaf = l;
    }
}
