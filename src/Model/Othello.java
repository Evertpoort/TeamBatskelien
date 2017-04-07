package Model;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class Othello extends Game {
    ArrayList<Integer> validIndexes = new ArrayList<>();

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
        if (!validIndexes.contains(index)) {
//            System.out.println("Not a valid move!");
//            return false;
        }
        playerTurn = false;
        board.setCell(index, cellTypePlayer);
        updateBoard(index);
        sendMoveToServer(index);
        return true;
    }

    @Override
    public void opponentMove(int index) {
        super.opponentMove(index);
        updateBoard(index);
    }

    private void updateBoard(int indexMove) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++)
                checkCell(getIndex(j, i), indexMove); // Horizontal
            startIndex = -1;
            for (int j = 0; j < 8; j++)
                checkCell(getIndex(i, j), indexMove); // Vertical
            startIndex = -1;
            for (int j = 0, k = i; k < 8; j++, k++) {
                checkCell(getIndex(j, k), indexMove); // Diagonal1
            }
            startIndex = -1;
            for (int j = 0, k = i; k < 8; j++, k++) {
                checkCell(getIndex(k, j), indexMove); // Diagonal1
            }
            startIndex = -1;
        }

        for(int i = 0 ; i < 15; i++) {
            for(int j = 0; j <= i; j++) {
                int k = i - j;
                if(k < 8 && j < 8) {
                    checkCell(getIndex(k, j), indexMove); // Diagonal2
                }
            }
            startIndex = -1;
        }

        validIndexes = new ArrayList<>();
    }

    private int startIndex = -1;
    private ArrayList<Integer> needFlip = new ArrayList<>();
    private void checkCell(int currentIndex, int clickedIndex) {
        Cell cellType = board.getCell(currentIndex);
        if (cellType == Cell.EMPTY) {
            startIndex = -1;
            if (!needFlip.isEmpty())
                needFlip.clear();
        } else if (cellType == board.getCell(clickedIndex)) {
            if (!needFlip.isEmpty()) {
                if ((currentIndex == clickedIndex && startIndex != -1) || startIndex == clickedIndex) {
                    for (int i : needFlip) {
                        Cell cell = board.getCell(i);
                        if (cell == Cell.ZWART)
                            cell = Cell.WIT;
                        else
                            cell = Cell.ZWART;
                        board.setCell(i, cell);
                    }
                }
                needFlip.clear();
            }
            startIndex = currentIndex;
        } else if (startIndex != -1) {
            needFlip.add(currentIndex);
        }

//        if (cellType == Cell.EMPTY || cellType == null || current == Cell.EMPTY || current == null  || cellType == current) {
//            if (!needFlip.isEmpty()) {
//                if (cellType == current) {
//                    for (int i : needFlip) {
//                        Cell cell = board.getCell(i);
//                        if (cell == Cell.ZWART)
//                            cell = Cell.WIT;
//                        else
//                            cell = Cell.ZWART;
//                        board.setCell(i, cell);
//                    }
//                }
//                needFlip.clear();
//            }
//            current = cellType;
//        } else {
//            needFlip.add(index);
//        }
    }

    @Override
    public boolean AIMove() {
        return false; // AI
    }
}
