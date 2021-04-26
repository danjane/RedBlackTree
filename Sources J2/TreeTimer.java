import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class TreeTimer {

    public static Stack<Integer> shuffledIntegers(int size) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++)
            list.add(i);
        java.util.Collections.shuffle(list,  new Random(0));

        Stack<Integer> stack = new Stack<>();
        for (Integer e: list)
            stack.push(e);

        return stack;
    }

    public static boolean checkTree(BinaryTree tree) {
        System.out.println("Checking tree after insertions...");

        boolean redViolation = tree.redViolation();
        System.out.println("  RedViolation    : " + redViolation);

        boolean blackViolation = tree.blackViolation();
        System.out.println("  BlackViolation  : " + blackViolation);

        boolean lostParents = tree.lostParents();
        System.out.println("  PointerViolation: " + lostParents);

        int expected = 0;
        boolean valuesOk = true;
        for (int i : tree.values())
            valuesOk = valuesOk && i == expected++;
        boolean valueViolation = !valuesOk;
        System.out.println("  ValuesCorrupted : " + valueViolation);

        return !(redViolation || blackViolation || lostParents || valueViolation);
    }


    public static void main(String[] args) {
        int size = 1000000;

        BinaryTree tree = new BinaryTree();
        Stack<Integer> ints = shuffledIntegers(size);

        long startTime = System.nanoTime();
        for (int i = 0; i < size; i++)
            tree.insertRB(ints.pop());
        long endTime = System.nanoTime();

        long millis = (endTime - startTime) / 1000000;

        // tree.print(); // too big
        checkTree(tree);
        System.out.println("Time taken: " + millis + " ms");

    }


}
