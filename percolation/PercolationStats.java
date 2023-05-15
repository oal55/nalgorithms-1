/* *****************************************************************************
 *  Name:              Orbay Altuntoprak
 *  Coursera User ID:  16353807
 *  Last modified:     5/1/2023
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {
    private final double pMean, pStddev, pConfidenceLo, pConfidenceHi;

    public PercolationStats(int n, int trials) {
        throwIfIllegalArgument(n <= 0 || trials <= 0);

        int nsquare = n * n;
        double sqrtT = Math.sqrt(trials);
        double[] percolationThresholds = new double[trials];
        for (int i = 0; i < trials; ++i) {
            percolationThresholds[i] = (double) simulate(n) / (double) nsquare;
        }
        pMean = StdStats.mean(percolationThresholds);
        pStddev = StdStats.stddev(percolationThresholds);
        double range = 1.96 * pStddev / sqrtT;
        pConfidenceLo = pMean - range;
        pConfidenceHi = pMean + range;
    }

    private int simulate(int n) {
        Percolation p = new Percolation(n);
        int[] cellsToOpen = StdRandom.permutation(n * n);
        int iCell = 0;
        while (!p.percolates()) {
            int cell = cellsToOpen[iCell++];
            p.open(cell / n + 1, cell % n + 1);
        }
        return iCell;
    }

    // sample mean of percolation threshold
    public double mean() {
        return pMean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return pStddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return pConfidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return pConfidenceHi;
    }

    private void throwIfIllegalArgument(boolean shouldThrow) {
        if (shouldThrow) {
            throw new IllegalArgumentException();
        }
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        PercolationStats p = new PercolationStats(n, t);
        StdOut.printf("mean                     = %.15f\n", p.mean());
        StdOut.printf("stddev                   = %.15f\n", p.stddev());
        StdOut.printf("95 %% confidence interval = [%.15f, %.15f]\n", p.confidenceLo(),
                      p.confidenceHi());

    }

}
