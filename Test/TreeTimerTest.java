import org.junit.Test;

import java.util.Stack;

import static org.junit.Assert.*;

public class TreeTimerTest {

    @Test
    public void shuffle1() {
        Stack<Integer> stack = new Stack<>();
        stack.push(0);
        assertEquals(stack, TreeTimer.shuffledIntegers(1));
    }

    @Test
    public void shuffle5() {
        Stack<Integer> stack = new Stack<>();
        stack.push(4);
        stack.push(2);
        stack.push(1);
        stack.push(3);
        stack.push(0);
        assertEquals(stack, TreeTimer.shuffledIntegers(5));
    }

    @Test
    public void tree_ok() {
        BinaryTree tree = new BinaryTree();
        tree.insertsRB(new int[] {7,1,5,0,4,6,3,2});
        assertTrue(TreeTimer.checkTree(tree));
    }

    @Test
    public void timer_returnsPositiveLong() {
        long millis = TreeTimer.timeSetOfInsertions(100);
        assertTrue(millis > 0);
    }

    @Test
    public void stringbuilder_empty() {
        StringBuilder sb = new StringBuilder();
        assertSame("", sb.toString());
    }

    @Test
    public void stringbuilder_1and2() {
        StringBuilder sb = new StringBuilder("Size,Time\n");
        sb = TreeTimer.appendResult(sb, 1,2);
        assertEquals("Size,Time\n1,2\n", sb.toString());
    }

}