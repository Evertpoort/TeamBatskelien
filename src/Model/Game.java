package Model;

import java.util.concurrent.LinkedBlockingQueue;

public abstract class Game {
    LinkedBlockingQueue<String> outputQueue;
    Board board;
    boolean playerTurn = false;
    Cell cellTypePlayer;
    Cell cellTypeOpponent;
    boolean AI;

    public Game(LinkedBlockingQueue<String> outPutQueue, Board board, Cell cellTypePlayer, Cell cellTypeOpponent , boolean AI) {
        this.outputQueue = outPutQueue;
        this.board = board;
        this.cellTypePlayer = cellTypePlayer;
        this.cellTypeOpponent = cellTypeOpponent;
        this.AI = AI;
    }

    public void sendMoveToServer(int x, int y) {
        return;
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
        return false;
    }

    public boolean AIMove() {
        return false;
    }
}
