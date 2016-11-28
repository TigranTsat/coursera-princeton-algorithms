import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final int HIGH_CONNECT = 1;   // = 0x00001
    private static final int LOW_CONNECT = 2;    // = 0x00010
    private static final int WAS_INSPECTED = 4;  // = 0x00100
    
    private final byte[][] elementMarks;
    private final WeightedQuickUnionUF connections;
    private final int connectionsLength;
    private final int n;
    private boolean percholates = false;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        elementMarks = new byte[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
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
        if ((elementMarks[rowI][colI] & WAS_INSPECTED) == WAS_INSPECTED) {
            return; // already opened
        }
        elementMarks[rowI][colI] |= WAS_INSPECTED;
        final int connectionCellPos = rowI * n + colI + 1;

        int newFlag = 0;
        boolean hasTop = false, hasLeft = false, hasRight = false, hasBottom = false;
        // top row
        if (rowI == 0) {
            // connections.union(connectionCellPos, 0);
            newFlag |= HIGH_CONNECT;
        }
        // top
        else if ((rowI - 1 >= 0) && (elementMarks[rowI - 1][colI] & WAS_INSPECTED) == WAS_INSPECTED) {
            hasTop = true;
        }
        // left
        if ((colI - 1 >= 0) && (elementMarks[rowI][colI - 1] & WAS_INSPECTED) == WAS_INSPECTED) {
            hasLeft = true;
        }
        // Right
        if ((colI + 1 < n) && (elementMarks[rowI][colI + 1] & WAS_INSPECTED) == WAS_INSPECTED) {
            hasRight = true;
        }
        // bottom row
        if (rowI == n - 1) {
            // connections.union(connectionCellPos, connectionsLength - 1);
            newFlag |= LOW_CONNECT;
        }
        // bottom
        else if ((rowI < n - 1) && (elementMarks[rowI + 1][colI] & WAS_INSPECTED) == WAS_INSPECTED) {
            hasBottom = true;
            int neighbour = (rowI + 1) * n + colI + 1;
            int neighbourRootI = connections.find(neighbour) - 1;
            newFlag |= elementMarks[neighbourRootI / n][neighbourRootI % n];
            connections.union(connectionCellPos, neighbour);
        }

        // === Do finds ===
        if (hasTop) {
            int neighbour = (rowI - 1) * n + colI + 1;
            int neighbourRootI = connections.find(neighbour) - 1;
            newFlag |= elementMarks[neighbourRootI / n][neighbourRootI % n];
        }
        if (hasLeft) {
            int neighbour = connectionCellPos - 1;
            int neighbourRootI = connections.find(neighbour) - 1;
            newFlag |= elementMarks[neighbourRootI / n][neighbourRootI % n];
        }
        if (hasRight) {
            int neighbour = connectionCellPos + 1;
            int neighbourRoot = connections.find(neighbour) - 1;
            newFlag |= elementMarks[neighbourRoot / n][neighbourRoot % n];
        }
        if (hasBottom) {
            int neighbour = (rowI + 1) * n + colI + 1;
            int neighbourRootI = connections.find(neighbour) - 1;
            newFlag |= elementMarks[neighbourRootI / n][neighbourRootI % n];
        }

        // === Do unions ===
        if (hasTop) {
            int neighbour = (rowI - 1) * n + colI + 1;
            connections.union(connectionCellPos, neighbour);
        }
        if (hasLeft) {
            int neighbour = connectionCellPos - 1;
            connections.union(connectionCellPos, neighbour);
        }
        if (hasRight) {
            int neighbour = connectionCellPos + 1;
            connections.union(connectionCellPos, neighbour);
        }
        if (hasBottom) {
            int neighbour = (rowI + 1) * n + colI + 1;
            connections.union(connectionCellPos, neighbour);
        }

        int newRootI = connections.find(connectionCellPos) - 1;
        elementMarks[newRootI / n][newRootI % n] |= newFlag;

        elementMarks[rowI][colI] |= newFlag;
        // if both flags are set
        if (((newFlag & HIGH_CONNECT) == HIGH_CONNECT) && ((newFlag & LOW_CONNECT) == LOW_CONNECT)) {
            percholates = true;
        }

    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row <= 0 || col <= 0 || row > n || col > n) {
            throw new IndexOutOfBoundsException("Wrong input");
        }
        return (elementMarks[row - 1][col - 1] & WAS_INSPECTED) == WAS_INSPECTED;
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row <= 0 || col <= 0 || row > n || col > n) {
            throw new IndexOutOfBoundsException("Wrong input");
        }
        int elementI = (row - 1) * n + (col - 1) + 1;
        int root1 = connections.find(elementI);
        int root1Flag = this.elementMarks[(root1 - 1) / n][(root1 - 1) % n];
        return ((root1Flag & HIGH_CONNECT) == HIGH_CONNECT);
    }

    // does the system percolate?
    public boolean percolates() {
        // return connections.connected(0, connectionsLength - 1);
        return percholates;
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
        if ((perc1.elementMarks[0][0] & HIGH_CONNECT) != HIGH_CONNECT) {
            throw new RuntimeException("Bit was not set");
        }
        if ((perc1.elementMarks[0][0] & LOW_CONNECT) == LOW_CONNECT) {
            throw new RuntimeException("Wrong bit was set");
        }
        if (perc1.isOpen(3, 3)) {
            throw new RuntimeException("Validation failed");
        }
        if (perc1.percolates()) {
            throw new RuntimeException("Validation failed");
        }

        perc1.open(2, 1);
        perc1.open(2, 2);
        if (!perc1.isOpen(2, 2)) {
            throw new RuntimeException("Validation failed");
        }
        if (!perc1.isFull(2, 2)) {
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
        perc2.open(1, 1);
        if (!perc2.isOpen(1, 1)) {
            throw new RuntimeException("Validation failed");
        }
        if (!perc2.isFull(1, 1)) {
            throw new RuntimeException("Validation failed");
        }
        if (perc2.isOpen(2, 2)) {
            throw new RuntimeException("Validation failed");
        }
        if (perc2.isFull(2, 2)) {
            throw new RuntimeException("Validation failed");
        }

        // ========== Test 3 (backwash test) ===============
        Percolation perc3 = new Percolation(3);
        perc3.open(1, 3);
        if ((perc3.elementMarks[0][2] & HIGH_CONNECT) != HIGH_CONNECT) {
            throw new RuntimeException("Bit was not set");
        }
        if ((perc3.elementMarks[0][2] & LOW_CONNECT) == LOW_CONNECT) {
            throw new RuntimeException("Wrong bit was set");
        }
        perc3.open(2, 3);
        if ((perc3.elementMarks[1][2] & HIGH_CONNECT) != HIGH_CONNECT) {
            throw new RuntimeException("Bit was not set");
        }
        if ((perc3.elementMarks[1][2] & LOW_CONNECT) == LOW_CONNECT) {
            throw new RuntimeException("Wrong bit was set");
        }
        perc3.open(3, 3);
        if ((perc3.elementMarks[2][2] & HIGH_CONNECT) != HIGH_CONNECT) {
            throw new RuntimeException("Bit was not set");
        }
        if ((perc3.elementMarks[2][2] & LOW_CONNECT) != LOW_CONNECT) {
            throw new RuntimeException("Bit was not set");
        }
        perc3.open(3, 1);
        if ((perc3.elementMarks[2][0] & HIGH_CONNECT) == HIGH_CONNECT) {
            throw new RuntimeException("Wrong bit was set");
        }
        if ((perc3.elementMarks[2][0] & LOW_CONNECT) != LOW_CONNECT) {
            throw new RuntimeException("Bit was not set");
        }
        if (!perc3.percolates()) {
            throw new RuntimeException("Validation failed");
        }
        if (perc3.isFull(3, 1)) {
            throw new RuntimeException("Validation failed");
        }
        if (perc3.isFull(3, 2)) {
            throw new RuntimeException("Validation failed");
        }
        if (perc3.isFull(3, 1)) {
            throw new RuntimeException("Validation failed");
        }
        Percolation perc4 = new Percolation(4);
        perc4.open(3, 3);
        perc4.open(2, 3);
        perc4.open(1, 3);
        if (!perc4.isFull(3, 3) || !perc4.isFull(2, 3)) {
            throw new RuntimeException("Validation failed");
        }
        perc4.open(4, 4);
        perc4.open(3, 4);
        if (!perc4.isFull(3, 4)) {
            throw new RuntimeException("Validation failed");
        }
        if (!perc4.percolates()) {
            throw new RuntimeException("Validation failed");
        }
        if (perc4.isFull(2, 4)) {
            throw new RuntimeException("Validation failed");
        }
        System.out.println("Tests success");
    }
}