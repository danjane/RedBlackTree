import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class TreeTimer {

    private static void print(String s) {
        System.out.println(s);
    }

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
        print("  RedViolation    : " + redViolation);

        boolean blackViolation = tree.blackViolation();
        print("  BlackViolation  : " + blackViolation);

        boolean lostParents = tree.lostParents();
        print("  PointerViolation: " + lostParents);

        int expected = 0;
        boolean valuesOk = true;
        for (int i : tree.values())
            valuesOk = valuesOk && i == expected++;
        boolean valueViolation = !valuesOk;
        print("  ValuesCorrupted : " + valueViolation);

        return !(redViolation || blackViolation || lostParents || valueViolation);
    }

    public static long timeSetOfInsertions(int size) {
        BinaryTree tree = new BinaryTree();
        Stack<Integer> ints = shuffledIntegers(size);

        long startTime = System.nanoTime();
        for (int i = 0; i < size; i++)
            tree.insertRB(ints.pop());
        long endTime = System.nanoTime();

        long millis = (endTime - startTime) / 1000000;

        // tree.print(); // too big
        if (!checkTree(tree))
            throw new RuntimeException("Tree fail with size" + size);

        print("Time taken: " + millis + " ms");
        return millis;
    }

    public static StringBuilder appendResult(StringBuilder sb, int size, int time) {
        sb.append(size).append(",").append(time).append("\n");
        return sb;
    }


    public static void main(String[] args) {
        int size = 1000000;



    }


}
