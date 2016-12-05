/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *  
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import java.util.Comparator;

import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {

    private final int x; // x-coordinate of this point
    private final int y; // y-coordinate of this point

    /**
     * Initializes a new point.
     * 
     * @param x
     *            the <em>x</em>-coordinate of the point
     * @param y
     *            the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point to
     * standard draw.
     * 
     * @param that
     *            the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point. Formally,
     * if the two points are (x0, y0) and (x1, y1), then the slope is (y1 - y0)
     * / (x1 - x0). For completeness, the slope is defined to be +0.0 if the
     * line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical; and
     * Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     * 
     * @param that
     *            the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        if (x == that.x && y == that.y) {
            return Double.NEGATIVE_INFINITY;
        }
        if (x == that.x) {
            return Double.POSITIVE_INFINITY;
        }
        if (y == that.y) {
            return +0.0;
        }
        return (that.y - y) / (double) (that.x - x);
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     * 
     * @param that
     *            the other point
     * @return the value <tt>0</tt> if this point is equal to the argument point
     *         (x0 = x1 and y0 = y1); a negative integer if this point is less
     *         than the argument point; and a positive integer if this point is
     *         greater than the argument point
     */
    public int compareTo(Point that) {
        if (x == that.x && y == that.y) {
            return 0;
        }
        else if (y == that.y) {
            if (x < that.x) {
                return -1;
            }
            else {
                return 1;
            }
        }
        else if (y < that.y) {
            return -1;
        }
        else {
            return 1;
        }
    }

    /**
     * Compares two points by the slope they make with this point. The slope is
     * defined as in the slopeTo() method.
     * 
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        return new PointComparator(this);
    }
    
    private class PointComparator implements Comparator<Point> {

        private Point currentPoint;

        public PointComparator(Point p) {
            if (p == null) {
                throw new IllegalArgumentException("P cannot be null");
            }
            currentPoint = p;
        }

        @Override
        public int compare(Point o1, Point o2) {
            if (o1 == null) {
                throw new NullPointerException();
            }
            if (o2 == null) {
                throw new NullPointerException();
            }
            if (o1 == o2) {
                return 0;
            }
            double sloap1 = currentPoint.slopeTo(o1);
            double sloap2 = currentPoint.slopeTo(o2);
            if (sloap1 < sloap2) {
                return -1;
            } 
            else if (Double.compare(sloap1, sloap2) == 0) {
                return 0;
            } 
            else {
                return 1;
            }
        }
        
    }

    /**
     * Returns a string representation of this point. This method is provide for
     * debugging; your program should not rely on the format of the string
     * representation.
     * 
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        Point p11a = new Point(1, 1);
        Point p11b = new Point(1, 1);
        Point p21 = new Point(2, 1);
        Point p30 = new Point(3, 0);
        if (p11a.compareTo(p11b) != 0) {
            throw new RuntimeException("Validation failed");
        }
        if (p11a.compareTo(p21) != -1) {
            throw new RuntimeException("Validation failed");
        }
        if (p30.compareTo(p21) != -1) {
            throw new RuntimeException("Validation failed");
        }
        if ((new Point(1, 1)).slopeTo(new Point(1, 1)) != Double.NEGATIVE_INFINITY) {
            throw new RuntimeException("Validation failed");
        }
        if ((new Point(1, 1)).slopeTo(new Point(1, 5)) != Double.POSITIVE_INFINITY) {
            throw new RuntimeException("Validation failed");
        }
        if ((new Point(1, 1)).slopeTo(new Point(6, 1)) != 0) {
            throw new RuntimeException("Validation failed");
        }
        if ((new Point(2, 2)).slopeTo(new Point(3, 3)) != 1) {
            throw new RuntimeException("Validation failed");
        }
        if ((new Point(1, 1)).slopeOrder().compare(new Point(2, 2), new Point(3, 3)) != 0) {
            throw new RuntimeException("Validation failed");
        }
        if ((new Point(1, 1)).slopeOrder().compare(new Point(5, 5), new Point(2, 5)) > -1) {
            throw new RuntimeException("Validation failed");
        }
        if ((new Point(1, 1)).slopeOrder().compare(new Point(5, 5), new Point(1, 5)) > -1) {
            throw new RuntimeException("Validation failed");
        }
        if ((new Point(1, 1)).slopeOrder().compare(new Point(5, 5), new Point(5, 1)) < 1) {
            throw new RuntimeException("Validation failed");
        }
        System.out.println("Tests success");
    }
}