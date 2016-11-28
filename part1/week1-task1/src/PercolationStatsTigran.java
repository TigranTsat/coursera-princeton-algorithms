public class PercolationStatsTigran {
    public static void main(String[] args) {
        int n = 200;
        int trials = 100;
        PercolationStats stats = new PercolationStats(n, trials);
        System.out.println("Mean = " + stats.mean());
        System.out.println("stddev = " + stats.stddev());
        System.out.println(String.format("95%% confidence interval = %1.8f - %1.8f ", stats.confidenceLo(),
                stats.confidenceHi()));
        /*
         * % java PercolationStats 200 100 
         * mean = 0.5929934999999997 
         * stddev = 0.00876990421552567 
         * 95% confidence interval = 0.5912745987737567, 0.5947124012262428
         */
    }
}
