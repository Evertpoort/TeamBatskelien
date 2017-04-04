package Model;

import java.util.List;
import java.lang.Math.*;

import static java.lang.Math.sqrt;

public class TicTacToe extends Game {
    public TicTacToe(boolean playerTurn, Cell cellType){
        super(new Board(3), playerTurn, cellType, cellType == Cell.KRUISJE ? Cell.RONDJE : Cell.KRUISJE, false);
    }

    @Override
    public boolean move(int x, int y) {
        if (state != GameState.PLAYER_TURN) {
            System.out.println("It's not your turn!");
            return false;
        }
        state = GameState.OPPONENT_TURN;
        if (board.getCell(x, y) != Cell.EMPTY) {
            System.out.println("Cell not empty!");
            return false;
        }
        board.setCell(x, y, cellTypePlayer);
        checkWinner();
        return true;
    }




    // TODO: Network
    private void checkWinner() { // (Wordt door server gedaan; is nu alleen voor testen)
        Cell[] row = new Cell[3];
        Cell[] rowv = new Cell[3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                row[j] = board.getCell(j, i);
                rowv[j] = board.getCell(i, j);
            }
            checkRow(row);
            checkRow(rowv);
        }
        row[0] = board.getCell(0, 0);
        row[1] = board.getCell(1, 1);
        row[2] = board.getCell(2, 2);
        checkRow(row);
        row[0] = board.getCell(0, 2);
        row[2] = board.getCell(2, 0);
        checkRow(row);
    }

    private void checkRow(Cell[] row) {
        if (row[0] == row[1] && row[1] == row[2]) {
            if (row[0] == cellTypePlayer)
                state = GameState.PLAYER_WON;
            else if (row[0] == cellTypeOpponent)
                state = GameState.OPPONENT_WON;
        }
    }

    public static void main(String[] args) {
        TicTacToe game = new TicTacToe(true, Cell.KRUISJE);
        game.move(0, 0);
        System.out.println(game.getGameState());
    }
}
