import java.util.ArrayList;
import java.util.Iterator;

public class Board {
    private final int n;
    private int[][] blocks;
    private int zeroRow, zeroCol;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        if (blocks == null) {
            throw new NullPointerException("blocks cannot be null");
        }
        n = blocks.length;
        if (n < 2) {
            throw new RuntimeException("n cannot be < 2");
        }
        this.blocks = new int[n][n];
        for (int i = 0; i < n; i++) {
            if (blocks[i].length != n) {
                throw new RuntimeException("Not square input");
            }
            for (int j = 0; j < n; j++) {
                this.blocks[i][j] = blocks[i][j];
                if (this.blocks[i][j] == 0) {
                    zeroRow = i;
                    zeroCol = j;
                }
            }
        }
    }

    private Board deepClone() {
        return new Board(blocks);
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of blocks out of place
    public int hamming() {
        int missCounter = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (row == n - 1 && col == n - 1) {
                    break;
                }
                int goalNumber = row * n + col + 1;
                if (this.blocks[row][col] != goalNumber) {
                    missCounter++;
                }
            }
        }
        return missCounter;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int missCounter = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                final int cellVal = blocks[row][col];
                if (cellVal == 0) {
                    continue;
                }
                int cellShouldBeRow = (cellVal - 1) / n;
                int cellShouldBeCol = (cellVal - 1) % n;
                int cellManhattan = Math.abs((cellShouldBeRow - row)) + Math.abs((cellShouldBeCol - col));
                missCounter += cellManhattan;
            }
        }
        return missCounter;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (row == n - 1 && col == n - 1) {
                    break;
                }
                int goalNumber = row * n + col + 1;
                if (this.blocks[row][col] != goalNumber) {
                    return false;
                }
            }
        }
        return true;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        Board twin = deepClone();
        if (twin.blocks[0][0] != 0 && twin.blocks[0][1] != 0) {
            // use 0,0 <---> 0, 1
            twin.swap(0, 0, 0, 1);
        } 
        else {
            // use 1,0 <---> 1, 1
            twin.swap(1, 0, 1, 1);
        }
        validateZero();
        return twin;
    }

    private void swap(int row1, int col1, int row2, int col2) {
        final int first = blocks[row1][col1];
        blocks[row1][col1] = blocks[row2][col2];
        blocks[row2][col2] = first;
        if (blocks[row1][col1] == 0) {
            zeroRow = row1;
            zeroCol = col1;
        }
        if (blocks[row2][col2] == 0) {
            zeroRow = row2;
            zeroCol = col2;
        }
    }

    private void validateZero() {
        if (blocks[zeroRow][zeroCol] != 0) {
            throw new RuntimeException("Zero cell validation failure");
        }
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if (y == this) {
            return true;
        }
        if (!(y.getClass().equals(getClass()))) {
            return false;
        }
        Board b = (Board) y;
        if (b.n != this.n) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (b.blocks[i][j] != this.blocks[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return new BoardNeighborsIterable(this);
    }
    
    private class BoardNeighborsIterable implements Iterable<Board> {
        private final ArrayList<Board> boardNeighborsList;

        public BoardNeighborsIterable(Board b) {
            final int maxNeighbors = 4;
            boardNeighborsList = new ArrayList<Board>(maxNeighbors);
            b.validateZero();
            // left cell move
            if (b.zeroRow > 0) {
                Board leftN = new Board(b.blocks);
                leftN.swap(leftN.zeroRow, leftN.zeroCol, leftN.zeroRow - 1, leftN.zeroCol);
                boardNeighborsList.add(leftN);
            }
            // right cell move
            if (b.zeroRow < b.n - 1) {
                Board rightN = new Board(b.blocks);
                rightN.swap(rightN.zeroRow, rightN.zeroCol, rightN.zeroRow + 1, rightN.zeroCol);
                boardNeighborsList.add(rightN);
            }
            // top cell move
            if (b.zeroCol > 0) {
                Board topN = new Board(b.blocks);
                topN.swap(topN.zeroRow, topN.zeroCol, topN.zeroRow, topN.zeroCol - 1);
                boardNeighborsList.add(topN);
            }
            // bottom cell move
            if (b.zeroCol < b.n - 1) {
                Board bottomN = new Board(b.blocks);
                bottomN.swap(bottomN.zeroRow, bottomN.zeroCol, bottomN.zeroRow, bottomN.zeroCol + 1);
                boardNeighborsList.add(bottomN);
            }

        }

        @Override
        public Iterator<Board> iterator() {
            return boardNeighborsList.iterator();
        }
    }


    // string representation of this board (in the output format specified
    // below)
    public String toString() {
        final int maxDigit = n * n;
        final int nDigits = (int) (Math.log10(maxDigit) + 1);
        final int digitSpace = nDigits + 1;
        StringBuilder result = new StringBuilder(maxDigit * digitSpace + n * ("\n".length() + 1));
        final String formatter = "%" + digitSpace + "d";
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                result.append(String.format(formatter, blocks[row][col]));
            }
            if (row != n - 1) {
                result.append("\n");
            }
        }
        return result.toString();
    }

    // unit tests (not graded)
    public static void main(String[] args) {
        runBoardBasicTests1();
        runBoardBasicTests2();
        runBoardTestInterable1();
    }

    private static void runBoardBasicTests1() {
        final int[][] b1blocks = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        Board b1 = new Board(b1blocks);
        System.out.println(b1);
        int b1hamming = b1.hamming();
        if (b1hamming != 5) {
            throw new RuntimeException("Validation failed for hamming: " + b1hamming);
        }
        int b1manhattan = b1.manhattan();
        if (b1manhattan != 10) {
            throw new RuntimeException("Validation failed for manhattan: " + b1manhattan);
        }
        if (b1.isGoal()) {
            throw new RuntimeException("Validation failed for goal");
        }
        final int[][] b2blocks = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        Board b2 = new Board(b2blocks);
        if (!b2.equals(b1)) {
            throw new RuntimeException("Validation failed for equal");
        }
        final int[][] b3blocks = { { 8, 3, 1 }, { 4, 0, 2 }, { 7, 6, 5 } };
        Board b3 = new Board(b3blocks);
        if (b2.equals(b3)) {
            throw new RuntimeException("Validation failed for equal");
        }
        if (b3.isGoal()) {
            throw new RuntimeException("Validation failed for goal");
        }
    }

    private static void runBoardBasicTests2() {
        final int[][] b01blocks = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
        Board b01 = new Board(b01blocks);
        if (!b01.isGoal()) {
            throw new RuntimeException("Validation failed for goal");
        }
        // clone and twin
        final int[][] b02blocks = { { 0, 1 }, { 2, 3 } };
        Board b02 = new Board(b02blocks);
        Board b02copy = b02.deepClone();
        b02.blocks[1][1] = 12;
        if (b02copy.blocks[1][1] != 3) {
            throw new RuntimeException("Deep copy should create new arrray");
        }
        b02.blocks[1][1] = 3;
        Board b02twin = b02.twin();
        if (b02twin.equals(b01)) {
            throw new RuntimeException("Twin is wrong");
        }
    }

    private static void runBoardTestInterable1() {
        final int[][] b1blocks = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        Board b1 = new Board(b1blocks);
        if (b1.zeroCol != 1 || b1.zeroRow != 1) {
            throw new RuntimeException("Validation failed");
        }
        final ArrayList<Board> b1neighbors = new ArrayList<Board>();
        for (Board b : b1.neighbors()) {
            b1neighbors.add(b);
        }
        if (b1neighbors.size() != 4) {
            throw new RuntimeException("Wrong b1neighbors: " + b1neighbors.toString());
        }

        // =============
        final int[][] b1topNblocks = { { 8, 0, 3 }, { 4, 1, 2 }, { 7, 6, 5 } };
        final int[][] b1leftNblocks = { { 8, 1, 3 }, { 0, 4, 2 }, { 7, 6, 5 } };
        final int[][] b1rightNblocks = { { 8, 1, 3 }, { 4, 2, 0 }, { 7, 6, 5 } };
        final int[][] b1bottomNblocks = { { 8, 1, 3 }, { 4, 6, 2 }, { 7, 0, 5 } };

        Board b1topN = new Board(b1topNblocks);
        Board b1leftN = new Board(b1leftNblocks);
        Board b1rightN = new Board(b1rightNblocks);
        Board b1botomN = new Board(b1bottomNblocks);
        boolean b1topNFound = false;
        boolean b1leftNFound = false;
        boolean b1rightNFound = false;
        boolean b1bottomNFound = false;

        for (Board b : b1neighbors) {
            if (b.equals(b1topN)) {
                b1topNFound = true;
            }
            if (b.equals(b1leftN)) {
                b1leftNFound = true;
            }
            if (b.equals(b1rightN)) {
                b1rightNFound = true;
            }
            if (b.equals(b1botomN)) {
                b1bottomNFound = true;
            }
        }
        if (!b1topNFound) {
            throw new RuntimeException("Validation failed");
        }
        if (!b1leftNFound) {
            throw new RuntimeException("Validation failed");
        }
        if (!b1rightNFound) {
            throw new RuntimeException("Validation failed");
        }
        if (!b1bottomNFound) {
            throw new RuntimeException("Validation failed");
        }
    }
}
