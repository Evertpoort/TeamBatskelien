package Model;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

public class Othello extends Game {
    private final ArrayList<Integer>[] rows;
    private static final int[] valuetable = {
            50, -10, 5, 3, 3, 5, -10, 50,
            -10, -20, -3, -3, -3, -3, -20, -10,
            5, -3, 1, 1, 1, 1, -3, 5,
            3, -3, 1, 0, 0, 1, -3, 3,
            3, -3, 1, 0, 0, 1, -3, 3,
            5, -3, 1, 1, 1, 1, -3, 5,
            -10, -20, -3, -3, -3, -3, -20, -10,
            50, -10, 5, 3, 3, 5, -10, 50,
    };
    private static final int timeout = 9000;

    public Othello(LinkedBlockingQueue<String> outputQueue, boolean playerTurn, Cell cellType) {
        super(outputQueue, 8, cellType, cellType == Cell.ZWART ? Cell.WIT : Cell.ZWART);
        rows = getAllRows();
        playerScore = 2;
        opponentScore = 2;
        if (playerTurn) {
            board[27] = cellTypeOpponent;
            board[36] = cellTypeOpponent;
            board[28] = cellTypePlayer;
            board[35] = cellTypePlayer;
        } else {
            board[27] = cellTypePlayer;
            board[36] = cellTypePlayer;
            board[28] = cellTypeOpponent;
            board[35] = cellTypeOpponent;
        }
        updateValidIndexesBoard(board,getValidIndexes(board, cellTypePlayer));
    }

    @Override
    public boolean move(int index) {
        if (!playerTurn) {
            System.out.println("It's not your turn!");
            return false;
        }
        if (board[index] != Cell.EMPTY_VALID) {
            System.out.println("Not a valid move!");
            return false;
        }
        playerTurn = false;
        super.move(index);
        playerScore++;
        updateBoard(board, true, index);
        updateValidIndexesBoard(board,getValidIndexes(board, cellTypePlayer));
        return true;
    }

