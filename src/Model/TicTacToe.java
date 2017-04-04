package Model;

import java.util.concurrent.LinkedBlockingQueue;

public class TicTacToe extends Game {
    public TicTacToe(LinkedBlockingQueue<String> outputQueue, Cell cellType){
        super(outputQueue, new Board(3), cellType, cellType == Cell.KRUISJE ? Cell.RONDJE : Cell.KRUISJE, false);
    }

    @Override
    public boolean move(int x, int y) {
        if (!playerTurn) {
            System.out.println("It's not your turn!");
            return false;
        }
        playerTurn = true;
        if (board.getCell(x, y) != Cell.EMPTY) {
            System.out.println("Cell not empty!");
            return false;
        }
        board.setCell(x, y, cellTypePlayer);
        sendMoveToServer(x, y);
        return true;
    }
}
