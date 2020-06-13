import java.util.Arrays;
import java.util.Stack;

public class Board {

    private int[] board;
    private int size;
    private int sizeSquare;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        size = tiles.length;
        sizeSquare = size*size;
        board = new int[sizeSquare];
        for(int i = 0 ; i < size; i++)
            for (int j = 0; j < size; j++)
                board[i*size + j] = tiles[i][j];
    }

    //?????
    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(size + "\n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                s.append(String.format("%2d ", board[i*size + j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return size;
    }

    // number of tiles out of place
    public int hamming() {
        int score = 0;
        for (int i = 0; i < sizeSquare; i++) if (board[i] != i + 1 && board[i] != 0) score++;
        //для n=3 максимум 8 очков, !! а не 9!!, поэтому если боард[i]=0 - не в счет
        return score;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int score = 0;
        for (int i = 0 ; i < sizeSquare; i++)
            //если боард[i]=0 - не в счет
            if (board[i] != 0)
                score += Math.abs((board[i] - 1)/size - i/size) + Math.abs((board[i] - 1)%size - i%size);
            return score;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    //???
    // does this board equal y?
    public boolean equals(Object y) {
        Board that = (Board) y;
        return Arrays.equals(this.board, that.board);
    }

    // all neighboring boards
    // создаст стек с соседями Board
    public Iterable<Board> neighbors() {
        Stack<Board> sosedi = new Stack<Board>();
        int zeroIndex = findZero();

        //int[] copy = board.clone(); // нужно ли это????

        int i = zeroIndex/size;
        int j = zeroIndex % size;
        if (valid(i, j + 1)) sosedi.push(makeBoard(swap(board, zeroIndex, zeroIndex + 1)));
        if (valid(i, j - 1)) sosedi.push(makeBoard(swap(board, zeroIndex, zeroIndex - 1)));
        if (valid(i+1, j)) sosedi.push(makeBoard(swap(board, zeroIndex, zeroIndex + size)));
        if (valid(i-1, j)) sosedi.push(makeBoard(swap(board, zeroIndex, zeroIndex - size)));
        return sosedi;
    }



    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] blocks = new int[size][size];
        int index = 0;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                blocks[i][j] = board[i * size + j];
                if (board[i*size + j] ==  0) index = i*size + j;
            }
        int i = 0;
            //свап будет между 0-1 или 3-4  [3x3]
            while (i == index || i + 1 == index || (i + 1)/size - i/size > 0) i++;

            int swap = blocks[i/size][i%size]; //(i/size) = i; (i%size) = j
            blocks[i/size][i%size] = blocks[i/size][i%size + 1];
            blocks[i/size][i%size + 1] = swap;
        return new Board(blocks);
    }

    private int findZero() {
        for (int i = 0 ; i < sizeSquare; i++)
            if (board[i] == 0) return i;
            return -1;
    }

    private boolean valid(int x, int y) {
        return (x >= 0 && y >= 0 && x < size && y < size);
    }

    // вернет новый массив с exch индексами
    private int[] swap(int[] orig, int index1, int index2) {
        int[] newBoard = orig.clone();
        int t = newBoard[index1];
        newBoard[index1] = newBoard[index2];
        newBoard[index2] = t;
        return newBoard;
    }

    // создаст новый элемент Board из массива
    private Board makeBoard(int[] array){
        int[][] newBoard = new int[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                newBoard[i][j] = array[i*size + j];
            return new Board(newBoard);
    }

    // unit testing (not graded)
    public static void main(String[] args) {}

}