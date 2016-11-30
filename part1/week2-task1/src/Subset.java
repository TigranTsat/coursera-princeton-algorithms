import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Subset {
    private static boolean probability(double threshold) {
        double rand = StdRandom.uniform((double) 0, (double) 1);
        return rand < threshold;
    }
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        int i = 0;
        while (!StdIn.isEmpty()) {
            String str = StdIn.readString();
            if (i < k) {
                queue.enqueue(str);
            } 
            else {
                boolean doSwap = probability((k) / (double) (i + 1));
                if (doSwap) {
                    // Probability of 1/i
                    queue.dequeue();
                    queue.enqueue(str);
                }
            }
            i++;
        }
        if (i < k) {
            throw new RuntimeException("Missing input. Read: " + i);
        }
        if (queue.size() != k) {
            throw new RuntimeException("Wrong size of " + queue.size());
        }
        for (int j = 0; j < k; j++) {
            String randVal = queue.dequeue();
            System.out.println(randVal);
        }
    }
}
