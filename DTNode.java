import java.util.*;

public class DTNode {
    protected DTNode left;
    protected DTNode right;
    String name;
    int val;

    public DTNode(String n, int b) {
        left = null;
        right = null;

        name = n;
        val = b;
    }
}
