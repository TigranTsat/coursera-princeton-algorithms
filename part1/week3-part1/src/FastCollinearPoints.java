import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private static final int COLLINEAR_POINT_MIN = 4;
    private final Point[] sortedSlopes;
    private LineSegment[] lineSegmentsArr;

    // finds all line segments containing 4 points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new NullPointerException("points is null");
        }
        final ArrayList<PointHolder> foundSegments = new ArrayList<PointHolder>();
        sortedSlopes = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new NullPointerException("We cannot have nulls in array");
            }
            sortedSlopes[i] = points[i];
        }

        for (int i = 0; i < points.length; i++) {
            Point baseP = points[i];
            // System.out.println("Selected point: " + baseP);
            Arrays.sort(sortedSlopes, baseP.slopeOrder());
            // System.out.println("Sorted B: " + Arrays.toString(sortedSlopes));
            int left = 0;
            int right = 0;
            while (true) {
                left++;
                if (left >= sortedSlopes.length) {
                    break;
                }
                Point minP = baseP;
                Point maxP = baseP;
                double slope = baseP.slopeTo(sortedSlopes[left]);
                for (right = left; right < sortedSlopes.length; right++) {
                    if (sortedSlopes[left] == baseP) {
                        continue;
                    }
                    double slope2 = baseP.slopeTo(sortedSlopes[right]);
                    if (slope2 == Double.NEGATIVE_INFINITY) {
                        throw new IllegalArgumentException("Duplicates are not allowed");
                    }
                    if (Double.compare(slope, slope2) != 0) {
                        break;
                    }
                    if (minP.compareTo(sortedSlopes[right]) > 0) {
                        minP = sortedSlopes[right];
                    }
                    if (maxP.compareTo(sortedSlopes[right]) < 0) {
                        maxP = sortedSlopes[right];
                    }
                }
                right--;
                // we have a gap of index left and right - 1
                // System.out.println(String.format("Found gap: %s - %s", left,
                // right));
                int collinearLen = right - left + 1 + 1;
                if (collinearLen >= COLLINEAR_POINT_MIN) {
                    // System.out.println(String.format("Found collinear (%s, %s). Len = %s. %s - %s [%s]",
                    // left, right,
                    // collinearLen, minP, maxP,
                    // Arrays.toString(collenearPoints)));
                    addSegment(minP, maxP, foundSegments);

                }
                left = right;
            }
        }
        // All done. Generate arr:
        lineSegmentsArr = pointHolderToLineSegment(foundSegments);
    }

    private void addSegment(Point minP, Point maxP, ArrayList<PointHolder> foundSegments) {
        PointHolder newPh = new PointHolder(minP, maxP);
        if (minP.compareTo(maxP) >= 0) {
            throw new RuntimeException("Wrong");
        }
        foundSegments.add(newPh);
    }

    // private void addSegmentIfnotExists(Point minP, Point maxP,
    // ArrayList<PointHolder> foundSegments) {
    // PointHolder newPh = new PointHolder(minP, maxP);
    // for (PointHolder ph : foundSegments) {
    // if (ph.equals(newPh)) {
    // // Duplicate found
    // return;
    // }
    // }
    // foundSegments.add(newPh);
    // }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegmentsArr.length;
    }

    private boolean doesElementExist(PointHolder[] phArr, int i, PointHolder p) {
        for (int j = 0; j < i; j++) {
            if (phArr[j].equals(p)) {
                return true;
            }
        }
        return false;
    }

    // the line segments
    private LineSegment[] pointHolderToLineSegment(ArrayList<PointHolder> foundSegments) {
        final PointHolder[] phResult = new PointHolder[foundSegments.size()];
        int i = 0;
        for (PointHolder ph : foundSegments) {
            // check that not exist
            boolean alreadyExist = doesElementExist(phResult, i, ph);
            if (!alreadyExist) {
                phResult[i] = ph;
                i++;
            } 
        }
        final LineSegment[] result = new LineSegment[i];
        for (int j = 0; j < i; j++) {
            result[j] = phResult[j].toLineSegment();
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

        private FastCollinearPoints getOuterType() {
            return FastCollinearPoints.this;
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

        @Override
        public String toString() {
            return "PointHolder [minP=" + minP + ", maxP=" + maxP + "]";
        }

    }
}
