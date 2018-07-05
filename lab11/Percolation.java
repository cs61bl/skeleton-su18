// import edu.princeton.cs.algs4.QuickFindUF;
// import edu.princeton.cs.algs4.QuickUnionUF;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    // TODO: Add instance variables here.

    /* Creates an N-by-N grid with all sites initially blocked. */
    public Percolation(int N) {
        // TODO: YOUR CODE HERE
    }

    /* Opens the site (row, col) if it is not open already. */
    public void open(int row, int col) {
        // TODO: YOUR CODE HERE
    }

    /* Returns true if the site at (row, col) is open. */
    public boolean isOpen(int row, int col) {
        // TODO: YOUR CODE HERE
        return false;
    }

    /* Returns true if the site (row, col) is full. */
    public boolean isFull(int row, int col) {
        // TODO: YOUR CODE HERE
        return false;
    }

    /* Returns the number of open sites. */
    public int numberOfOpenSites() {
        // TODO: YOUR CODE HERE
        return 0;
    }

    /* Returns true if the system percolates. */
    public boolean percolates() {
        // TODO: YOUR CODE HERE
        return false;
    }

    /* Converts row and column coordinates into a number. This will be helpful
       when trying to tie in the disjoint sets into our NxN grid of sites. */
    private int xyTo1D(int row, int col) {
        // TODO: YOUR CODE HERE
        return 0;
    }
    /* Returns true if (row, col) site exists in the NxN grid of sites.
       Otherwise, return false. */
    private boolean valid(int row, int col) {
        // TODO: YOUR CODE HERE
        return true;
    }
}