    @Override
    public void opponentMove(int index) {
        super.opponentMove(index);
        opponentScore++;
        updateBoard(board, true, index);
        updateValidIndexesBoard(board,getValidIndexes(board, cellTypePlayer));
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

    private void updateBoard(Cell[] board, boolean updateScores, int indexMove) {
        for (ArrayList<Integer> row : rows) { // Elke rij
            int startIndex = -1;
            boolean needFlip = false;
            for (Integer currentIndex : row) { // Controleer elke index per rij
                Cell cellType = board[currentIndex];
                if (cellType == Cell.EMPTY || cellType == Cell.EMPTY_VALID) {
                    startIndex = -1;
                    if (needFlip)
                        needFlip = false;
                } else if (cellType == board[indexMove]) { // Cell is van player
                    if (needFlip) {
                        if (currentIndex == indexMove || startIndex == indexMove) { // Flip de cells als het bij de move hoort
                            for (int i = row.indexOf(startIndex) + 1; i < row.indexOf(currentIndex); i++) {
                                board[row.get(i)] = cellType;
                                if (updateScores) {
                                    if (cellType == cellTypePlayer) {
                                        playerScore++;
                                        opponentScore--;
                                    } else {
                                        playerScore--;
                                        opponentScore++;
                                    }
                                }
                            }
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

    private ArrayList<Integer> getValidIndexes(Cell[] board, Cell playerCell) {
        ArrayList<Integer> validIndexes = new ArrayList<>();
        for (ArrayList<Integer> row : rows) { // Elke rij
            int startIndex = -1;
            boolean c = false;
            for (Integer currentIndex : row) { // Controleer elke index per rij
                Cell cellType = board[currentIndex];
                if (cellType == Cell.EMPTY || cellType == Cell.EMPTY_VALID) {
                    if (startIndex == -2) { // Als er cellen zijn geweest van de player en daarna tegenstander cellen (player->tegenstander->currentIndex)
                        if (!validIndexes.contains(currentIndex))
                            validIndexes.add(currentIndex);
                    }
                    startIndex = currentIndex;
                    c = false;
                } else if (cellType == playerCell) {
                    if (c && startIndex > -1) { // Als er cellen zijn geweest van de tegenstander en daarna de player cell (startIndex->tegenstander->player)
                        if (!validIndexes.contains(startIndex))
                            validIndexes.add(startIndex);
                    }
                    startIndex = -3;
                } else {
                    if (startIndex > -1)
                        c = true;
                    else if (startIndex < -1)
                        startIndex = -2;
                }
            }
        }
        return validIndexes;
    }

    // Hoeft alleen uitgevoerd te worden wanneer de GUI geupdate moet worden
    private void updateValidIndexesBoard(Cell[] board, ArrayList<Integer> validIndexes) {
        for (int i = 0; i < 64; i++) {
            if (validIndexes.contains(i)) {
                if (board[i] == Cell.EMPTY)
                    board[i] = Cell.EMPTY_VALID;
            } else if (board[i] == Cell.EMPTY_VALID)
                board[i] = Cell.EMPTY;
        }
    }

    @Override
    public boolean AIMove() {
        int index = findBestMove();
        System.out.println("Move: " + index);
        super.move(index);
        playerScore++;
        updateBoard(board, true, index);
        updateValidIndexesBoard(board, getValidIndexes(board, cellTypePlayer));
        return true;
    }

    // Voorbeeld: https://pastebin.com/LVnpfh5G
    private long timeforcalc;
    private int findBestMove() {
        Cell[] currentboard;
        int searchdepth = 6;
        int best = Integer.MIN_VALUE;
        int bestMove = -1;
        int currentbest;
        System.out.println("---- CALCULATING SCORES (" + searchdepth + ") ---");
        long starttime;
        timeforcalc= timeout/getValidIndexes(board,cellTypePlayer).size();
        for (int index : getValidIndexes(board, cellTypePlayer)) {
            starttime=System.currentTimeMillis();
            currentboard = board.clone();
            currentboard[index] = cellTypePlayer;
            updateBoard(currentboard, false, index);

            currentbest = findBestScore(currentboard, searchdepth, Integer.MIN_VALUE,Integer.MAX_VALUE, false,starttime);
            System.out.println("Score index " + index + ": " + currentbest);
            if (currentbest == Integer.MAX_VALUE) {
                bestMove = index;
                System.out.println("Move gevonden waarbij de tegenstander geen move meer kan doen");
                break; // Beste move voor de tegenstader is geen move, dus sws beste move om te doen
            }
            if (currentbest >= best) {
                best = currentbest;
                bestMove = index;
            }
        }
        System.out.println("---- FINISHED ---");
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return bestMove;
    }

    private int findBestScore(Cell[] board, int searchdepth, int alpha, int beta, boolean playerTurn,long starttime) {
        if (searchdepth == 0)
            return countAIScore(board);
//      if ((starttime+timeforcalc)>=System.currentTimeMillis()){
//          System.out.println("I quit, the current searchdepth==" +searchdepth + " and i used time "+ timeforcalc+starttime + " > " + System.currentTimeMillis());
//          System.out.println("I have to following time for my calc " + timeforcalc + " start time was " + starttime);
//          return countAIScore(board);
//        }
        searchdepth--;

        Cell cell = playerTurn ? cellTypePlayer : cellTypeOpponent;
        int result = playerTurn ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        Cell[] currentboard;
        for (int index : getValidIndexes(board, cell)) {
            currentboard = board.clone();
            currentboard[index] = cell;
            updateBoard(currentboard, false, index);
            int currenentresult = findBestScore(currentboard, searchdepth,alpha,beta, !playerTurn,starttime);
            if (playerTurn) {
                if (currenentresult > result) {
                    result = currenentresult;
                    if (result == Integer.MAX_VALUE)
                        break; // Beste move voor de tegenstader is geen move, dus sws beste move om te doen
                }
                if (result>alpha){
                    alpha=result;
                }
            } else {
                if (currenentresult < result) {
                    result = currenentresult;
                    if (result == Integer.MIN_VALUE)
                        break; // Beste move voor de player is geen move, dus sws beste move om te doen
                }
                if (result<beta){
                    beta=result;
                }
            }
            if (alpha>=beta){
                break;
            }
        }
        return result;
    }

    private int countAIScore(Cell[] currentboard) {
        int score = 0;
        for (int i = 0; i < 64; i++) {
            if (currentboard[i] == cellTypePlayer) {
                score += valuetable[i];
            } else if (currentboard[i]==cellTypeOpponent){
                score -= valuetable[i];
            }
        }
        return score;
    }
}