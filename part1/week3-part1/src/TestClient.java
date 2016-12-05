import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class TestClient {

    public static void main(String[] args) {
        TestClient tClient = new TestClient();
        tClient.runTests();
    }

    private void runTests() {
        Point[] pointsA = constructSetA();
        Point[] pointsB = constructSetB();
        Point[] pointsC = constructSetC();
        Point[] constructSetE = constructSetE();

        draw(pointsA, DrawType.BRUTE_FORCE);
        draw(pointsB, DrawType.BRUTE_FORCE);
        try {
            draw(pointsC, DrawType.BRUTE_FORCE);
            throw new RuntimeException("Exception should have been thrown");
        } catch (IllegalArgumentException ex) {
            // nope
        }
        try {
            draw(constructSetE, DrawType.BRUTE_FORCE);
            throw new RuntimeException("Exception should have been thrown");
        } catch (IllegalArgumentException ex) {
            // nope
        }
        // ===== Fast
        System.out.println("----------------------------");
        draw(pointsA, DrawType.FAST);
        draw(pointsB, DrawType.FAST);
        try {
            draw(pointsC, DrawType.FAST);
            throw new RuntimeException("Exception should have been thrown");
        } catch (IllegalArgumentException ex) {
            // nope
        }
        try {
            draw(constructSetE, DrawType.FAST);
            throw new RuntimeException("Exception should have been thrown");
        } catch (IllegalArgumentException ex) {
            // nope
        }
        Point[] pointsDfast = constructSetDfast();
        draw(pointsDfast, DrawType.FAST);
    }

    private Point[] readPointsFromStdIn(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        return points;
    }

    private Point[] constructSetA() {
        Point[] points = new Point[5];
        points[0] = new Point(19000, 10000);
        points[1] = new Point(18000, 10000);
        points[2] = new Point(32000, 10000);
        points[3] = new Point(1234, 5678);
        points[4] = new Point(14000, 10000);
        return points;
    }

    private Point[] constructSetB() {
        Point[] points = new Point[8];
        points[0] = new Point(10000, 0);
        points[1] = new Point(0, 10000);
        points[2] = new Point(3000, 7000);
        points[3] = new Point(7000, 3000);
        points[4] = new Point(20000, 21000);
        points[5] = new Point(3000, 4000);
        points[6] = new Point(14000, 15000);
        points[7] = new Point(6000, 7000);
        return points;
    }

    private Point[] constructSetC() {
        Point[] points = new Point[4];
        points[0] = new Point(0, 0);
        points[1] = new Point(1, 1);
        points[2] = new Point(2, 2);
        points[3] = new Point(0, 0);
        return points;
    }

    private Point[] constructSetDfast() {
        Point[] points = new Point[6];
        points[0] = new Point(19000, 10000);
        points[1] = new Point(18000, 10000);
        points[2] = new Point(32000, 10000);
        points[3] = new Point(1234, 5678);
        points[4] = new Point(14000, 10000);
        points[5] = new Point(21000, 10000);
        return points;
    }

    private Point[] constructSetE() {
        Point[] points = new Point[3];
        points[0] = new Point(1, 1);
        points[1] = new Point(0, 0);
        points[2] = new Point(0, 0);
        return points;
    }

    private void draw(Point[] points, DrawType type) {
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        if (type == DrawType.BRUTE_FORCE) {
            BruteCollinearPoints collinear = new BruteCollinearPoints(points);
            for (LineSegment segment : collinear.segments()) {
                StdOut.println(segment);
                segment.draw();
            }
        }
        if (type == DrawType.FAST) {
            FastCollinearPoints collinear = new FastCollinearPoints(points);
            for (LineSegment segment : collinear.segments()) {
                StdOut.println(segment);
                segment.draw();
            }
        }
        StdDraw.show();
    }

    private enum DrawType {
        BRUTE_FORCE, FAST
    }
}
