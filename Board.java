public class Board
{
    private final int N;
    private int[][] board;
    
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
            }
        }
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
        int distances = 0;
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                if ((i == N - 1) && (j == N - 1))
                {
                    if (board[i][j] != 0)
                    {
                        int outOfPlace = board[i][j];
                        int properI = Math.abs((outOfPlace - 1) / N);
                        int properJ = Math.abs((outOfPlace - 1) % N);
                        distances += Math.abs(properI - i) + Math.abs(properJ - j);
                    }
                }
                else if ((board[i][j] != 0) && (board[i][j] != (i * N + j + 1)))
                {
                    int outOfPlace = board[i][j];
                    int properI = Math.abs((outOfPlace - 1) / N);
                    int properJ = Math.abs((outOfPlace - 1) % N);
                    distances += Math.abs(properI - i) + Math.abs(properJ - j);
                }
            }
        }
        return distances;
    }
    
    // is this board the goal board?
    public boolean isGoal()
    {
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                if ((i == N - 1) && (j == N - 1))
                {
                    if (board[i][j] != 0)
                    {
                        return false;
                    }
                }
                else if (board[i][j] != (i * N + j + 1))
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    // a board obtained by exchanging two adjacent blocks in the same row
    public Board twin()
    {
        Board twin = new Board(board);
        int[] zero = getCoordsOfZero();
        int avoidRow = zero[0];
        if (avoidRow - 1 >= 0)
        {
            twin.board[avoidRow - 1][0] = board[avoidRow - 1][1];
            twin.board[avoidRow - 1][1] = board[avoidRow - 1][0];
        }
        else
        {
            twin.board[avoidRow + 1][0] = board[avoidRow + 1][1];
            twin.board[avoidRow + 1][1] = board[avoidRow + 1][0];
        }
        return twin;
    }
    
    // does this board equal y?
    public boolean equals(Object y)
    {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board cmp = (Board) y;
        if (cmp.dimension() != this.dimension()) return false;
        int cmpN = cmp.dimension();
        for (int i = 0; i < cmpN; i++)
        {
            for (int j = 0; j < cmpN; j++)
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
        int[] zero = getCoordsOfZero();
        int row = zero[0];
        int col = zero[1];
        if (row == 0)
        {
            neighbors.push(swapBelow(row, col));
            if (col == 0)
            {
                // top left corner: 2 neighbors: below and right
                neighbors.push(swapRight(row, col));
            }
            else if (col == N-1)
            {
                // top right corner: 2 neighbors: below and left
                neighbors.push(swapLeft(row, col));
            }
            else
            {
                // top row in between corners: 3 neighbors: below, left, and right
               neighbors.push(swapLeft(row, col));
               neighbors.push(swapRight(row, col));
            }
        }
        else if (row == N-1)
        {
            neighbors.push(swapAbove(row, col));
            if (col == 0)
            {
                // bottom left corner: 2 neighbors: above and right
                neighbors.push(swapRight(row, col));
            }
            else if (col == N-1)
            {
                // bottom right corner: 2 neighbors: above and left
                neighbors.push(swapLeft(row, col));
            }
            else
            {
                // bottom row in between corners: 3 neighbors: above, left, and right
                neighbors.push(swapLeft(row, col));
                neighbors.push(swapRight(row, col));
            }
        }
        else
        {
            neighbors.push(swapAbove(row, col));
            neighbors.push(swapBelow(row, col));
            if (col == 0)
            {
                // left side in between corners: 3 neighbors: above, below, and right
                neighbors.push(swapRight(row, col));
            }
            else if (col == N -1)
            {
                
                // right side in between corners: 3 neighbors: above, below, and left
                neighbors.push(swapLeft(row, col));
            }
            else
            {
                // somewhere in the middle: 4 neighbors: above, below, left, and right
                neighbors.push(swapLeft(row, col));
                neighbors.push(swapRight(row, col));
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
    
    // find coordinates of zero tile
    private int[] getCoordsOfZero()
    {
        int[] coords = new int[2];
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                if (board[i][j] == 0)
                {
                    coords[0] = i;
                    coords[1] = j;
                }
            }
        }
        return coords;
    }
    
    private Board swapAbove(int row, int col)
    {
        Board nb = new Board(board);
        nb.board[row][col] = board[row-1][col];
        nb.board[row-1][col] = board[row][col];
        return nb;
    }
    
    private Board swapBelow(int row, int col)
    {
        Board nb = new Board(board);
        nb.board[row][col] = board[row+1][col];
        nb.board[row+1][col] = board[row][col];
        return nb;
    }
    
    private Board swapLeft(int row, int col)
    {
        Board nb = new Board(board);
        nb.board[row][col] = board[row][col-1];
        nb.board[row][col-1] = board[row][col];
        return nb;
    }
    
    private Board swapRight(int row, int col)
    {
        Board nb = new Board(board);
        nb.board[row][col] = board[row][col+1];
        nb.board[row][col+1] = board[row][col];
        return nb;
    }
}