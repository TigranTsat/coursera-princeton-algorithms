import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

public class KdTree {
    private static final double DRAW_LINE_SIZE = 0.001;
    private static final double DRAW_POINT_SIZE = 0.007;

    private int size = 0;
    private Node rootNode = null;
    private double minX = Double.MAX_VALUE;
    private double minY = Double.MAX_VALUE;
    private double maxX = Double.MIN_VALUE;
    private double maxY = Double.MIN_VALUE;

    // construct an empty set of points
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    private void updateMinMaxRegion(Point2D p) {
        final double x = p.x();
        final double y = p.y();
        if (x > maxX) {
            maxX = x;
        }
        if (x < minX) {
            minX = x;
        }
        if (y > maxY) {
            maxY = y;
        }
        if (y < minY) {
            minY = y;
        }
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        updateMinMaxRegion(p);
        
        int level = 0;
        Node prevNode = null;
        Node currentNode = rootNode;
        boolean exploreLeft = false;
        if (rootNode == null) {
            rootNode = new Node(p, true);
            size++;
            return;
        }
        while (true) {
            if (currentNode == null) {
                break;
            }
            prevNode = currentNode;
            if (level % 2 == 0) {
                // compare by x
                if (currentNode.point.equals(p)) {
                    return;
                }
                double x = currentNode.point.x();
                if (p.x() < x) {
                    currentNode = currentNode.left;
                    exploreLeft = true;
                }
                else {
                    currentNode = currentNode.right;
                    exploreLeft = false;
                }
            } 
            else {
                // compare by y
                if (currentNode.point.equals(p)) {
                    return;
                }
                double y = currentNode.point.y();
                if (p.y() < y) {
                    currentNode = currentNode.left;
                    exploreLeft = true;
                }
                else {
                    currentNode = currentNode.right;
                    exploreLeft = false;
                }
            }
            level++;
        }
        boolean newNodeRed = false;
        if (level % 2 == 0) {
            newNodeRed = true;
        }
        if (exploreLeft) {
            testNull(prevNode.left);
            prevNode.left = new Node(p, newNodeRed);
        }
        else {
            testNull(prevNode.right);
            prevNode.right = new Node(p, newNodeRed);
        }
        size++;
    }

    private void testNull(Node n) {
        if (n != null) {
            throw new RuntimeException("Node is not null, but: " + n);
        }
    }


    // does the set contain point p?
    public boolean contains(Point2D p) {
        return contains(p, false);
    }

    private boolean contains(Point2D p, boolean printPath) {

        int level = 0;
        Node currentNode = rootNode;
        if (rootNode == null) {
            return false;
        }

        boolean lastActionLeft = false; // used for printing
        while (true) {
            if (currentNode == null) {
                return false;
            }
            if (printPath) {
                System.out.println("prev. left ? " + lastActionLeft + " ---> " + currentNode);
            }
            if (level % 2 == 0) {
                // compare by x
                if (currentNode.point.equals(p)) {
                    return true;
                }
                double x = currentNode.point.x();
                if (p.x() < x) {
                    currentNode = currentNode.left;
                    lastActionLeft = true;
                } 
                else {
                    currentNode = currentNode.right;
                    lastActionLeft = false;
                }
            } 
            else {
                // compare by y
                if (currentNode.point.equals(p)) {
                    return true;
                }
                double y = currentNode.point.y();
                if (p.y() < y) {
                    currentNode = currentNode.left;
                    lastActionLeft = true;
                } 
                else {
                    currentNode = currentNode.right;
                    lastActionLeft = false;
                }
            }
            level++;
        }
    }

    // draw all points to standard draw
    public void draw() {
        if (rootNode == null) {
            return;
        }
        final double scale = 0.025;
        final double scaleXmin = minX - Math.abs(minX * scale);
        final double scaleXmax = maxX + Math.abs(maxX * scale);
        final double scaleYmin = minY - Math.abs(minY * scale);
        final double scaleYmax = maxY + Math.abs(maxY * scale);
        
        StdDraw.setXscale(scaleXmin, scaleXmax);
        StdDraw.setYscale(scaleYmin, scaleYmax);
        // System.out.println("Set scale X = [" + scaleXmin + " ," + scaleXmax +
        // " ];" + "Set scale Y = [" + scaleYmin + " ," + scaleYmax + " ];");
        draw(rootNode, scaleXmin, scaleXmax, scaleYmin, scaleYmax);
    }
    
