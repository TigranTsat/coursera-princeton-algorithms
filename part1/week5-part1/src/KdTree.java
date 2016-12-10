import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

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
            System.out.println("New min = " + x + " was " + minX);
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

        int level = 0;
        Node currentNode = rootNode;
        if (rootNode == null) {
            return false;
        }
        while (true) {
            if (currentNode == null) {
                return false;
            }
            if (level % 2 == 0) {
                // compare by x
                if (currentNode.point.equals(p)) {
                    return true;
                }
                double x = currentNode.point.x();
                if (p.x() < x) {
                    currentNode = currentNode.left;
                } 
                else {
                    currentNode = currentNode.right;
                }
            } else {
                // compare by y
                if (currentNode.point.equals(p)) {
                    return true;
                }
                double y = currentNode.point.y();
                if (p.y() < y) {
                    currentNode = currentNode.left;
                } 
                else {
                    currentNode = currentNode.right;
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
        // TODO
        return null;
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
        System.out.println("Tests completed for KdTree");
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

        kdTree01.draw();

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

        if (!(new Point(25, 25).equals(new Point(25, 25)))) {
            throw new RuntimeException("Validation failed for Point");
        }

    }
}
