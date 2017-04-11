package Model;

import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Math.pow;

public abstract class Game {
    LinkedBlockingQueue<String> outputQueue;
    Cell[] board;
    boolean playerTurn = false;
    Cell cellTypePlayer;
    Cell cellTypeOpponent;
    int size;
    int playerScore = 0;
    int opponentScore = 0;

    public Game(LinkedBlockingQueue<String> outPutQueue, int size, Cell cellTypePlayer, Cell cellTypeOpponent) {
        this.outputQueue = outPutQueue;
        this.size = size;
        this.board = new Cell[(int)(pow(size, 2))];
        Arrays.fill(this.board, Cell.EMPTY);
        this.cellTypePlayer = cellTypePlayer;
        this.cellTypeOpponent = cellTypeOpponent;
    }

    public int getIndex(int x, int y) {
        return y * size + x;
    }

    public int getX(int index) {
        return index % size;
    }

    public int getY(int index) {
        return index / size;
    }

    public void sendMoveToServer(int index) {
        outputQueue.offer("move " + index);
    }

    public Cell[] getBoard() {
        return board;
    }

    public void setPlayerTurn() {
        playerTurn = true;
    }

    public boolean getPlayerTurn() {
        return playerTurn;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public int getOpponentScore() {
        return opponentScore;
    }

    public void opponentMove(int index) {
        board[index] = cellTypeOpponent;
    }

    public boolean move(int x, int y) {
        return move(getIndex(x, y));
    }

    public boolean move(int index) {
        board[index] = cellTypePlayer;
        sendMoveToServer(index);
        return true;
    }

    public boolean AIMove() {
        return false;
    }
}
