import org.junit.Test;

import java.util.Stack;

import static org.junit.Assert.*;

public class TreeTimerTest {

    @Test
    public void shuffle1() {
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(0);
        assertEquals(stack, TreeTimer.shuffledIntegers(1));
    }

    @Test
    public void shuffle5() {
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(4);
        stack.push(2);
        stack.push(1);
        stack.push(3);
        stack.push(0);
        assertEquals(stack, TreeTimer.shuffledIntegers(5));
    }

}