    private void draw(Node n, double xMin, double xMax, double yMin, double yMax) {
        if (n == null) {
            return;
        }
        if (xMin > xMax) {
            throw new RuntimeException("Scale x validation failed");
        }
        if (yMin > yMax) {
            throw new RuntimeException("Scale y validation failed");
        }

        StdDraw.setPenRadius(DRAW_POINT_SIZE);
        StdDraw.setPenColor(Color.BLACK);
        final Point2D p = n.point;
        p.draw();
        StdDraw.setPenRadius(DRAW_LINE_SIZE);
        if (n.red) {
            // vertical - x split
            StdDraw.setPenColor(Color.RED);
            final double x = p.x();
            StdDraw.line(x, yMin, x, yMax);
        } 
        else {
            // horizontal - y split
            StdDraw.setPenColor(Color.BLUE);
            final double y = p.y();
            StdDraw.line(xMin, y, xMax, y);
        }
        if (n.left != null) {
            if (n.red) {
                draw(n.left, xMin, p.x(), yMin, yMax);
            }
            else {
                draw(n.left, xMin, xMax, yMin, p.y());
            }
        }
        if (n.right != null) {
            if (n.red) {
                draw(n.right, p.x(), xMax, yMin, yMax);
            }
            else {
                draw(n.right, xMin, xMax, p.y(), yMax);
            }
        }
        
    }


    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        final Queue<Node> nodesToExplore = new LinkedList<Node>();
        final List<Point2D> pointsFitIntoRange = new ArrayList<Point2D>();
        if (rootNode == null) {
            return pointsFitIntoRange;
        }
        nodesToExplore.add(rootNode);
        while (true) {
            if (nodesToExplore.size() == 0) {
                break;
            }
            final Node n = nodesToExplore.poll();
            final Point2D p = n.point;
            if (n.red) {
                // x limit
                boolean exploreRight = false;
                boolean exploreLeft = false;
                if (rect.xmin() < p.x() && rect.xmax() < p.x()) {
                    exploreLeft = true;
                }
                else if (rect.xmin() > p.x() && rect.xmax() > p.x()) {
                    exploreRight = true;
                }
                else {
                    // part left, part right
                    exploreRight = true;
                    exploreLeft = true;
                    if (rect.contains(p)) {
                        pointsFitIntoRange.add(p);
                    }
                }

                if (exploreRight && n.right != null) {
                    nodesToExplore.add(n.right);
                }
                if (exploreLeft && n.left != null) {
                    nodesToExplore.add(n.left);
                }
            } 
            else {
                // y limit
                boolean exploreRight = false;
                boolean exploreLeft = false;
                if (rect.ymin() < p.y() && rect.ymax() < p.y()) {
                    exploreLeft = true;
                }
                else if (rect.ymin() > p.y() && rect.ymax() > p.y()) {
                    exploreRight = true;
                }
                else {
                    // part left, part right
                    exploreRight = true;
                    exploreLeft = true;
                    if (rect.contains(p)) {
                        pointsFitIntoRange.add(p);
                    }
                }

                if (exploreRight && n.right != null) {
                    nodesToExplore.add(n.right);
                }
                if (exploreLeft && n.left != null) {
                    nodesToExplore.add(n.left);
                }
            }
        }
        return pointsFitIntoRange;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (rootNode == null) {
            return null;
        }
        return nearest(rootNode, p, this.minX, this.maxX, this.minY, this.maxY);
    }

    private Point2D nearest(Node n, Point2D targetP, double xMin, double xMax, double yMin, double yMax) {
        if (n == null) {
            return null;
        }
        if (xMin > xMax) {
            throw new RuntimeException("nearest x validation failed");
        }
        if (yMin > yMax) {
            throw new RuntimeException("nearest y validation failed");
        }
        final double targetpX = targetP.x();
        final double targetpY = targetP.y();
        final Point2D p = n.point;
        if (p.x() > xMax || p.x() < xMin) {
            throw new RuntimeException(String.format(
                    "Point coordiantes wrong for x(%s): supplied (min, max) = (%s, %s)", p.x(), xMin, xMax));
        }
        if (p.y() > yMax || p.y() < yMin) {
            throw new RuntimeException("Point coordiantes wrong for y:");
        }

        Point2D smallestLeft = null;
        Point2D smallestRight = null;
        // System.out.println("Processing node: " + n);

        if (n.red) {
            // x compare
            if (targetpX < p.x()) {
                smallestLeft = nearest(n.left, targetP, xMin, p.x(), yMin, yMax);
                // System.out.println(String.format(
                // "About to create rect: %s, %s, %s, %s for node: %s ; boarders: (%s, %s, %s, %s)",
                // p.x(), yMin,
                // xMax, yMax, n, xMin, xMax, yMin, yMax));
                RectHV rect = new RectHV(p.x(), yMin, xMax, yMax);
                if (smallestLeft != null && rect.distanceTo(targetP) > smallestLeft.distanceTo(targetP)) {
                    // no op
                } 
                else {
                    smallestRight = nearest(n.right, targetP, p.x(), xMax, yMin, yMax);
                }
            } 
            else {
                smallestRight = nearest(n.right, targetP, p.x(), xMax, yMin, yMax);
                RectHV rect = new RectHV(xMin, yMin, p.x(), yMax);
                if (smallestRight != null && rect.distanceTo(targetP) > smallestRight.distanceTo(targetP)) {
                    // no op
                } 
                else {
                    smallestLeft = nearest(n.left, targetP, xMin, p.x(), yMin, yMax);
                }
                
            }
        } 
        else {
            // y compare
            if (targetpY < p.y()) {
                smallestLeft = nearest(n.left, targetP, xMin, xMax, yMin, p.y());
                RectHV rect = new RectHV(xMin, p.y(), xMax, yMax);
                if (smallestLeft != null && rect.distanceTo(targetP) > smallestLeft.distanceTo(targetP)) {
                    // no op
                } 
                else {
                    smallestRight = nearest(n.right, targetP, xMin, xMax, p.y(), yMax);
                }
            } 
            else {
                smallestRight = nearest(n.right, targetP, xMin, xMax, p.y(), yMax);
                RectHV rect = new RectHV(xMin, yMin, xMax, p.y());
                if (smallestRight != null && rect.distanceTo(targetP) > smallestRight.distanceTo(targetP)) {
                    // no op
                } else {
                    smallestLeft = nearest(n.left, targetP, xMin, xMax, yMin, p.y());
                }
            }
        }
        Point2D minToTarget = identifyMinToTarget(targetP, n.point, smallestLeft, smallestRight);
        if (minToTarget == null) {
            throw new RuntimeException("Logic failure in minToTarget");
        }
        return minToTarget;
    }

    private Point2D identifyMinToTarget(Point2D target, Point2D point1, Point2D point2, Point2D point3) {
        double p1d = Double.MAX_VALUE;
        double p2d = Double.MAX_VALUE;
        double p3d = Double.MAX_VALUE;
        if (point1 != null) {
            p1d = target.distanceTo(point1);
        }
        if (point2 != null) {
            p2d = target.distanceTo(point2);
        }
        if (point3 != null) {
            p3d = target.distanceTo(point3);
        }

        if (p1d <= p2d && p1d <= p3d) {
            return point1;
        }
        if (p2d <= p1d && p2d <= p3d) {
            return point2;
        }
        if (p3d <= p1d && p3d <= p2d) {
            return point3;
        }
        throw new RuntimeException("non reacheable code");
    }

    private class Node {
        private static final String TO_STRING_NULL = "     ";
        private static final String TO_STRING_NON_NULL = " *** ";

        private Point2D point;
        private Node left;
        private Node right;
        private boolean red; // level 0, 2, 4

        public Node() {
        }

        public Node(Point2D p, boolean red) {
            point = p;
            this.red = red;
            left = null;
            right = null;
        }

        @Override
        public String toString() {
            final String leftStr;
            final String rightStr;
            if (left == null) {
                leftStr = TO_STRING_NULL;
            } 
            else {
                leftStr = TO_STRING_NON_NULL;
            }
            if (right == null) {
                rightStr = TO_STRING_NULL;
            } 
            else {
                rightStr = TO_STRING_NON_NULL;
            }
            final String redBlackMark;
            if (red) {
                redBlackMark = "'";
            }
            else {
                redBlackMark = "";
            }
            return "[" + leftStr + "  " + point.toString() + redBlackMark + "   " + rightStr + "]";
        }
    }
    
    private class EmptyNode extends Node {
        public EmptyNode() {
        }

        @Override
        public String toString() {
                 //[       (5.0, 0.0)        ]
            return "           ";
        }
    }

    private String getTreeAsString() {
        if (this.rootNode == null) {
            return "";
        }
        StringBuilder strBld = new StringBuilder();
        Queue<Node> currentQ = new LinkedList<Node>();
        Queue<Node> futureQ = new LinkedList<Node>();

        int level = 0;
        int futureQeffectiveN = -1;
        currentQ.add(this.rootNode);

        while (true) {
            if (currentQ.size() == 0 || futureQeffectiveN == 0) {
                break;
            }
            strBld.append("| ").append(level).append(" |   ");
            futureQeffectiveN = 0;
            while (currentQ.size() > 0) {
                Node n = currentQ.poll();
                strBld.append(n.toString()).append("    ");
                if (n.left != null) {
                    futureQ.add(n.left);
                    futureQeffectiveN++;
                }
                else {
                    futureQ.add(new EmptyNode());
                }
                if (n.right != null) {
                    futureQ.add(n.right);
                    futureQeffectiveN++;
                }
                else {
                    futureQ.add(new EmptyNode());
                }
            }
            currentQ.addAll(futureQ);
            futureQ.clear();
            level++;
            strBld.append("\n");
        }
        return strBld.toString();
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        runTests01();
        runTest02Nearest();
        runTest02NearestRandFixed();
        runTest08NearestRandom();
        System.out.println("Tests completed for KdTree");
    }

    private static void runTest08NearestRandom() {
        final int gridLen = 100;
        final int nOfPoints = 20;
        int numberOfTrials = 100000;

        ArrayList<Point2D> points = new ArrayList<Point2D>(nOfPoints);
        for (int i = 0; i < nOfPoints; i++) {
            points.add(getRandomGridPoint2D(gridLen));
        }

        KdTree kdTree = new KdTree();
        PointSET pSet = new PointSET();
        for (Point2D p : points) {
            kdTree.insert(p);
            pSet.insert(p);
        }

        if (kdTree.size() != pSet.size()) {
            throw new RuntimeException("Validation failed");
        }
        
       
        for (int i = 0; i < numberOfTrials; i++) {
            Point2D randPoint = getRandomGridPoint2D(gridLen);
            Point2D nearestTree = kdTree.nearest(randPoint);
            Point2D nearestSet = pSet.nearest(randPoint);
            if (!nearestSet.equals(nearestTree)) {
                double distanceTree = randPoint.distanceTo(nearestTree);
                double distanceSet = randPoint.distanceTo(nearestSet);
                if (Double.compare(distanceSet, distanceTree) == 0) {
                    // System.err.println("Found multiple points with similar distance");
                    continue;
                }
                final String errorStr = "Validation failed for point: " + randPoint + " on trial " + i + ". Tree: "
                        + nearestTree + "; Set: " + nearestSet + " . Tree distance = " + distanceTree
                        + "; Set distance = " + distanceSet;
                System.err.println(errorStr);
                System.out.println(getInsertCodeFromPoints(points));
                throw new RuntimeException(errorStr);
            }
        }
    }

    private static String getInsertCodeFromPoints(ArrayList<Point2D> points) {
        StringBuilder strBld = new StringBuilder();
        for (Point2D p : points) {
            strBld.append(String.format("kdTree05.insert(new Point2D(%s, %s));", p.x(), p.y()));
            strBld.append("\n");
        }
        return strBld.toString();
    }

    private static Point2D getRandomGridPoint2D(int gridLen) {
        double x = (double) StdRandom.uniform(0, gridLen) / gridLen;
        double y = (double) StdRandom.uniform(0, gridLen) / gridLen;
        return new Point2D(x, y);
    }

    private static void runTest02NearestRandFixed() {
        KdTree kdTree05 = new KdTree();
        kdTree05.insert(new Point2D(0.75, 0.75));
        kdTree05.insert(new Point2D(0.53, 0.88));
        kdTree05.insert(new Point2D(0.4, 0.63));
        kdTree05.insert(new Point2D(0.65, 0.08));
        kdTree05.insert(new Point2D(0.32, 0.33));
        kdTree05.insert(new Point2D(0.23, 0.26));
        kdTree05.insert(new Point2D(0.91, 0.84));
        kdTree05.insert(new Point2D(0.34, 0.19));
        kdTree05.insert(new Point2D(0.61, 0.29));
        kdTree05.insert(new Point2D(0.35, 0.16));
        kdTree05.insert(new Point2D(0.48, 0.77));
        kdTree05.insert(new Point2D(0.29, 0.16));
        kdTree05.insert(new Point2D(0.91, 0.11));
        kdTree05.insert(new Point2D(0.42, 0.9));
        kdTree05.insert(new Point2D(0.21, 0.33));
        kdTree05.insert(new Point2D(0.34, 0.19));
        kdTree05.insert(new Point2D(0.1, 0.58));
        kdTree05.insert(new Point2D(0.32, 0.48));
        kdTree05.insert(new Point2D(0.43, 0.57));
        kdTree05.insert(new Point2D(0.28, 0.89));
        

        // Validation failed for point: (0.28, 0.78) on trial 13. Tree: (0.4,
        // 0.63); Set: (0.28, 0.89) .
        // Tree distance = 0.19209372712298547; Set distance =
        // 0.10999999999999999

        final Point2D p028078 = new Point2D(0.28, 0.78);
        final Point2D nearestTo028078 = kdTree05.nearest(p028078);
        final Point2D expectedTo028078 = new Point2D(0.28, 0.89);
        // kdTree05.contains(nearestTo028078, true);
        // System.out.println("-----");
        // kdTree05.contains(expectedTo028078, true);

        boolean doDraw = false;
        if (doDraw) {
            kdTree05.draw();
            StdDraw.setPenRadius(DRAW_POINT_SIZE * 2);
            StdDraw.setPenColor(Color.YELLOW);
            nearestTo028078.draw();
            StdDraw.setPenColor(Color.GREEN);
            expectedTo028078.draw();
            StdDraw.setPenColor(Color.ORANGE);
            p028078.draw();
        }

        if (!nearestTo028078.equals(expectedTo028078)) {
            throw new RuntimeException("Validation failed: returned (yellow) " + nearestTo028078 + " expected (green) "
                    + expectedTo028078);
        }
        
    }

    private static void runTest02Nearest() {
        KdTree kdTree02 = new KdTree();
        kdTree02.insert(new Point2D(1, 1));
        kdTree02.insert(new Point2D(2, 2));
        kdTree02.insert(new Point2D(6, 6));
        kdTree02.insert(new Point2D(3, 3));
        kdTree02.insert(new Point2D(7, 7));
        kdTree02.insert(new Point2D(4, 4));
        kdTree02.insert(new Point2D(5, 5));

        final Point2D nearestTo00 = kdTree02.nearest(new Point2D(0, 0));
        final Point2D expectedTo00 = new Point2D(1, 1);
        if (!nearestTo00.equals(expectedTo00)) {
            throw new RuntimeException("Validation failed: returned " + nearestTo00 + " expected " + expectedTo00);
        }

        final Point2D nearestTo88 = kdTree02.nearest(new Point2D(8, 8));
        final Point2D expectedTo88 = new Point2D(7, 7);
        if (!nearestTo88.equals(expectedTo88)) {
            throw new RuntimeException("Validation failed: returned " + nearestTo88 + " expected " + expectedTo88);
        }

        final Point2D nearestTo2222 = kdTree02.nearest(new Point2D(2.2, 2.2));
        final Point2D expectedTo2222 = new Point2D(2, 2);
        if (!nearestTo2222.equals(expectedTo2222)) {
            throw new RuntimeException("Validation failed: returned " + nearestTo2222 + " expected " + expectedTo2222);
        }

        final Point2D nearestTo5601 = kdTree02.nearest(new Point2D(5, 6.01));
        final Point2D expectedTo5601 = new Point2D(6, 6);
        if (!nearestTo5601.equals(expectedTo5601)) {
            throw new RuntimeException("Validation failed: returned " + nearestTo5601 + " expected " + expectedTo5601);
        }

        // ========================== Circle
        KdTree kdTree03 = new KdTree();
        kdTree03.insert(new Point2D(-3, 0));
        kdTree03.insert(new Point2D(0, 3));
        kdTree03.insert(new Point2D(0, -3));
        kdTree03.insert(new Point2D(3, 0));

        final Point2D nearestTo00001 = kdTree03.nearest(new Point2D(0.0, 0.001));
        final Point2D expectedTo00001 = new Point2D(0, 3);
        if (!nearestTo00001.equals(expectedTo00001)) {
            throw new RuntimeException("Validation failed: returned " + nearestTo00001 + " expected " + expectedTo00001);
        }

        final Point2D nearestTo00010 = kdTree03.nearest(new Point2D(0.0001, 0));
        final Point2D expectedTo00010 = new Point2D(3, 0);
        if (!nearestTo00010.equals(expectedTo00010)) {
            throw new RuntimeException("Validation failed: returned " + nearestTo00010 + " expected " + expectedTo00010);
        }

    }

    private static void runTests01() {
        KdTree kdTree01 = new KdTree();
        kdTree01.insert(new Point2D(0, 0));
        kdTree01.insert(new Point2D(0, 5));
        kdTree01.insert(new Point2D(5, 0));
        kdTree01.insert(new Point2D(5, 5));
        kdTree01.insert(new Point2D(2.5, 2.5));
        kdTree01.insert(new Point2D(5, 5));
        if (kdTree01.size() != 5) {
            throw new RuntimeException("Validation failed");
        }
        System.out.println("Tree");
        System.out.println(kdTree01.getTreeAsString());
        if (!kdTree01.contains(new Point2D(5, 5))) {
            throw new RuntimeException("Validation failed");
        }
        if (kdTree01.contains(new Point2D(10, 10))) {
            throw new RuntimeException("Validation failed");
        }

        if (!kdTree01.contains(new Point2D(2.5, 2.5))) {
            throw new RuntimeException("Validation failed");
        }
        if (kdTree01.contains(new Point2D(3, 3))) {
            throw new RuntimeException("Validation failed");
        }

        // kdTree01.draw();

        ArrayList<Point2D> points01a = new ArrayList<Point2D>();
        for (Point2D p : kdTree01.range(new RectHV(-1, -1, 10, 10))) {
            points01a.add(p);
        }
        if (points01a.size() != 5) {
            throw new RuntimeException("Validation failed");
        }

        ArrayList<Point2D> points01b = new ArrayList<Point2D>();
        for (Point2D p : kdTree01.range(new RectHV(-1, -1, 2, 2))) {
            points01b.add(p);
        }
        if (points01b.size() != 1) {
            throw new RuntimeException("Validation failed");
        }

        ArrayList<Point2D> points01c = new ArrayList<Point2D>();
        for (Point2D p : kdTree01.range(new RectHV(2, 2, 7, 3))) {
            points01c.add(p);
        }
        if (points01c.size() != 1) {
            throw new RuntimeException("Validation failed");
        }

        if (!(new Point2D(25, 25).equals(new Point2D(25, 25)))) {
            throw new RuntimeException("Validation failed for Point");
        }

        final Point2D nearestTo11 = kdTree01.nearest(new Point2D(1, 1));
        final Point2D expectedTo11 = new Point2D(0, 0);
        if (!nearestTo11.equals(expectedTo11)) {
            throw new RuntimeException("Validation failed: returned " + nearestTo11 + " expected " + expectedTo11);
        }

        final Point2D nearestTo95 = kdTree01.nearest(new Point2D(9, 0));
        final Point2D expectedTo95 = new Point2D(5, 0);
        if (!nearestTo95.equals(expectedTo95)) {
            throw new RuntimeException("Validation failed: returned " + nearestTo95 + " expected " + expectedTo95);
        }

        final Point2D nearestToM13 = kdTree01.nearest(new Point2D(-1, 3));
        final Point2D expectedToM13 = new Point2D(0, 5);
        if (!nearestToM13.equals(expectedToM13)) {
            throw new RuntimeException("Validation failed: returned " + nearestToM13 + " expected " + expectedToM13);
        }

        // Test for min
        Point2D target1 = kdTree01.identifyMinToTarget(new Point2D(0, 0), new Point2D(0, 1), new Point2D(1, 0),
                new Point2D(4, 4));
        if (target1.equals(new Point2D(4, 4))) {
            throw new RuntimeException("Validation failed");
        }
    }
}
