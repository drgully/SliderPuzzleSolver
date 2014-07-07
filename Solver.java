import java.util.Comparator;

public class Solver
{
    private int N, moves;
    private boolean solvable;
    private BoardNode solved;
    
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial)
    {
        N = initial.dimension();
        moves = 0;
        solvable = false;
        solved = null;
        
        BoardNode current = new BoardNode(initial, -1, null);
        //BoardNode twin = new BoardNode(initial.twin(), -1, null);
        MinPQ<BoardNode> currentQueue = new MinPQ<BoardNode>(new BoardComparator());
        //MinPQ<BoardNode> twinQueue = new MinPQ<BoardNode>(new BoardComparator());
        
        int max = getMax();
        while (moves < max)
        {
            if (current.getBoard().isGoal())
            {
                solvable = true;
                solved = current;
                break;
            }
            /*
            if (twin.getBoard().isGoal())
            {
                break;
            }
            */
            for (Board board: current.getBoard().neighbors())
            {
                if (!board.equals(current.getBoard()))
                {
                    BoardNode neighbor = new BoardNode(board, current.getMoves(), current);
                    currentQueue.insert(neighbor);
                }
            }
            /*
            for (Board board: twin.getBoard().neighbors())
            {
                if (!board.equals(twin.getBoard()))
                {
                    BoardNode neighbor = new BoardNode(board, twin.getMoves(), twin);
                    twinQueue.insert(neighbor);
                }
            }
            */
            current = currentQueue.delMin();
            //twin = twinQueue.delMin();
            moves++;
        }
    }
    
    private int getMax()
    {
        if (N == 1) return 0;
        else if (N == 2) return 6;
        else if (N == 3) return 31;
        else if (N == 4) return 80;
        else if (N == 5) return 152;
        else             return N*N*N;
    }
    
    // is the initial board solvable?
    public boolean isSolvable()
    {
        return solvable;
    }
    
    // min number of moves to solve initial board; -1 if no solution
    public int moves()
    {
        if (solvable)
        {
            return solved.getMoves();
        }
        return -1;
    }
    
    // sequence of boards in a shortest solution; null if no solution
    public Iterable<Board> solution()
    {
        if (solvable)
        {
            Stack<Board> solution = new Stack<Board>();
            BoardNode bn = solved;
            while (bn.getPrev() != null)
            {
                solution.push(bn.getBoard());
                bn = bn.getPrev();
            }
            solution.push(bn.getBoard());
            return solution;
        }
        return null;
    }
    
    // inner class to create board nodes
    private class BoardNode
    {
        private Board board;
        private int moves;
        private BoardNode prev;
        
        public BoardNode(Board board, int moves, BoardNode prev)
        {
            this.board = board;
            this.moves = moves + 1;
            this.prev = prev;
        }
        
        public Board getBoard()
        {
            return board;
        }
        
        public int getMoves()
        {
            return moves;
        }
        
        public BoardNode getPrev()
        {
            return prev;
        }
    }
    
    // inner class to enable comparison of boards
    private class BoardComparator implements Comparator<BoardNode>
    {
        public int compare(BoardNode thisBN, BoardNode thatBN)
        {
            int thisBNTotal = thisBN.getMoves() + thisBN.getBoard().manhattan();
            int thatBNTotal = thatBN.getMoves() + thatBN.getBoard().manhattan();
            if (thisBNTotal < thatBNTotal)
            {
                return -1;
            }
            else if (thisBNTotal > thatBNTotal)
            {
                return 1;
            }
            return 0;
        }
    }
    
    // solve a slider puzzle (given below)
    public static void main(String[] args)
    {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                blocks[i][j] = in.readInt();
            }
        }
        Board initial = new Board(blocks);
        
        // solve the puzzle
        Solver solver = new Solver(initial);
        
        // print solution to standard output
        if (!solver.isSolvable())
        {
            StdOut.println("No solution possible");
        }
        else
        {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
            {
                StdOut.println(board);
            }
        }
    }
}