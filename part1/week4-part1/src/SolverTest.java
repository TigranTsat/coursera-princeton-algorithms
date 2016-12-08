import java.util.ArrayList;


public class SolverTest {

    public static void main(String[] args) {
        System.out.println("Running solver tests");
        runTests1();
        runTests2();
        System.out.println("--- Tests completed ---");
    }

    private static void runTests2() {
        final int[][] b1blocks = { { 0, 1, 3 }, { 4, 2, 5 }, { 7, 8, 6 } };
        final Board b1 = new Board(b1blocks);
        final Solver s1 = new Solver(b1);
        if (!s1.isSolvable()) {
            throw new RuntimeException("Validation failed");
        }
        if (s1.moves() != 4) {
            throw new RuntimeException("Validation failed");
        }
        ArrayList<Board> solution = new ArrayList<Board>();
        for (Board b : s1.solution()) {
            solution.add(b);
        }
        if (solution.size() != 5) {
            throw new RuntimeException("Validation failed: size = " + solution.size());
        }

        // ==== step 0
        final int[][] b1solution1blocks = { { 0, 1, 3 }, { 4, 2, 5 }, { 7, 8, 6 } };
        final Board b1solution1 = new Board(b1solution1blocks);
        if (!b1solution1.equals(solution.get(0))) {
            throw new RuntimeException("Validation failed: solution = " + solution.get(0));
        }

        // ==== step 1
        final int[][] b1solution2blocks = { { 1, 0, 3 }, { 4, 2, 5 }, { 7, 8, 6 } };
        final Board b2solution1 = new Board(b1solution2blocks);
        if (!b2solution1.equals(solution.get(1))) {
            throw new RuntimeException("Validation failed: solution = " + solution.get(1));
        }

    }

    private static void runTests1() {
        final int[][] b1blocks = { { 1, 2, 3 }, { 4, 5, 0 }, { 7, 8, 6 } };
        final Board b1 = new Board(b1blocks);
        final Solver s1 = new Solver(b1);
        if (!s1.isSolvable()) {
            throw new RuntimeException("Validation failed");
        }
        if (s1.moves() != 1) {
            throw new RuntimeException("Validation failed");
        }
        if (s1.solution() == null) {
            throw new RuntimeException("Validation failed");
        }
        ArrayList<Board> solution = new ArrayList<Board>();
        for (Board b : s1.solution()) {
            solution.add(b);
        }
        if (solution.size() != 2) {
            throw new RuntimeException("Validation failed: size = " + solution.size());
        }

        final int[][] b2blocks = { { 1, 2, 3 }, { 4, 5, 6 }, { 8, 7, 0 } };
        final Board b2 = new Board(b2blocks);
        final Solver s2 = new Solver(b2);
        if (s2.isSolvable()) {
            throw new RuntimeException("Validation failed");
        }
        if (s2.moves() != -1) {
            throw new RuntimeException("Validation failed");
        }
        if (s2.solution() != null) {
            throw new RuntimeException("Validation failed");
        }

    }

}
