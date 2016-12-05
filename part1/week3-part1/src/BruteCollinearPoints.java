import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private LineSegment[] lineSegmentsArr;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new NullPointerException("points is null");
        }
        checkForDuplicates(points);

        final ArrayList<PointHolder> foundSegments = new ArrayList<PointHolder>();
        for (int a = 0; a < points.length; a++) {
            for (int b = a + 1; b < points.length; b++) {
                for (int c = b + 1; c < points.length; c++) {
                    for (int d = c + 1; d < points.length; d++) {
                        double slope1 = points[a].slopeTo(points[b]);
                        double slope2 = points[a].slopeTo(points[c]);
                        double slope3 = points[a].slopeTo(points[d]);
                        if (slope1 == Double.NEGATIVE_INFINITY || slope2 == Double.NEGATIVE_INFINITY
                                || slope3 == Double.NEGATIVE_INFINITY) {
                            throw new IllegalArgumentException("Duplicates are not allowed");
                        }
                        if (Double.compare(slope1, slope2) == 0 && Double.compare(slope1, slope3) == 0) {
                            // Found
                            Point[] foundCollinear = new Point[4];
                            foundCollinear[0] = points[a];
                            foundCollinear[1] = points[b];
                            foundCollinear[2] = points[c];
                            foundCollinear[3] = points[d];
                            Arrays.sort(foundCollinear);
                            addSegmentIfnotExists(foundCollinear[0], foundCollinear[3], foundSegments);
                        }
                    }
                }
            }
        }
        // All done. Generate arr:
        lineSegmentsArr = pointHolderToLineSegment(foundSegments);
    }

    private void checkForDuplicates(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    private void addSegmentIfnotExists(Point minP, Point maxP, ArrayList<PointHolder> foundSegments) {
        PointHolder newPh = new PointHolder(minP, maxP);
        for (PointHolder ph : foundSegments) {
            if (ph.equals(newPh)) {
                // Duplicate found
                return;
            }
        }
        foundSegments.add(newPh);
        // System.out.println("Added: " + newPh);
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegmentsArr.length;
    }

    // the line segments
    private LineSegment[] pointHolderToLineSegment(ArrayList<PointHolder> foundSegments) {
        final LineSegment[] result = new LineSegment[foundSegments.size()];
        int i = 0;
        for (PointHolder ph : foundSegments) {
            result[i++] = ph.toLineSegment();
        }
        return result;
    }

    // the line segments
    public LineSegment[] segments() {
        return lineSegmentsArr.clone();
    }

    // We need this, since LineSegment does not allow to get values
    private class PointHolder {

        private Point minP;
        private Point maxP;

        public PointHolder(Point a, Point b) {
            minP = a;
            maxP = b;
        }

        public LineSegment toLineSegment() {
            return new LineSegment(minP, maxP);
        }

        private BruteCollinearPoints getOuterType() {
            return BruteCollinearPoints.this;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + maxP.hashCode();
            result = prime * result + minP.hashCode();
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            PointHolder other = (PointHolder) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (maxP == null) {
                if (other.maxP != null)
                    return false;
            } 
            else if (!maxP.equals(other.maxP))
                return false;
            if (minP == null) {
                if (other.minP != null)
                    return false;
            } 
            else if (!minP.equals(other.minP))
                return false;
            return true;
        }
    }
}
