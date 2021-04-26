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
        tree.insertRB(1);
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
        tree.insertsRB(values);
        assertArrayEquals(values, tree.values());
    }

    @Test(expected = BinaryTree.Empty.class)
    public void willThrowEmpty_WhenAskedForMinimum() {
        tree.minimumValue();
    }

    @Test
    public void add123_minimumIs1() {
        tree.insertsRB(new int[] {1, 2, 3});
        assertEquals(1, tree.minimumValue());
    }

    @Test
    public void add213_minimumIs1() {
        tree.insertsRB(new int[] {2, 1, 3});
        assertEquals(1, tree.minimumValue());
    }

    @Test
    public void newTree_returnsFalseOnFind1() {
        assertFalse(tree.find(1));
    }

    @Test
    public void add1_returnsTrueOnFind1() {
        tree.insertRB(1);
        assertTrue(tree.find(1));
    }

    @Test
    public void add1_returnsFalseOnFind2() {
        tree.insertRB(1);
        assertFalse(tree.find(2));
    }

    @Test
    public void add123_returnsTrueOnFind3() {
        tree.insertsRB(new int[] {2, 1, 3});
        assertTrue(tree.find(3));
    }

    @Test
    public void add12_blackHeightsAre1() {
        tree.insertRB(1);
        tree.insertRB(2);
        assertArrayEquals(new int[]{2,2,2}, tree.blackHeights());
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
        tree.insertsRB(new int[]{2,1,4,3,5});
        assertArrayEquals(new int[]{1,2,3,4,5}, tree.values());
    }

    @Test
    public void add21435_preorderValues12345() {
        tree.insertsRB(new int[]{2,1,4,3,5});
        assertArrayEquals(new int[]{2,1,4,3,5}, tree.preorderValues());
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

    @Test
    public void add123_RRrotation() {
        int[] values = new int[]{1,2,3};
        tree.insertsRB(values);
        assertArrayEquals(values, tree.values());
        assertFalse(tree.redViolation());
    }

    @Test
    public void add12345_checkParentPointers() {
        int[] values = new int[]{1,2,3,4,5};
        tree.insertsRB(values);
        assertArrayEquals(values, tree.values());
        assertFalse(tree.lostParents());
    }

    @Test
    public void add321_LLrotation() {
        tree.insertsRB(new int[]{3,2,1});
        assertArrayEquals(new int[]{1,2,3}, tree.values());
        assertFalse(tree.redViolation());
    }

    @Test
    public void add132_RLrotation() {
        tree.insertsRB(new int[]{1,3,2});
        assertArrayEquals(new int[]{1,2,3}, tree.values());
        assertFalse(tree.blackViolation());
        assertFalse(tree.redViolation());
    }

    @Test
    public void add12345_noBlackViolation() {
        int[] values = new int[]{1,2,3,4,5};
        tree.insertsRB(values);
        assertArrayEquals(values, tree.values());
        assertFalse(tree.blackViolation());
        assertFalse(tree.redViolation());
    }
}