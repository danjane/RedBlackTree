import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BinaryTree {

    enum Colour{RED, BLACK}

    static class Node {
        int key;
        Node left, right, parent;
        Colour colour;

        public Node(int data){
            key = data;
            left = right = parent = null;
            colour = Colour.RED;
        }
    }

    static class FamilyNode extends Node {
        Node grandparent, uncle;
        Colour parentColour, grandparentColour, uncleColour;

        public FamilyNode(Node node){
            super(node.key);
            left = node.left;
            right = node.right;
            parent = node.parent;
            colour = node.colour;

            if (parent==null) {
                grandparent = null;
                uncle = null;
                parentColour = null;
                grandparentColour = null;
                uncleColour = null;
            }
            else {
                grandparent = parent.parent;
                if (grandparent==null) {
                    grandparentColour = null;
                    uncleColour = null;
                } else {
                    grandparentColour = grandparent.colour;
                    if (grandparent.left == node)
                        uncle = grandparent.right;
                    else
                        uncle = grandparent.left;
                    if (uncle==null)
                        uncleColour = null;
                    else
                        uncleColour = uncle.colour;
                }
            }
        }
    }

    private Node root = null;

    public boolean isEmpty() {
        return root==null;
    }

    public void insert(int key) {
        root = insertRecursive(root, key);
    }

    private Node insertRecursive(Node root, int key) {
        if (root == null) {
            root = new Node(key);
        }
        else if (key < root.key) {
            root.left = insertRecursive(root.left, key);
            root.left.parent = root;
        }
        else {
            root.right = insertRecursive(root.right, key);
            root.right.parent = root;
        }
        return root;
    }

    public void inserts(int[] xs) {
        for (int x : xs) {
            insert(x);
        }
    }

    public void insertRB(int key) {
        root = insertRecursiveRB(root, key, null);
        root.colour = Colour.BLACK; //TODO: should this be in fixRBtree?
    }

    public void insertsRB(int[] keys) {
        for (int key : keys) {
            insertRB(key);
        }
    }

    private Node insertRecursiveRB(Node root, int key, Node parent) {
        if (root == null) {
            root = new Node(key);
            root.parent = parent;
            fixRBtree(root);
/*          1. Every node is either red or black.
            2. The root is black.
            3. Every leaf (NIL) is black.
            4. If a node is red, then both its children are black.
            5. For each node, all simple paths from the node to descendant leaves contain the
            same number of black nodes. */
        }
        else if (key < root.key) {
            root.left = insertRecursiveRB(root.left, key, root);
        }
        else {
            root.right = insertRecursiveRB(root.right, key, root);
        }
        return root;
    }

    private void fixRBtree(Node node) {
        FamilyNode familyNode = new FamilyNode(node);
        if (familyNode.uncleColour==Colour.RED)
            redUncleSoRecolour(familyNode);
    }

    private void redUncleSoRecolour(FamilyNode familyNode) {
        familyNode.parent.colour = Colour.BLACK;
        familyNode.uncle.colour = Colour.BLACK;
        familyNode.grandparent.colour = Colour.RED;
    }

    public void delete(int key) {
        root = deleteRecursive(root, key);
    }

    private Node deleteRecursive(Node root, int key) {
        if (root == null)
            throw new Empty();

        if (key < root.key)
            root.left = deleteRecursive(root.left, key);
        else if (key > root.key)  //traverse right subtree
            root.right = deleteRecursive(root.right, key);
        else {
            // node contains only one child
            if (root.left == null)
                return root.right;
            else if (root.right == null)
                return root.left;
            else {
                // DISASTER! Node has two children
                root.key = minimumValueRecursive(root.right);
                root.right = deleteRecursive(root.right, root.key);
            }
        }
        return root;
    }

    private List<Integer> getValuesRecursive(Node node) {
        if (node == null) return new ArrayList<>();
        List<Integer> left_values = getValuesRecursive(node.left);
        List<Integer> right_values = getValuesRecursive(node.right);
        left_values.add(node.key);
        left_values.addAll(right_values);
        return left_values;
    }

    private List<Integer> getPreorderValuesRecursive(Node node) {
        // Preorder (Root, Left, Right)
        if (node == null) return new ArrayList<>();
        List<Integer> values = new ArrayList<>(Collections.singletonList(node.key));
        values.addAll(getPreorderValuesRecursive(node.left));
        values.addAll(getPreorderValuesRecursive(node.right));
        return values;
    }

    public int[] values() {
        List<Integer> list = getValuesRecursive(root);
        int[] vs = new int[list.size()];
        for (int i = 0; i < list.size(); i++)
            vs[i] = list.get(i);
        return vs;
    }

    public int[] preorderValues() {
        List<Integer> list = getPreorderValuesRecursive(root);
        int[] vs = new int[list.size()];
        for (int i = 0; i < list.size(); i++)
            vs[i] = list.get(i);
        return vs;
    }

    private int minimumValueRecursive(Node n){
        if (n.left == null) return n.key;
        else return minimumValueRecursive(n.left);
    }

    public int minimumValue() {
        if (isEmpty())
            throw new Empty();
        return minimumValueRecursive(root);
    }

    private Node findRecursive(Node n, int k) {
        if (n==null) return null;
        else if (k < n.key) return findRecursive(n.left, k);
        else if (k > n.key) return findRecursive(n.right, k);

        return n;
    }

    public boolean find(int k) {
        return findRecursive(root, k) != null;
    }

    public Colour rootColour() {
        return root.colour;
    }

    public int[] blackHeights() {
        List<Integer> list = getBlackHeightsRecursive(root);
        int[] vs = new int[list.size()];
        for (int i = 0; i < list.size(); i++)
            vs[i] = list.get(i);
        return vs;
    }

    private List<Integer> getBlackHeightsRecursive(Node node) {
        if (node == null) return new ArrayList<>(Collections.singletonList(1));
        List<Integer> bhs = getBlackHeightsRecursive(node.left);
        bhs.addAll(getBlackHeightsRecursive(node.right));
        if (isBlack(node))
            for (int i = 0; i < bhs.size(); i++)
                bhs.set(i, bhs.get(i) + 1);
        return bhs;
    }

    private boolean isBlack(Node n) {
        return n.colour.equals(Colour.BLACK);
    }

    private boolean isRed(Node n) {
        return n.colour.equals(Colour.RED);
    }

    public boolean redViolation() {
        Node possibleRed = redNodeWithRedChildRecursive(root, false);
        return (possibleRed != null && isRed(possibleRed));
    }

    private Node redNodeWithRedChildRecursive(Node root, boolean redParent) {
        if (root==null)
            return null;

        boolean redRoot = isRed(root);

        if (redParent && redRoot)
            return root;
        else {
            Node node = redNodeWithRedChildRecursive(root.left, redRoot);
            if (node==null)
                return redNodeWithRedChildRecursive(root.right, redRoot);
            return node;
        }
    }

    private void leftRotate(Node node_x) {
        Node node_y = node_x.right;
        node_y.parent = node_x.parent;

        node_x.right = node_y.left;
        node_x.right.parent = node_x;

        node_y.left = node_x;
        node_x.parent = node_y;

        if (node_y.parent == null)
            root = node_y;
    }

    private void rightRotate(Node node_y) {
        Node node_x = node_y.left;
        node_x.parent = node_y.parent;

        node_y.left = node_x.right;
        node_y.left.parent = node_y;

        node_x.right = node_y;
        node_y.parent = node_x;

        if (node_x.parent == null)
            root = node_x;
    }

    public void leftRotateOnKey(int key) {
        // For debugging, to allow a test of leftRotate
        Node node = findRecursive(root, key);
        leftRotate(node);
    }

    public void rightRotateOnKey(int key) {
        // For debugging, to allow a test of rightRotate
        Node node = findRecursive(root, key);
        rightRotate(node);
    }


    static class Empty extends RuntimeException {
    }

    public static void main(String[] args) {
        BinaryTree tree = new BinaryTree();
        // tree.inserts(new int[] {11,2,14,1,7,15,5,8,4});
        tree.insertsRB(new int[]{2,1,3,4});
        System.out.println(tree.root.colour);

        /*
        tree.leftRotateOnKey(2);
        int[] vs = tree.preorderValues();
        for (int v : vs) {
            System.out.println("This is : " + v);
        }
        */
    }

}