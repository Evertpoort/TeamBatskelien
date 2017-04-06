package Model;

import Model.Networker.Networker;
import java.util.concurrent.LinkedBlockingQueue;

public class Model {
    Networker networker;
    LinkedBlockingQueue<String> inputQueue;
    LinkedBlockingQueue<String> outputQueue;
    Game game;
    public Model(){
        inputQueue = new LinkedBlockingQueue<String>();
        outputQueue = new LinkedBlockingQueue<String>();
        networker = new Networker(outputQueue,inputQueue);
    }

    public void makeGame(String type, boolean playerTurn, Cell playerCellType) {
        if (type.equals("Reversi")) {
            if (playerCellType == null)
                playerCellType = Cell.KRUISJE;
            game = new Othello(outputQueue, playerTurn, playerCellType);
        } else if (type.equals("Tic-tac-toe")) {
            if (playerCellType == null)
                playerCellType = Cell.ZWART;
            game = new TicTacToe(outputQueue, playerCellType);
        } else {
            System.out.println("Unknown game type: " + type);
        }
    }

    public Game getGame() {
        return game;
    }

    public LinkedBlockingQueue<String> returnInstance(){
        return outputQueue;
    }
    public LinkedBlockingQueue<String> returnInputinstance(){return inputQueue;}
}