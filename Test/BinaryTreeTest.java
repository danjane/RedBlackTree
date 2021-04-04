import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BinaryTreeTest {

    private BinaryTree tree;

    @Before
    public void setUp() {
        tree = new BinaryTree();
    }

    @Test
    public void newTree_isEmpty() {
        assertTrue(tree.isEmpty());
    }

    @Test
    public void addValueToNewTree_notEmpty() {
        tree.insert(1);
        assertFalse(tree.isEmpty());
    }

    @Test
    public void newTree_valuesIsAnEmptyList() {
        int[] empty_list = new int[0];
        assertArrayEquals(empty_list, tree.values());
    }

    @Test
    public void addOneValues_valuesIsThisValue() {
        int[] values = new int[] {1, 1};
        tree.inserts(values);
        assertArrayEquals(values, tree.values());
    }

    @Test(expected = BinaryTree.Empty.class)
    public void willThrowEmpty_WhenAskedForMinimum() {
        tree.minimumValue();
    }

    @Test
    public void add123_minimumIs1() {
        tree.inserts(new int[] {1, 2, 3});
        assertEquals(1, tree.minimumValue());
    }

    @Test
    public void add213_minimumIs1() {
        tree.inserts(new int[] {2, 1, 3});
        assertEquals(1, tree.minimumValue());
    }

    @Test
    public void newTree_returnsFalseOnFind1() {
        assertFalse(tree.find(1));
    }

    @Test
    public void add1_returnsTrueOnFind1() {
        tree.insert(1);
        assertTrue(tree.find(1));
    }

    @Test
    public void add1_returnsFalseOnFind2() {
        tree.insert(1);
        assertFalse(tree.find(2));
    }

    @Test
    public void add123_returnsTrueOnFind3() {
        tree.inserts(new int[] {2, 1, 3});
        assertTrue(tree.find(3));
    }

    @Test(expected = BinaryTree.Empty.class)
    public void newTreeDelete1_throwsEmpty() {
        tree.delete(1);
    }

    @Test
    public void add21_delete1() {
        tree.insert(2);
        tree.insert(1);
        tree.delete(1);
    }

    @Test
    public void add1Delete1_isEmpty() {
        tree.insert(1);
        tree.delete(1);
        assertTrue(tree.isEmpty());
    }

    @Test
    public void add123and4delete4_valuesAre123() {
        int[] values = new int[]{1, 2, 3};
        tree.inserts(values);
        tree.insert(4);
        tree.delete(4);
        assertArrayEquals(values, tree.values());
    }

    @Test
    public void add123delete2_valuesAre13() {
        tree.inserts(new int[]{1,2,3});
        tree.delete(2);
        assertArrayEquals(new int[]{1,3}, tree.values());
    }

    @Test
    public void add321delete2_valuesAre13() {
        tree.inserts(new int[]{3,2,1});
        tree.delete(2);
        assertArrayEquals(new int[]{1,3}, tree.values());
    }

    @Test
    public void add231delete2_valuesAre13() {
        tree.inserts(new int[]{2,3,1});
        tree.delete(2);
        assertArrayEquals(new int[]{1,3}, tree.values());
    }

    @Test
    public void add1_isRed() {
        tree.insert(1);
        assertEquals(BinaryTree.Colour.RED, tree.rootColour());
    }

    @Test
    public void add12_blackHeightsAre1() {
        tree.insert(1);
        tree.insert(2);
        assertArrayEquals(new int[]{1,1,1}, tree.blackHeights());
    }

    @Test
    public void add123_redNodeHasRedChild() {
        tree.inserts(new int[]{1, 2, 3});
        assertTrue(tree.redViolation());
    }

    @Test
    public void insert1_isBlack() {
        tree.insertRB(1);
        assertEquals(BinaryTree.Colour.BLACK, tree.rootColour());
    }

    @Test
    public void inserts12_blackHeightsAre1() {
        tree.insertsRB(new int[]{1,2});
        assertArrayEquals(new int[]{2,2,2}, tree.blackHeights());
    }

    @Test
    public void add21435_values12345() {
        tree.inserts(new int[]{2,1,4,3,5});
        assertArrayEquals(new int[]{1,2,3,4,5}, tree.values());
    }

    @Test
    public void add21435_preorderValues12345() {
        tree.inserts(new int[]{2,1,4,3,5});
        assertArrayEquals(new int[]{2,1,4,3,5}, tree.preorderValues());
    }

    @Test
    public void add21435LeftRotate_is42135() {
        tree.inserts(new int[]{2,1,4,3,5});
        tree.leftRotateOnKey(2);
        assertArrayEquals(new int[]{4,2,1,3,5}, tree.preorderValues());
    }

    @Test
    public void add21435LeftRightRotate_is21435() {
        int[] values = new int[]{2,1,4,3,5};
        tree.inserts(values);
        tree.leftRotateOnKey(2);
        tree.rightRotateOnKey(4);
        assertArrayEquals(values, tree.preorderValues());
    }

    @Test
    public void add2134_RedUncleSoRecolour() {
        tree.insertsRB(new int[]{2,1,3,4});
        assertFalse(tree.redViolation());
    }

    @Test
    public void add3241_RedUncleSoRecolour() {
        tree.insertsRB(new int[]{3,2,4,1});
        assertFalse(tree.redViolation());
    }
}