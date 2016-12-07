
public class Board {
    private final int n;
    private int[][] blocks;
    private int numberOfMoves = 0;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        if (blocks == null) {
            throw new NullPointerException("blocks cannot be null");
        }
        n = blocks.length;
        this.blocks = new int[n][n];
        for (int i = 0; i < n; i++) {
            if (blocks[i].length != n) {
                throw new RuntimeException("Not square input");
            }
            for (int j = 0; j < n; j++) {
                this.blocks[i][j] = blocks[i][j];
            }
        }
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
        return missCounter + this.numberOfMoves;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        // TODO
        return 0;
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
        // TODO:
        return null;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) {
            throw new NullPointerException();
        }
        if (y == this) {
            return true;
        }
        if (!(y instanceof Board)) {
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
        // TODO:
        return null;
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
        final int[][] b1blocks = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        Board b1 = new Board(b1blocks);
        System.out.println(b1);
        int b1hamming = b1.hamming();
        if (b1hamming != 5) {
            throw new RuntimeException("Validation failed for hamming: " + b1hamming);
        }
        if (b1.isGoal()) {
            throw new RuntimeException("Validation failed for goal");
        }
    }
}
