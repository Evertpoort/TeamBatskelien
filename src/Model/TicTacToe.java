package Model;

import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

public class TicTacToe extends Game {
    public TicTacToe(LinkedBlockingQueue<String> outputQueue, Properties prop, Cell cellType){
        super(outputQueue, prop, 3, cellType, cellType == Cell.KRUISJE ? Cell.RONDJE : Cell.KRUISJE);
    }

    @Override
    public boolean move(int index) {
        if (!playerTurn) {
            System.out.println("It's not your turn!");
            return false;
        }
        if (board[index] != Cell.EMPTY) {
            System.out.println("Cell not empty!");
            return false;
        }
        playerTurn = false;
        super.move(index);
        return true;
    }
}
