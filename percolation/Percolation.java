/* *****************************************************************************
 *  Name:              Orbay Altuntoprak
 *  Coursera User ID:  16353807
 *  Last modified:     5/1/2023
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public final class Percolation {
    private boolean[][] grid; // grid[i][j] == isOpen(i, j);
    private int numOpenCells;
    private final int gridSize, iLast, iFirst;
    private WeightedQuickUnionUF unionFindPercolation, unionFindCellFull;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        throwIfIllegalArgument(n <= 0);
        grid = new boolean[n][n];
        gridSize = n;
        iLast = n * n + 1;
        iFirst = 0;
        numOpenCells = 0;
        unionFindPercolation = new WeightedQuickUnionUF(n * n + 2);
        unionFindCellFull = new WeightedQuickUnionUF(n * n + 1);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (isOpen(row, col)) {
            return;
        }
        grid[row - 1][col - 1] = true;
        numOpenCells++;
        int srcUnionFindId = unionFindIdOf(row, col);
        connecIfNeighborIstOpen(srcUnionFindId, row - 1, col);
        connecIfNeighborIstOpen(srcUnionFindId, row + 1, col);
        connecIfNeighborIstOpen(srcUnionFindId, row, col - 1);
        connecIfNeighborIstOpen(srcUnionFindId, row, col + 1);
        connectToVirtualNodesIfTopOrBottom(srcUnionFindId);
    }

    private void connectToVirtualNodesIfTopOrBottom(int srcUnionFindId) {
        if (srcUnionFindId <= gridSize) {
            unionFindCellFull.union(srcUnionFindId, iFirst);
            unionFindPercolation.union(srcUnionFindId, iFirst);
        }
        if (srcUnionFindId + gridSize >= iLast) {
            unionFindPercolation.union(srcUnionFindId, iLast);
        }
    }

    // srcRow and srcCol are already sanitized
    private void connecIfNeighborIstOpen(int srcUnionFindId, int dstRow, int dstCol) {
        if (coordinatesInGrid(dstRow, dstCol) && isOpenUnsafe(dstRow, dstCol)) {
            unionFindCellFull.union(srcUnionFindId, unionFindIdOf(dstRow, dstCol));
            unionFindPercolation.union(srcUnionFindId, unionFindIdOf(dstRow, dstCol));
        }
    }

    private int unionFindIdOf(int row, int col) {
        return (row - 1) * gridSize + col;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        throwIfIllegalArgument(!coordinatesInGrid(row, col));
        return isOpenUnsafe(row, col);
    }

    private boolean isOpenUnsafe(int row, int col) {
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return isOpen(row, col) && (unionFindCellFull.find(iFirst) == unionFindCellFull.find(
                unionFindIdOf(row, col)));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpenCells;
    }

    // does the system percolate?
    public boolean percolates() {
        return unionFindPercolation.find(iFirst) == unionFindPercolation.find(iLast);
    }

    private void throwIfIllegalArgument(boolean shouldThrow) {
        if (shouldThrow) {
            throw new IllegalArgumentException();
        }
    }

    private boolean coordinatesInGrid(int row, int col) {
        return (1 <= row && row <= gridSize) && (1 <= col && col <= gridSize);
    }
}
