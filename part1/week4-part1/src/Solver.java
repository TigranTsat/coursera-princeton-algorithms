import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private final boolean solvable;
    private final SearchNode solutionSN;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new NullPointerException("Board cannot be null");
        }
        int maxTries = 25;
        SearchNode solution = null;
        boolean solutionDirect = false, solutionTwin = false;
        while (true) {
            solution = findSolution(initial, maxTries);
            if (solution != null) {
                solutionDirect = true;
                break;
            }
            solution = findSolution(initial.twin(), maxTries);
            if (solution != null) {
                solutionTwin = true;
                break;
            }
            maxTries *= 2;
        }
        if (solutionDirect && solutionTwin) {
            throw new RuntimeException("Internal error");
        }
        if (solutionDirect) {
            solvable = true;
            solutionSN = solution;
        }
        else if (solutionTwin) {
            solvable = false;
            solutionSN = null;
        } 
        else {
            throw new RuntimeException("Internal error");
        }
        // System.out.println("Completed. Solvable: " + solvable);
    }

    private SearchNode findSolution(Board initial, int maxTries) {
        final MinPQ<SearchNode> boardQueue = new MinPQ<SearchNode>(new BoardManhattanComparator());
        SearchNode initialNode = new SearchNode(initial);
        boardQueue.insert(initialNode);

        SearchNode minSelectedSearchNode = null;
        int tries = 0;
        while (true) {
            tries++;
            minSelectedSearchNode = boardQueue.delMin();
            if (minSelectedSearchNode.b.isGoal()) {
                break;
            }
            if (tries > maxTries) {
                return null;
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
        return minSelectedSearchNode;
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (solvable) {
            return solutionSN.numberOfMoves;
        } 
        else {
            return -1;
        }
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (solvable) {
            return new SolutionIterable(solutionSN);
        } 
        else {
            return null;
        }
    }
    
    private class SolutionIterable implements Iterable<Board> {
        private final SearchNode solution;

        private SolutionIterable(SearchNode solution) {
            this.solution = solution;
        }
        @Override
        public Iterator<Board> iterator() {
            LinkedList<Board> stack = new LinkedList<Board>();
            SearchNode s = solution;
            while (s != null) {
                stack.add(s.b);
                s = s.prevSearchNode;
            }
            return stack.descendingIterator();
        }
        
    }
    

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
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
            else {
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
