package Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

public class Othello extends Game {
    private final ArrayList<Integer>[] rows;

    public Othello(LinkedBlockingQueue<String> outputQueue, boolean playerTurn, Cell cellType){
        super(outputQueue, 8, cellType, cellType == Cell.ZWART ? Cell.WIT : Cell.ZWART);
        rows = getAllRows();
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
        updateValidIndexesBoard(getValidIndexes());
    }

    @Override
    public boolean move(int index) {
        if (!playerTurn) {
            System.out.println("It's not your turn!");
            return false;
        }
        if (board.getCell(index) != Cell.EMPTY_VALID) {
            System.out.println("Not a valid move!");
            return false;
        }
        playerTurn = false;
        super.move(index);
        updateBoard(rows, index);
        updateValidIndexesBoard(getValidIndexes());
        return true;
    }

    @Override
    public void opponentMove(int index) {
        super.opponentMove(index);
        updateBoard(rows, index);
        updateValidIndexesBoard(getValidIndexes());
    }

    // Hoeft maar 1 keer gecalld te worden
    private ArrayList<Integer>[] getAllRows() {
        ArrayList<Integer>[] rows = new ArrayList[8+8+15+15];
        for(int i=0; i < rows.length; i++)
            rows[i] = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                rows[i].add(getIndex(j, i)); // Horizontal
                rows[8+i].add(getIndex(i, j)); // Vertical
            }
            for (int j = 0, k = i; k < 8; j++, k++) {
                rows[16+i].add(getIndex(j, k)); // Diagonal1
                if (i != 0)
                    rows[23+i].add(getIndex(k, j)); // Diagonal1
            }
        }

        for(int i = 0 ; i < 15; i++) {
            for(int j = 0; j <= i; j++) {
                int k = i - j;
                if(k < 8 && j < 8) {
                    rows[31+i].add(getIndex(k, j)); // Diagonal2
                }
            }
        }

        return rows;
    }

    private void updateBoard(ArrayList<Integer>[] rows, int indexMove) {
        for (ArrayList<Integer> row : rows) { // Elke rij
            int startIndex = -1;
            boolean needFlip = false;
            for (Integer currentIndex : row) { // Controleer elke index per rij
                Cell cellType = board.getCell(currentIndex);
                if (cellType == Cell.EMPTY || cellType == Cell.EMPTY_VALID) {
                    startIndex = -1;
                    if (needFlip)
                        needFlip = false;
                } else if (cellType == board.getCell(indexMove)) { // Cell is van player
                    if (needFlip) {
                        if (currentIndex == indexMove || startIndex == indexMove) { // Flip de cells als het bij de move hoort
                            for (int i = row.indexOf(startIndex) + 1; i < row.indexOf(currentIndex); i++)
                                board.setCell(row.get(i), cellType);
                        }
                        needFlip = false;
                    }
                    startIndex = currentIndex;
                } else if (startIndex != -1) { // Cell is van tegenstander
                    needFlip = true;
                }
            }
        }
    }

    private ArrayList<Integer> getValidIndexes() {
        ArrayList<Integer> validIndexes = new ArrayList<>();
        for (ArrayList<Integer> row : rows) { // Elke rij
            int startIndex = -1;
            boolean c = false;
            for (Integer currentIndex : row) { // Controleer elke index per rij
                Cell cellType = board.getCell(currentIndex);
                if (cellType == Cell.EMPTY || cellType == Cell.EMPTY_VALID) {
                    if (startIndex == -2) { // Als er cellen zijn geweest van de player en daarna tegenstander cellen (player->tegenstander->currentIndex)
                        if (!validIndexes.contains(startIndex))
                            validIndexes.add(currentIndex);
                    }
                    startIndex = currentIndex;
                    c = false;
                } else if (cellType == cellTypeOpponent) {
                    if (startIndex > -1)
                        c = true;
                    else if (startIndex < -1)
                        startIndex = -2;
                } else if (cellType == cellTypePlayer) {
                    if (c && startIndex > -1) { // Als er cellen zijn geweest van de tegenstander en daarna de player cell (startIndex->tegenstander->player)
                        if (!validIndexes.contains(startIndex))
                            validIndexes.add(startIndex);
                    }
                    startIndex = -3;
                }
            }
        }
        return validIndexes;
    }

    // Hoeft alleen uitgevoerd te worden wanneer de GUI geupdate moet worden
    private void updateValidIndexesBoard(ArrayList<Integer> validIndexes) {
        for (int i = 0; i < 64; i++) {
            if (validIndexes.contains(i)) {
                if (board.getCell(i) == Cell.EMPTY)
                    board.setCell(i, Cell.EMPTY_VALID);
            } else if (board.getCell(i) == Cell.EMPTY_VALID)
                    board.setCell(i, Cell.EMPTY);
        }
    }

    @Override
    public boolean AIMove() {
        return false; // AI
    }
}
