package Model;

import java.util.concurrent.LinkedBlockingQueue;

public abstract class Game {
    LinkedBlockingQueue<String> outputQueue;
    Board board;
    boolean playerTurn = false;
    Cell cellTypePlayer;
    Cell cellTypeOpponent;
    int size;

    public Game(LinkedBlockingQueue<String> outPutQueue, int size, Cell cellTypePlayer, Cell cellTypeOpponent) {
        this.outputQueue = outPutQueue;
        this.size = size;
        this.board = new Board(size);
        this.cellTypePlayer = cellTypePlayer;
        this.cellTypeOpponent = cellTypeOpponent;
    }

    private int getIndex(int x, int y) {
        return y * size + x;
    }

    private int getX(int index) {
        return index % size;
    }

    private int getY(int index) {
        return index / size;
    }

    public void sendMoveToServer(int index) {
        outputQueue.add("move " + index);
    }

    public Cell[] getBoard() {
        return board.getCells();
    }

    public void setPlayerTurn() {
        playerTurn = true;
    }

    public void opponentMove(int index) {
        board.setCell(index, cellTypeOpponent);
    }

    public boolean move(int x, int y) {
        return move(getIndex(x, y));
    }

    public boolean move(int index) {
        return false;
    }

    public boolean AIMove() {
        return false;
    }
}
