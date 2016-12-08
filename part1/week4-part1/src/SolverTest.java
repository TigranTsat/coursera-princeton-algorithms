
public class SolverTest {

    public static void main(String[] args) {
        System.out.println("Running solver tests");
        runTests1();
    }

    private static void runTests1() {
        final int[][] b1blocks = { { 1, 2, 3 }, { 4, 5, 0 }, { 7, 8, 6 } };
        final Board b1 = new Board(b1blocks);
        final Solver s1 = new Solver(b1);
    }

}
