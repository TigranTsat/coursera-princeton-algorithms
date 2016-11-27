public class PercolationStatsTigran {
    public static void main(String[] args) {
        int n = 200;
        int trials = 100;
        PercolationStats stats = new PercolationStats(n, trials);
        System.out.println("Mean = " + stats.mean());
        System.out.println("stddev = " + stats.stddev());
        System.out.println(String.format("95%% confidence interval = %1.8f - %1.8f ", stats.confidenceLo(),
                stats.confidenceHi()));
    }
}
