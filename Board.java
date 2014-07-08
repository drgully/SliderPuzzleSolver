public class Board
{
    private final int N;
    private int[][] board;
    private int blankRow, blankCol, distances;
    
    // construct a board from an N-by-N array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks)
    {
        N = blocks[0].length;
        board = new int[N][N];
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                board[i][j] = blocks[i][j];
                if (board[i][j] == 0) // record location of blank tile
                {
                    blankRow = i;
                    blankCol = j;
                }
            }
        }
        distances = computeManhattan();
    }
    
    // board dimension N
    public int dimension()
    {
        return N;
    }
    
    // number of blocks out of place
    public int hamming()
    {
        int count = 0;
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                if ((i == N - 1) && (j == N - 1))
                {
                    if ((board[i][j] != 0) && (board[i][j] != 0))
                    {
                        count++;
                    }
                }
                else if ((board[i][j] != 0) && (board[i][j] != (i * N + j + 1)))
                {
                    count++;
                }
            }
        }
        return count;
    }
    
    // sum of Manhattan distances between blocks and goal
    public int manhattan()
    {
        return distances;
    }
    
    private int computeManhattan()
    {
        int retval = 0;
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                if (board[i][j] != 0)
                {
                    if ((i == N - 1) && (j == N - 1))
                    {
                        int outOfPlace = board[i][j];
                        int properI = Math.abs((outOfPlace - 1) / N);
                        int properJ = Math.abs((outOfPlace - 1) % N);
                        retval += Math.abs(properI - i) + Math.abs(properJ - j);
                    }
                    else if ((board[i][j] != (i * N + j + 1)))
                    {
                        int outOfPlace = board[i][j];
                        int properI = Math.abs((outOfPlace - 1) / N);
                        int properJ = Math.abs((outOfPlace - 1) % N);
                        retval += Math.abs(properI - i) + Math.abs(properJ - j);
                    }
                }
            }
        }
        return retval;
    }
    
    // is this board the goal board?
    public boolean isGoal()
    {
        return distances == 0;
    }
    
    // a board obtained by exchanging two adjacent blocks in the same row
    public Board twin()
    {
        Board temp = new Board(board);
        if (blankRow == 0)
        {
            temp.board[1][0] = board[1][1];
            temp.board[1][1] = board[1][0];
        }
        else
        {
            temp.board[0][0] = board[0][1];
            temp.board[0][1] = board[0][0];
        }
        return new Board(temp.board);
    }
    
    // does this board equal y?
    public boolean equals(Object y)
    {
        if (y == this) return true; // same object
        if (y == null) return false; // null object
        if (y.getClass() != this.getClass()) return false; // different class
        Board cmp = (Board) y;
        if (cmp.N != this.N) return false; // different dimension
        if ((cmp.blankRow != this.blankRow) ||  (cmp.blankCol != this.blankCol))
            return false; // blank tile in different locations
        for (int i = 0; i < cmp.N; i++)
        {
            for (int j = 0; j < cmp.N; j++)
            {
                if (cmp.board[i][j] != this.board[i][j])
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    // all neighboring boards
    public Iterable<Board> neighbors()
    {
        Stack<Board> neighbors = new Stack<Board>();
        if (blankRow == 0)
        {
            neighbors.push(swapBelow(blankRow, blankCol));
            if (blankCol == 0)
            {
                // top left corner: 2 neighbors: below and right
                neighbors.push(swapRight(blankRow, blankCol));
            }
            else if (blankCol == N-1)
            {
                // top right corner: 2 neighbors: below and left
                neighbors.push(swapLeft(blankRow, blankCol));
            }
            else
            {
                // top row in between corners: 3 neighbors: below, left, and right
               neighbors.push(swapLeft(blankRow, blankCol));
               neighbors.push(swapRight(blankRow, blankCol));
            }
        }
        else if (blankRow == N-1)
        {
            neighbors.push(swapAbove(blankRow, blankCol));
            if (blankCol == 0)
            {
                // bottom left corner: 2 neighbors: above and right
                neighbors.push(swapRight(blankRow, blankCol));
            }
            else if (blankCol == N-1)
            {
                // bottom right corner: 2 neighbors: above and left
                neighbors.push(swapLeft(blankRow, blankCol));
            }
            else
            {
                // bottom row in between corners: 3 neighbors: above, left, and right
                neighbors.push(swapLeft(blankRow, blankCol));
                neighbors.push(swapRight(blankRow, blankCol));
            }
        }
        else
        {
            neighbors.push(swapAbove(blankRow, blankCol));
            neighbors.push(swapBelow(blankRow, blankCol));
            if (blankCol == 0)
            {
                // left side in between corners: 3 neighbors: above, below, and right
                neighbors.push(swapRight(blankRow, blankCol));
            }
            else if (blankCol == N -1)
            {
                
                // right side in between corners: 3 neighbors: above, below, and left
                neighbors.push(swapLeft(blankRow, blankCol));
            }
            else
            {
                // somewhere in the middle: 4 neighbors: above, below, left, and right
                neighbors.push(swapLeft(blankRow, blankCol));
                neighbors.push(swapRight(blankRow, blankCol));
            }
        }
        return neighbors;
    }
    
    // string representation of the board (in the output format specified below)
    public String toString()
    {
        String retval = "\n" + N;
        for (int i = 0; i < N; i++)
        {
            retval += "\n ";
            for (int j = 0; j < N; j++)
            {
                retval += board[i][j] + "  ";
            }
        }
        return retval;
    }
    
    private Board swapAbove(int row, int col)
    {
        Board temp = new Board(board);
        temp.board[row][col] = board[row-1][col];
        temp.board[row-1][col] = board[row][col];
        return new Board(temp.board);
    }
    
    private Board swapBelow(int row, int col)
    {
        Board temp = new Board(board);
        temp.board[row][col] = board[row+1][col];
        temp.board[row+1][col] = board[row][col];
        return new Board(temp.board);
    }
    
    private Board swapLeft(int row, int col)
    {
        Board temp = new Board(board);
        temp.board[row][col] = board[row][col-1];
        temp.board[row][col-1] = board[row][col];
        return new Board(temp.board);
    }
    
    private Board swapRight(int row, int col)
    {
        Board temp = new Board(board);
        temp.board[row][col] = board[row][col+1];
        temp.board[row][col+1] = board[row][col];
        return new Board(temp.board);
    }
}