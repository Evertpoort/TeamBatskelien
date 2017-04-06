package Model;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class Othello extends Game {
    public Othello(LinkedBlockingQueue<String> outputQueue, boolean playerTurn, Cell cellType){
        super(outputQueue, 8, cellType, cellType == Cell.ZWART ? Cell.WIT : Cell.ZWART);
        if (playerTurn) {
            board.setCell(27, cellTypeOpponent);
            board.setCell(36, cellTypeOpponent);
            board.setCell(28, cellTypePlayer);
            board.setCell(35, cellTypePlayer);
        } else {
            board.setCell(27, cellTypePlayer);
            board.setCell(36, cellTypePlayer);
            board.setCell(28, cellTypeOpponent);
            board.setCell(35, cellTypeOpponent);
        }
    }

    @Override
    public boolean move(int index) {
        if (!playerTurn) {
            System.out.println("It's not your turn!");
            return false;
        }
        if (board.getCell(index) != Cell.EMPTY) {
            System.out.println("Cell not empty!");
            return false;
        }
//        if (!getValidIndexes().contains(index)) {
//            System.out.println("Not a valid move!");
//            return false;
//        }
        playerTurn = false;
        board.setCell(index, cellTypePlayer);
        sendMoveToServer(index);
        return true;
    }

    private ArrayList<Integer> getValidIndexes() {
        int i = 0;
        for (Cell cell : getBoard()) {

        }
//        board.getCell(getValidIndexes(cellLocX, i++));
        ArrayList<Integer> validIndexes = new ArrayList<>();
        return validIndexes;
    }

    @Override
    public boolean AIMove() {
        return false; // AI
    }
}
