import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BinaryTree {

    enum Colour{RED, BLACK}
    enum GrandType{LL, LR, RR, RL}

    static class Node implements TreePrinter.PrintableNode {

        // public static final String ANSI_RESET = "\u001B[0m";
        // public static final String ANSI_BLACK = "\u001B[30m";
        // public static final String ANSI_RED = "\u001B[31m";

        int key;
        Node left, right, parent;
        Colour colour;

        public Node(int data){
            key = data;
            left = right = parent = null;
            colour = Colour.RED;
        }

        @Override
        public Node getLeft() {
            return left;
        }

        @Override
        public Node getRight() {
            return right;
        }

        @Override
        public String getText() {
            return toString();
            /* Bugger, can't get this to work with offsets
            if (colour==Colour.BLACK)
                return ANSI_BLACK + key + ANSI_RESET;
            else
                return ANSI_RED + key + ANSI_RESET;
            */
        }

        public String toString() {
            return key + ", " + colour;
        }
    }

    static class FamilyNode extends Node {
        Node grandparent, uncle, underlying;
        Colour parentColour, grandparentColour, uncleColour;
        GrandType grandType;

        public FamilyNode(Node node){
            super(node.key);
            underlying = node;
            left = node.left;
            right = node.right;
            parent = node.parent;
            colour = node.colour;
            grandType = null;

            if (parent==null)
                nullParent();
            else {
                parentColour = parent.colour;
                grandparent = parent.parent;
                if (grandparent==null)
                    nullGrandparent();
                else
                    notNullGrandparent();
            }
        }

        private void nullParent() {
            grandparent = null;
            uncle = null;
            parentColour = null;
            grandparentColour = null;
            uncleColour = null;
        }

        private void nullGrandparent() {
            grandparentColour = null;
            uncleColour = null;
        }

        private void notNullGrandparent() {
            grandparentColour = grandparent.colour;
            if (grandparent.left == this.parent) {
                uncle = grandparent.right;
                if (parent.left == underlying)
                    grandType = GrandType.LL;
                else
                    grandType = GrandType.LR;
            }
            else {
                uncle = grandparent.left;
                if (parent.left == underlying)
                    grandType = GrandType.RL;
                else
                    grandType = GrandType.RR;

            }
            if (uncle==null)
                uncleColour = null;
            else
                uncleColour = uncle.colour;
        }
    }

    private Node root = null;
    private Node newNode = null; //pointer to the node just added

    public boolean isEmpty() {
        return root==null;
    }

    public void insertRB(int key) {
        root = insertRecursiveRB(root, key);
        checkForLocalRedViolation(newNode);
        root.colour = Colour.BLACK;
    }

    public void insertsRB(int[] keys) {
        for (int key : keys) {
            insertRB(key);
        }
    }

    private Node insertRecursiveRB(Node root, int key) {
        if (root == null) {
            root = new Node(key);
            newNode = root;
        }
        else if (key < root.key) {
            root.left = insertRecursiveRB(root.left, key);
            root.left.parent = root;
        }
        else {
            root.right = insertRecursiveRB(root.right, key);
            root.right.parent = root;
        }
        return root;
    }

    private void checkForLocalRedViolation(Node node) {
        /*  1. Every node is either red or black.
            2. The root is black.
            3. Every leaf (NIL) is black.
            4. If a node is red, then both its children are black.
            5. For each node, all simple paths from the node to descendant leaves contain the
            same number of black nodes. */
        if (isRed(node)) {
            FamilyNode fNode = new FamilyNode(node);
            if (fNode.parentColour == Colour.RED)
                fixRBtree(fNode);
        }
    }

    private void fixRBtree(FamilyNode familyNode) {
        if (familyNode.uncleColour==Colour.RED) {
            redUncleSoRecolour(familyNode);
            checkForLocalRedViolation(familyNode.grandparent);
        }
        else {
            Node blacken = familyNode.parent;
            switch (familyNode.grandType) {
                case LR:
                    leftRotate(familyNode.parent);
                    blacken = familyNode.underlying;
                case LL:
                    rightRotate(familyNode.grandparent);
                    break;

                case RL:
                    rightRotate(familyNode.parent);
                    blacken = familyNode.underlying;
                case RR:
                    leftRotate(familyNode.grandparent);
                    break;
            }
            familyNode.grandparent.colour = Colour.RED;
            blacken.colour = Colour.BLACK;
        }
    }

    private void redUncleSoRecolour(FamilyNode familyNode) {
        familyNode.parent.colour = Colour.BLACK;
        familyNode.uncle.colour = Colour.BLACK;
        familyNode.grandparent.colour = Colour.RED;
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

    public boolean blackViolation() {
        if (isEmpty())
            return false;
        int[] blackHeights = blackHeights();
        if (blackHeights.length==0)
            return true;
        int first = blackHeights[0];
        for (int h: blackHeights)
            if (h != first)
                return true;
        return false;
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
        if (!isRed(node))
            for (int i = 0; i < bhs.size(); i++)
                bhs.set(i, bhs.get(i) + 1);
        return bhs;
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

    private boolean lostParentsRecursive(Node node) {
        return (
                ((node.left  != null) && (node.left.parent  != node || lostParentsRecursive(node.left)) )
                ||
                ((node.right != null) && (node.right.parent != node || lostParentsRecursive(node.right)) )
        );
    }

    public boolean lostParents() {
        return lostParentsRecursive(root);
    }

    private void leftRotate(Node node_x) {
        Node node_y = node_x.right;
        node_y.parent = node_x.parent;

        node_x.right = node_y.left;
        if (node_x.right != null)
            node_x.right.parent = node_x;

        node_y.left = node_x;
        node_x.parent = node_y;

        if (node_y.parent == null)
            root = node_y;
        else if (node_y.parent.left == node_x)
            node_y.parent.left = node_y;
        else if (node_y.parent.right == node_x)
            node_y.parent.right = node_y;
    }

    private void rightRotate(Node node_y) {
        Node node_x = node_y.left;
        node_x.parent = node_y.parent;

        node_y.left = node_x.right;
        if (node_y.left != null)
            node_y.left.parent = node_y;

        node_x.right = node_y;
        node_y.parent = node_x;

        if (node_x.parent == null)
            root = node_x;
        else if (node_x.parent.left == node_y)
            node_x.parent.left = node_x;
        else if (node_x.parent.right == node_y)
            node_x.parent.right = node_x;
    }

    static class Empty extends RuntimeException {
    }

    public static void main(String[] args) {
        BinaryTree tree = new BinaryTree();

        tree.insertsRB(new int[] {11,2,14,1,7,15,5,8});
        System.out.println(tree.redViolation());
        System.out.println(tree.blackViolation());
        TreePrinter.print(tree.root);

        tree.insertRB(4);
        System.out.println(tree.redViolation());
        System.out.println(tree.blackViolation());
        TreePrinter.print(tree.root);

        BinaryTree tree2 = new BinaryTree();
        for (int i = 1; i < 10; i++) {
            tree2.insertRB(i);
            TreePrinter.print(tree2.root);
        }
    }

}