import edu.princeton.cs.algs4.In;

public class PercolationTimer {
    private static void runFile(String filename) {
        In in = new In(filename);
        int N = in.readInt();
        Percolation perc = new Percolation(N);

        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
            perc.percolates();
        }
    }

    public static void main(String[] args) {
        String filename = "inputFiles/wayne98.txt";
        long total = 0;
        for (int i = 0; i < 100; i++) {
            long start = System.currentTimeMillis();
            runFile(filename);
            long end = System.currentTimeMillis();
            total += end - start;
        }
        System.out.println("ms elapsed: " + total / 100);
    }
}
