package Model;

import java.util.Arrays;

import static java.lang.Math.pow;

public class Board {
    private Cell[] board;
    private int size;

    private int getIndex(int x, int y) {
        return y * size + x;
    }

    public Board(int size) {
        this.size = size;
        this.board = new Cell[(int)(pow(size, 2))];
        Arrays.fill(this.board, Cell.EMPTY);
    }

    public Cell getCell(int x, int y) {
        return board[getIndex(x, y)];
    }

    public void setCell(int x, int y, Cell cell) {
        board[getIndex(x, y)] = cell;
    }
}
