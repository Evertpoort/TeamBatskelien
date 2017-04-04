package Model;

import java.util.Arrays;

import static java.lang.Math.pow;

public class Board {
    private Cell[] board;
    private int size;

    public Board(int size) {
        this.size = size;
        this.board = new Cell[(int)(pow(size, 2))];
        Arrays.fill(this.board, Cell.EMPTY);
    }

    public Cell[] getCells() {
        return board;
    }

    public Cell getCell(int index) {
        return board[index];
    }

    public void setCell(int index, Cell cell) {
        board[index] = cell;
    }
}
