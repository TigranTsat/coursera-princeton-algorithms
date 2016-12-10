import java.awt.Point;
import java.util.ArrayList;
import java.util.TreeSet;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {
    private final TreeSet<Point2D> points = new TreeSet<Point2D>();

    // construct an empty set of points
    public PointSET() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : points) {
            p.draw();
        }
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new NullPointerException();
        }
        final ArrayList<Point2D> pointsInsideRect = new ArrayList<Point2D>();
        for (Point2D point : points) {
            if (rect.contains(point)) {
                pointsInsideRect.add(point);
            }
        }
        return pointsInsideRect;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }
        double distance = Double.MAX_VALUE;
        Point2D nearestP = null;

        for (Point2D point : points) {
            final double distanceToTest = p.distanceTo(point);
            if (distanceToTest < distance) {
                distance = distanceToTest;
                nearestP = point;
            }
        }
        return nearestP;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        runTests01();
        System.out.println("Tests completed for PointSET");
    }

    private static void runTests01() {
        PointSET pSet01 = new PointSET();
        pSet01.insert(new Point2D(0, 0));
        pSet01.insert(new Point2D(0, 5));
        pSet01.insert(new Point2D(5, 0));
        pSet01.insert(new Point2D(5, 5));
        pSet01.insert(new Point2D(2.5, 2.5));
        pSet01.insert(new Point2D(5, 5));
        if (pSet01.size() != 5) {
            throw new RuntimeException("Validation failed");
        }
        if (!pSet01.contains(new Point2D(5, 5))) {
            throw new RuntimeException("Validation failed");
        }
        if (pSet01.contains(new Point2D(10, 10))) {
            throw new RuntimeException("Validation failed");
        }

        ArrayList<Point2D> points01a = new ArrayList<Point2D>();
        for (Point2D p : pSet01.range(new RectHV(-1, -1, 10, 10))) {
            points01a.add(p);
        }
        if (points01a.size() != 5) {
            throw new RuntimeException("Validation failed");
        }

        ArrayList<Point2D> points01b = new ArrayList<Point2D>();
        for (Point2D p : pSet01.range(new RectHV(-1, -1, 2, 2))) {
            points01b.add(p);
        }
        if (points01b.size() != 1) {
            throw new RuntimeException("Validation failed");
        }
        
        if (!(new Point(25, 25).equals(new Point(25, 25)))) {
            throw new RuntimeException("Validation failed for Point");
        }

        final Point2D nearestTo11 = pSet01.nearest(new Point2D(1, 1));
        final Point2D expectedTo11 = new Point2D(0, 0);
        if (!nearestTo11.equals(expectedTo11)) {
            throw new RuntimeException("Validation failed: returned " + nearestTo11 + " expected " + expectedTo11);
        }

    }
}
