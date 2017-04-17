package Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;

public class Othello extends Game {
    private final ArrayList<Integer>[] rows;
    private static final ExecutorService processingPool = Executors.newCachedThreadPool();
    private int currentsearchdepth;
    // Default values (adjustable in config.properties)
    private int minimalDepth = 7;
    private int timeoutDepth = 5;
    private int[] valueTable = {
        10000, -3000, 1000,  800,  800, 1000, -3000, 10000,
        -3000, -5000, -450, -500, -500, -450, -5000, -3000,
         1000,  -450,   30,   10,   10,   30,  -450,  1000,
          800,  -500,   10,   50,   50,   10,  -500,   800,
          800,  -500,   10,   50,   50,   10,  -500,   800,
         1000,  -450,   30,   10,   10,   30,  -450,  1000,
        -3000, -5000, -450, -500, -500, -450, -5000, -3000,
        10000, -3000, 1000,  800,  800, 1000, -3000, 10000,
    };

    public Othello(LinkedBlockingQueue<String> outputQueue, Properties prop, boolean playerTurn, Cell cellType) {
        super(outputQueue, prop, 8, cellType, cellType == Cell.ZWART ? Cell.WIT : Cell.ZWART);
        loadSettings();
        currentsearchdepth = minimalDepth + 1;
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
        updateValidIndexes(board,getValidIndexes(board, cellTypePlayer));
    }

    private void loadSettings() {
        if (prop.getProperty("OTHELLO_MINIMAL_DEPTH") != null)
            minimalDepth = Integer.parseInt(prop.getProperty("OTHELLO_MINIMAL_DEPTH").trim());
        if (prop.getProperty("OTHELLO_TIMEOUT_DEPTH") != null)
            timeoutDepth = Integer.parseInt(prop.getProperty("OTHELLO_TIMEOUT_DEPTH").trim());
        if (prop.getProperty("OTHELLO_VALUE_TABLE") != null) {
            String[] values = prop.getProperty("OTHELLO_VALUE_TABLE").split(",");
            for(int i = 0; i < 64; i++) {
                valueTable[i] = Integer.parseInt(values[i].trim());
            }
        }

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
        updateValidIndexes(board,getValidIndexes(board, cellTypePlayer));
        return true;
    }

    @Override
    public boolean AIMove() {
        int index = findBestMove();
        System.out.println("Move: " + index);
        super.move(index);
        playerScore++;
        updateBoard(board, true, index);
        updateValidIndexes(board, getValidIndexes(board, cellTypePlayer));
        return true;
    }

    @Override
    public void opponentMove(int index) {
        super.opponentMove(index);
        opponentScore++;
        updateBoard(board, true, index);
        updateValidIndexes(board,getValidIndexes(board, cellTypePlayer));
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
            for (int currentIndex : row) { // Controleer elke index per rij
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
            for (int currentIndex : row) { // Controleer elke index per rij
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
    private void updateValidIndexes(Cell[] board, ArrayList<Integer> validIndexes) {
        for (int i = 0; i < 64; i++) {
            if (validIndexes.contains(i)) {
                if (board[i] == Cell.EMPTY)
                    board[i] = Cell.EMPTY_VALID;
            } else if (board[i] == Cell.EMPTY_VALID)
                board[i] = Cell.EMPTY;
        }
    }

    // Minimax
    private int[] AIIndexScores = new int[64];
    private int findBestMove() {
        if (playerScore == 2 && opponentScore == 2)
            return 19; // Allereerste move maakt niet uit
        Arrays.fill(AIIndexScores, Integer.MIN_VALUE+1); // Geen value = Integer.MIN_VALUE+1
        ArrayList<Integer> validIndexes = getValidIndexes(board, cellTypePlayer);
        List<Callable<Object>> todo = new ArrayList<>();
        System.out.println("---- CALCULATING SCORES ---");
        for (int index : validIndexes) {
            todo.add(Executors.callable(new BestScorePlayerIndexJob(index)));
        }
        try {
            boolean failed = false;
            for (Future<Object> f : processingPool.invokeAll(todo, serverTimeout - 1500, TimeUnit.MILLISECONDS)) {
                if (f.isCancelled()) {
                    failed = true;
                }
            }
            if (failed) {
                while (true) { // Wachten op de scores van de depth waar de indexes zich momenteel nog bevinden
                    boolean scores = true;
                    for (int i : validIndexes) {
                        if (AIIndexScores[i] == Integer.MIN_VALUE+1) {
                            scores = false;
                            break;
                        }
                    }
                    if (scores)
                        break;
                }
                currentsearchdepth -= 2;
                if (currentsearchdepth < minimalDepth)
                    currentsearchdepth = minimalDepth;
            } else {
                    currentsearchdepth++;
            }
        } catch (InterruptedException | CancellationException e) {
            e.printStackTrace();
        }

        int best = Integer.MIN_VALUE;
        int bestMove = -1;
        for (int index : validIndexes) {
            if (AIIndexScores[index] >= best) {
                best = AIIndexScores[index];
                bestMove = index;
            }
        }
        System.out.println("---- FINISHED ---");
        return bestMove;
    }

    private class BestScorePlayerIndexJob implements Runnable {
        private int index;
        BestScorePlayerIndexJob(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            Cell[] currentboard = board.clone();
            currentboard[index] = cellTypePlayer;
            updateBoard(currentboard, false, index);
            int score = findBestScore(currentboard, 0, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
            System.out.println("Score index " + index + ": " + score + " (depth: " + (Thread.currentThread().isInterrupted() ?  "<" : "") + currentsearchdepth + ")");
            AIIndexScores[index] = score;
        }
    }

    private int findBestScore(Cell[] board, int depth, boolean playerTurn, int alpha, int beta) {
        if (depth == currentsearchdepth || (Thread.currentThread().isInterrupted() && depth >= timeoutDepth))
            return countAIScore(board);
        depth++;

        Cell cell = playerTurn ? cellTypePlayer : cellTypeOpponent;
        int result = playerTurn ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        Cell[] currentboard;
        for (int index : getValidIndexes(board, cell)) {
            currentboard = board.clone();
            currentboard[index] = cell;
            updateBoard(currentboard, false, index);
            int currenentresult = findBestScore(currentboard, depth, !playerTurn, alpha, beta);
            if (playerTurn) {
                if (currenentresult > result) {
                    result = currenentresult;
                    if (result == Integer.MAX_VALUE)
                        return findBestScore(currentboard, depth, true,alpha ,beta); // Nog een keer aan de beurt
                    alpha = Math.max(alpha,result);
                }

            } else {
                if (currenentresult < result) {
                    result = currenentresult;
                    if (result == Integer.MIN_VALUE)
                        return findBestScore(currentboard, depth, false,alpha ,beta);
                    beta = Math.min(beta,result);
                }

            }
            if (alpha >= beta) {
                break;
            }
        }
        return result;
    }

    private int countAIScore(Cell[] currentboard) {
        int score = 0;
        for (int i = 0; i < 64; i++) {
            if (currentboard[i] == cellTypePlayer) {
                score += valueTable[i];
            } else if (currentboard[i]==cellTypeOpponent){
                score -= valueTable[i];
            }
        }
        return score;
    }
}