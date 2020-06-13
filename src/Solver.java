import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private Node start;  // начальный
    private Node last;  // победный шаг
    private Node badTwin; // если решается он, то оригинал не решится
    private int numOfMove;
    private boolean solvable;
    private MinPQ<Node> openQueue;  // used for the original Board
    private MinPQ<Node> twinQueue; // used for the twin Board
    private Stack<Board> step; // сюда записывать все шаги от победного к началу

    //задаем Ноде
    private class Node implements Comparable<Node> {
        int step = 0;
        Board board;
        Node previous;
        int fScore;

        //  нужно уточнить , что и как входит в Ноде, раньше ты это делал в функции  Add, push, enqueue
        public Node(Board b, int stepMade, Node prevNode) {
            step = stepMade;
            board = b;
            previous = prevNode;
            fScore = step + b.manhattan();
        }

        //сравнение
        public int compareTo(Node other) {
            return Integer.compare(fScore, other.fScore);
        }
    }





    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if(initial == null)
        { throw new NullPointerException("The initial board is NULL!"); }

        openQueue = new MinPQ<Node>();
        twinQueue = new MinPQ<Node>();
        solvable = false; //изначально так
        numOfMove = -1; // -1 т.к. 4 борда являются 3-мя шагами, т.к. первый не в счет
        start = new Node(initial, 0, null); // задать стартовый Борд
        badTwin = new Node(start.board.twin(), 0 , null); // плохой близнец для стартового Борда
        openQueue.insert(start); // очередь для оригинала
        twinQueue.insert(badTwin); // очередь для близнецов

        Node current;
        Node newNeighbor;
        Node alternateCurrent; // для twin

        while (!openQueue.isEmpty() || !twinQueue.isEmpty()) { // пока есть что доставать
            current = openQueue.delMin(); // достаем с минимальным манхеттеном
            alternateCurrent = twinQueue.delMin(); // достаем с минимальным манхеттеном

            if (current.board.isGoal()) {
                solvable = true;
                last = current;
                break;
            }

            if (alternateCurrent.board.isGoal())  // если решается он(близнец), то оригинал не решится
                break;                            // solvable осталась false

            // Для досок b принадлежащих стеку "соседи"
            for (Board b : current.board.neighbors()) {

                // ни шагу назад. Если сосед плиты"А" равен плите, что была до А. то пропускать тело цикла
                if (current.previous != null && b.equals(current.previous.board)) continue;

                newNeighbor = new Node(b, current.step + 1, current);
                openQueue.insert(newNeighbor); //вставляем соседей
            }

            for (Board b : alternateCurrent.board.neighbors()) {

                // ни шагу назад. Если сосед плиты"А" равен плите, что была до А. то пропускать тело цикла
                if (alternateCurrent.previous != null && b.equals(alternateCurrent.previous.board)) continue;

                newNeighbor = new Node(b, alternateCurrent.step + 1, alternateCurrent);
                twinQueue.insert(newNeighbor); //вставляем соседей
            }
        }

        //  когда нашелся победный (last)
        if (solvable) {
            step = new Stack<Board>();  // шаги в обратном порядке
            Node cur = last;
            while (cur != null) {
                numOfMove++;
                step.push(cur.board);
                cur = cur.previous;
            }
        }



    }



    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return numOfMove; // изначально -1
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        return step;
    }




    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

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

}