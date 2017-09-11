/**
 * Decision Tree Nodes. This class describes the object nodes found in a DecisionTree.
 * Used in the DecisionTree class.
 */
public class DTNode {
    DTNode left;    // reference to the left child
    DTNode right;   // reference to the right child
    int index;      // the node's attribute's index in the attribute array
    String name;    // the node's name or attribute
    int val;        // if a leaf node, its class value
    boolean isLeaf; // denotes a leaf node

    /**
     * Constructor.
     *
     * @param n name
     * @param ind index in attribute array
     * @param l boolean denoting a leaf node
     */
    public DTNode(String n, int ind, boolean l) {
        left = null;
        right = null;

        name = n;
        index = ind;
        isLeaf = l;
    }
}
