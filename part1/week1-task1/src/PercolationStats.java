public class PercolationStats {
    private final double[] probMeasurements;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        probMeasurements = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            int fullSize = n * n;
            int openedCells = 0;
            while (!perc.percolates()) {
                int row = edu.princeton.cs.algs4.StdRandom.uniform(n) + 1;
                int col = edu.princeton.cs.algs4.StdRandom.uniform(n) + 1;
                if (perc.isOpen(row, col)) {
                    continue;
                }
                perc.open(row, col);
                openedCells++;
            }
            probMeasurements[i] = ((double) openedCells / fullSize);
            if (probMeasurements[i] > 1) {
                throw new RuntimeException("Internal check failure");
            }
        }

    }

    // sample mean of percolation threshold
    public double mean() {
        return edu.princeton.cs.algs4.StdStats.mean(probMeasurements);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return edu.princeton.cs.algs4.StdStats.stddev(probMeasurements);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double s = stddev();
        double mean = mean();
        return mean - (1.96 * s) / Math.sqrt(probMeasurements.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double s = stddev();
        double mean = mean();
        return mean + (1.96 * s) / Math.sqrt(probMeasurements.length);
    }

    // test client (described below)
    public static void main(String[] args) {
        int n = -1;
        int trials = -1;
        boolean cmdArguments = true;
        if (cmdArguments) {
            n = Integer.parseInt(args[0]);
            trials = Integer.parseInt(args[1]);
        } 
        else {
            // n = StdIn.readInt();
            // trials = StdIn.readInt();
            // System.out.println(String.format("Running PercolationStats with n = %s and trials = %s",
            // n, trials));
        }
        PercolationStats stats = new PercolationStats(n, trials);
        System.out.println("mean = " + stats.mean());
        System.out.println("stddev = " + stats.stddev());
        System.out.println(String.format("95%% confidence interval = %1.8f, %1.8f ", stats.confidenceLo(),
                stats.confidenceHi()));
    }
}