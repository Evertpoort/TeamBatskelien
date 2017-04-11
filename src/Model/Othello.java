package Model;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

public class Othello extends Game {
    private final ArrayList<Integer>[] rows;

    public Othello(LinkedBlockingQueue<String> outputQueue, boolean playerTurn, Cell cellType){
        super(outputQueue, 8, cellType, cellType == Cell.ZWART ? Cell.WIT : Cell.ZWART);
        rows = getAllRows();
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
        updateValidIndexesBoard(getValidIndexes());
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
                Cell cellType = board[currentIndex];
                if (cellType == Cell.EMPTY || cellType == Cell.EMPTY_VALID) {
                    startIndex = -1;
                    if (needFlip)
                        needFlip = false;
                } else if (cellType == board[indexMove]) { // Cell is van player
                    if (needFlip) {
                        if (currentIndex == indexMove || startIndex == indexMove) { // Flip de cells als het bij de move hoort
                            for (int i = row.indexOf(startIndex) + 1; i < row.indexOf(currentIndex); i++)
                                board[row.get(i)] = cellType;
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
                Cell cellType = board[currentIndex];
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
                if (board[i] == Cell.EMPTY)
                    board[i] = Cell.EMPTY_VALID;
            } else if (board[i] == Cell.EMPTY_VALID)
                    board[i] = Cell.EMPTY;
        }
    }

    @Override
    public boolean AIMove() {
        ArrayList<Integer> validIndexes = getValidIndexes();
        int rnd = validIndexes.get(new Random().nextInt(validIndexes.size()));
        board[rnd] = cellTypePlayer;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        updateBoard(rows, rnd);
        updateValidIndexesBoard(getValidIndexes());
        sendMoveToServer(rnd);
        return true;
    }
}

// @Override
//public boolean AIMove() {
//    ArrayList<Integer> list = new ArrayList<>(); // to replace..
//    Board currentboard;
//    int searchdepth=  3;
//    int best=Integer.MIN_VALUE;
//    int bestindex;
//    int currentbest;
//    boolean currentturn;
//    for (int i= 0; i<list.size(); i++){
//        currentboard=board;
//        currentturn= playerTurn;
//        // makemove on the currentboard
//        currentbest=findbest(currentboard,list, searchdepth, currentturn);
//        if (currentbest>=best){
//            best=currentbest;
//            bestindex=i;
//        }
//    }
//    return false; // AI
//}
//
//    public int findbest(Board currentboard, ArrayList<Integer> validIndexes,int searchdepth,boolean currenntturn){
//        int resultaat= Integer.MIN_VALUE;
//        if (searchdepth==0){
//            return countscore(currentboard);
//        }
//        else {
//            for (int i= 0; i< validIndexes.size(); i++){
//                //makemoveon theboard
//                int currenentresult= findbest(currentboard,validIndexes,searchdepth--,currenntturn)
//                if (currenntturn){
//                    if(resultaat<currenentresult){
//                        resultaat=currenentresult;
//                    }
//                }
//                else {
//                    if(resultaat>currenentresult){
//                        resultaat=currenentresult;
//                    }
//                }
//            }
//        }
//
//        return resultaat;
//    }
//
//    private static final int[] valuetable = {
//            50, -10,   5,  3,  3,  5, -10,  50,
//            -10,-20,  -3, -3, -3, -3, -20, -10,
//            5,  -3 ,   1,  1,  1,  1, -3,    5,
//            3,  -3,    1,  0,  0,  1, -3,    3,
//            3,  -3,    1,  0,  0,  1, -3,    3,
//            5,  -3 ,   1,  1,  1,  1, -3,    5,
//            -10,-20,  -3, -3, -3, -3, -20, -10,
//            50, -10,   5,  3,  3,  5, -10,  50,
//    };
//    public int countscore (Board currentboard){
//        Cell[] cells = currentboard.getCells();
//        int score = 0;
//
//        for (int i= 0; i<cells.length; i++){
//            int value;
//
//            value= valuetable[i];
//            if (cells[i]==cellTypePlayer){
//                score=+value;
//            }
//            else {
//                score=-value;
//            }
//        }
//        return score;
//    }