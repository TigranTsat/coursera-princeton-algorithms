import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SubsetTest {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        SubsetTest test = new SubsetTest();
        test.runTest();
        // test.probTest();
        // test.dequeTest();
    }

    // private void probTest() {
    // double desiredProbability = 0.3;
    // int numberOfTrials = 10000;
    // int success = 0;
    // int failures = 0;
    // for (int i = 0; i < numberOfTrials; i++) {
    // boolean res = Subset.probability(desiredProbability);
    // if (res) {
    // success ++;
    // }
    // else {
    // failures++;
    // }
    // }
    // double realProb = success / (double) (success + failures);
    // System.out.println("Test ran. Real:" + realProb + " Desired" +
    // desiredProbability);
    // }

    private void dequeTest() {
        int totalTrialsToRun = 500;
        int[] data = { 1, 2, 3, 4, 5 };
        int[] stat = new int[10];
        for (int i = 0; i < totalTrialsToRun; i++) {
            RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
            for (int element : data) {
                queue.enqueue(element);
            }
            int rand = queue.dequeue();
            stat[rand]++;
        }
        System.out.println("Rand test completed: " + Arrays.toString(stat));
    }

    private void runTest() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PrintStream stdout = System.out;
        String[] args1 = { "3" };
        String str = "1 2 3 4 5 6";
        int[] distib = new int[10];
        int totalTrialsToRun = 600;

        // Hack
        Class<?> c = Class.forName("edu.princeton.cs.algs4.StdIn");
        Method method = c.getDeclaredMethod("resync");
        method.setAccessible(true);

        System.out.println("Running test");
        for (int i = 0; i < totalTrialsToRun; i++) {
            // System.out.println("Iteration " + i);
            InputStream fakeIn = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
            System.setIn(fakeIn);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            System.setOut(ps);

            Object o = method.invoke(c);

            Subset.main(args1);

            String outputContent = new String(baos.toByteArray(), StandardCharsets.UTF_8);
            String[] characters = outputContent.split("\\r?\\n");
            System.setOut(stdout);
            // System.out.println("Read characters = " + characters.length);
            for (String character : characters) {
                int ch = Integer.parseInt(character);
                distib[ch]++;
            }
        }
        System.out.println("Stats: " + Arrays.toString(distib));
    }
}
