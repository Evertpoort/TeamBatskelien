package Model;

import java.util.ArrayList;
import java.util.Arrays;
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
        if (!getUpdateIndexes().contains(index)) {
            System.out.println("Not a valid move!");
//            return false;
        }
        playerTurn = false;
        board.setCell(index, cellTypePlayer);
        sendMoveToServer(index);
        return true;
    }

    // Returns valid indexes
    private ArrayList<Integer> getUpdateIndexes() {
        ArrayList<Integer> validIndexes = new ArrayList<>();
        return validIndexes;
    }

    private Cell[][] getAllRows() {
        Cell[][] rows = new Cell[8+8+15+15][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                rows[i][j] = board.getCell(getIndex(j, i)); // Horizontal
                rows[8+i][j] = board.getCell(getIndex(i, j)); // Vertical
            }
            for (int j = 0, k = i; k < 8; j++, k++) {
                rows[16+i][j] = board.getCell(getIndex(j, k)); // Diagonal1
                if (i != 0)
                    rows[23+i][j] = board.getCell(getIndex(k, j)); // Diagonal1
            }
        }

        for(int i = 0 ; i < 15; i++) {
            for(int j = 0; j <= i; j++) {
                int k = i - j;
                if(k < 8 && j < 8) {
                    rows[31+i][j] = board.getCell(getIndex(k, j)); // Diagonal2
                }
            }
        }

        return rows;
    }

    @Override
    public boolean AIMove() {
        return false; // AI
    }
}
