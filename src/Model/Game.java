package Model;

public abstract class Game {
    public Board board;
    public boolean playerTurn;
    public Cell cellTypePlayer;
    public Cell cellTypeOpponent;
    public boolean AI;

    public Game(Board board, boolean playerTurn, Cell cellTypePlayer, Cell cellTypeOpponent , boolean AI) {
        this.board = board;
        this.playerTurn = playerTurn;
        this.cellTypePlayer = cellTypePlayer;
        this.cellTypeOpponent = cellTypeOpponent;
        this.AI = AI;
    }

    public Cell[] getBoard() {
        return board.getCells();
    }
	
	public void sendMove(int x, int y) {
		return;
	}

    public void opponentMove(int x, int y) {
        board.setCell(x, y, cellTypeOpponent);
    }

    public boolean move(int x, int y) {
        return false;
    }

    public boolean AIMove() {
        return false;
    }
}
