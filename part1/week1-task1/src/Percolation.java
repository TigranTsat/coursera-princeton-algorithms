import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final boolean[][] matrix;
    private final byte[][] elementMarks;
    private final WeightedQuickUnionUF connections;
    private final int connectionsLength;
    private final int n;
    private int filled = 0;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        matrix = new boolean[n][n];
        elementMarks = new byte[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = false;
                elementMarks[i][j] = 0;
            }
        }
        connectionsLength = n * n + 2;
        connections = new WeightedQuickUnionUF(connectionsLength);
        this.n = n;
    }

    // open site (row, col) if it is not open
    // already
    public void open(int row, int col) {
        if (row <= 0 || col <= 0 || row > n || col > n) {
            throw new IndexOutOfBoundsException();
        }
        final int rowI = row - 1;
        final int colI = col - 1;
        final boolean cell = matrix[rowI][colI];
        if (cell) {
            return; // already opened
        }
        filled += 1;
        matrix[rowI][colI] = true;
        final int connectionCellPos = rowI * n + colI + 1;
        // top row
        if (rowI == 0) {
            connections.union(connectionCellPos, 0);
        }
        // top
        else if ((rowI - 1 >= 0) && matrix[rowI - 1][colI]) {
            connections.union(connectionCellPos, (rowI - 1) * n + colI + 1);
        }
        // left
        if ((colI - 1 >= 0) && matrix[rowI][colI - 1]) {
            connections.union(connectionCellPos, connectionCellPos - 1);
        }
        // Right
        if ((colI + 1 < n) && matrix[rowI][colI + 1]) {
            connections.union(connectionCellPos, connectionCellPos + 1);
        }
        // bottom row
        if (rowI == n - 1) {
            connections.union(connectionCellPos, connectionsLength - 1);
        }
        // bottom
        else if ((rowI < n - 1) && matrix[rowI + 1][colI]) {
            connections.union(connectionCellPos, (rowI + 1) * n + colI + 1);
        }

    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row <= 0 || col <= 0 || row > n || col > n) {
            throw new IndexOutOfBoundsException("Wrong input");
        }
        return matrix[row - 1][col - 1];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row <= 0 || col <= 0 || row > n || col > n) {
            throw new IndexOutOfBoundsException("Wrong input");
        }
        int elementI = (row - 1) * n + (col - 1) + 1;
        System.out.println("Comparing with " + elementI);
        return connections.connected(0, elementI);
    }

    // does the system percolate?
    public boolean percolates() {
        return connections.connected(0, connectionsLength - 1);
    }

    // test client (optional)
    // INFO: terrible code below, but I cannot use any functions per task :(
    public static void main(String[] args) {
        // Test suit 1
        Percolation perc1 = new Percolation(3);
        if (perc1.percolates()) {
            throw new RuntimeException("Validation failed");
        }
        try {
            perc1.open(0, 0);
            throw new RuntimeException("Validation failed");
        } 
        catch (java.lang.IndexOutOfBoundsException ex) {
            // pass
        }
        try {
            perc1.open(3, 0);
            throw new RuntimeException("Validation failed");
        } 
        catch (java.lang.IndexOutOfBoundsException ex) {
            // pass
        }
        if (perc1.percolates()) {
            throw new RuntimeException("Validation failed");
        }
        perc1.open(1, 1);
        if (perc1.isOpen(3, 3)) {
            throw new RuntimeException("Validation failed");
        }
        if (!perc1.connections.connected(0, 1)) {
            throw new RuntimeException("Validation failed");
        }
        if (perc1.connections.connected(0, 2)) {
            throw new RuntimeException("Validation failed");
        }
        if (perc1.connections.connected(0, 4)) {
            throw new RuntimeException("Validation failed");
        }
        if (perc1.connections.connected(0, 3)) {
            throw new RuntimeException("Validation failed");
        }
        if (perc1.percolates()) {
            throw new RuntimeException("Validation failed");
        }

        perc1.open(2, 1);
        if (!perc1.connections.connected(0, 4)) {
            throw new RuntimeException("Validation failed");
        }
        if (perc1.connections.connected(0, 5)) {
            throw new RuntimeException("Validation failed");
        }
        if (perc1.connections.connected(0, 7)) {
            throw new RuntimeException("Validation failed");
        }
        perc1.open(2, 2);
        if (!perc1.isOpen(2, 2)) {
            throw new RuntimeException("Validation failed");
        }
        if (!perc1.isFull(2, 2)) {
            throw new RuntimeException("Validation failed");
        }
        if (!perc1.connections.connected(0, 5)) {
            throw new RuntimeException("Validation failed");
        }
        if (perc1.connections.connected(0, 6)) {
            throw new RuntimeException("Validation failed");
        }
        if (perc1.connections.connected(0, 7)) {
            throw new RuntimeException("Validation failed");
        }
        if (perc1.connections.connected(0, 8)) {
            throw new RuntimeException("Validation failed");
        }
        if (perc1.connections.connected(0, 9)) {
            throw new RuntimeException("Validation failed");
        }
        if (perc1.percolates()) {
            throw new RuntimeException("Validation failed");
        }
        perc1.open(3, 3);
        if (perc1.percolates()) {
            throw new RuntimeException("Validation failed");
        }
        if (!perc1.isOpen(3, 3)) {
            throw new RuntimeException("Validation failed");
        }
        perc1.open(2, 3);
        if (!perc1.connections.connected(0, 9)) {
            throw new RuntimeException("Validation failed");
        }
        if (!perc1.percolates()) {
            throw new RuntimeException("Validation failed");
        }

        // =========== Test open ===========
        Percolation perc2 = new Percolation(10);
        perc2.open(4, 5);
        if (!perc2.isOpen(4, 5)) {
            throw new RuntimeException("Validation failed");
        }
        if (perc2.isFull(4, 5)) {
            throw new RuntimeException("Validation failed");
        }

        // ========== Test 3 (backwash test) ===============
        Percolation perc3 = new Percolation(3);
        perc3.open(1, 3);
        perc3.open(2, 3);
        perc3.open(3, 3);
        perc3.open(3, 1);
        if (!perc3.percolates()) {
            throw new RuntimeException("Validation failed");
        }
        if (!perc3.isFull(3, 1)) {
            throw new RuntimeException("Validation failed");
        }
        if (perc3.isFull(3, 2)) {
            throw new RuntimeException("Validation failed");
        }
        if (perc3.isFull(3, 1)) {
            throw new RuntimeException("Validation failed");
        }

        System.out.println("Tests success");
    }
}