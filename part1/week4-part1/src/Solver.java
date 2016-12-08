import java.util.Comparator;

import edu.princeton.cs.algs4.MinPQ;

public class Solver {

    private final MinPQ<SearchNode> boardQueue = new MinPQ<SearchNode>(new BoardManhattanComparator());

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new NullPointerException("Board cannot be null");
        }
        SearchNode initialNode = new SearchNode(initial);
        boardQueue.insert(initialNode);

        while (true) {
            SearchNode minSelectedSearchNode = boardQueue.delMin();
            if (minSelectedSearchNode.b.isGoal()) {
                break;
            }

            SearchNode prevSearchNode = minSelectedSearchNode.prevSearchNode;
            if (prevSearchNode != null) {
                // Check whether is it same
                for (Board bNeighbor : minSelectedSearchNode.b.neighbors()) {
                    if (!bNeighbor.equals(prevSearchNode.b)) {
                        SearchNode neighborSN = new SearchNode(bNeighbor, minSelectedSearchNode);
                        boardQueue.insert(neighborSN);
                    }
                }
            } 
            else {
                for (Board bNeighbor : minSelectedSearchNode.b.neighbors()) {
                    SearchNode neighborSN = new SearchNode(bNeighbor, minSelectedSearchNode);
                    boardQueue.insert(neighborSN);
                }
            }
        }

        System.out.println("Found solution");
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        // TODO
        return false;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        // TODO
        return 0;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        // TODO
        return null;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // TODO
    }
    
    private class BoardManhattanComparator implements Comparator<SearchNode> {

        @Override
        public int compare(SearchNode o1, SearchNode o2) {
            if (o1 == null || o2 == null) {
                throw new NullPointerException("o1 or o2 is null");
            }
            if (o1 == o2) {
                return 0;
            }
            final int o1m = o1.b.manhattan() + o1.numberOfMoves;
            final int o2m = o2.b.manhattan() + o2.numberOfMoves;
            if (o1m < o2m) {
                return -1;
            }
            if (o1m > o2m) {
                return 1;
            } 
            else
            {
                return 0;
            }
        }
    }

    private class SearchNode {
        private Board b;
        private SearchNode prevSearchNode;
        private int numberOfMoves;

        public SearchNode(Board b) {
            this.b = b;
            this.prevSearchNode = null;
            this.numberOfMoves = 0;
        }

        public SearchNode(Board b, SearchNode prevSearchNode) {
            this.b = b;
            this.prevSearchNode = prevSearchNode;
            this.numberOfMoves = prevSearchNode.numberOfMoves + 1;
        }
    }
}
