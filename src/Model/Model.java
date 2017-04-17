package Model;

import Model.Networker.Networker;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

public class Model {
    Networker networker;
    LinkedBlockingQueue<String> inputQueue;
    LinkedBlockingQueue<String> outputQueue;
    Game game;
    Properties prop;
    public Model(Properties prop){
        this.prop = prop;
        inputQueue = new LinkedBlockingQueue<>();
        outputQueue = new LinkedBlockingQueue<>();
        networker = new Networker(prop.getProperty("SERVER_IP"), Integer.parseInt(prop.getProperty("SERVER_PORT")), outputQueue,inputQueue);
    }

    public void makeGame(String type, boolean playerTurn, Cell playerCellType) {
        if (type.equals("Reversi")) {
            if (playerCellType == null)
                playerCellType = Cell.ZWART;
            game = new Othello(outputQueue, prop, playerTurn, playerCellType);
        } else if (type.equals("Tic-tac-toe")) {
            if (playerCellType == null)
                playerCellType = Cell.KRUISJE;
            game = new TicTacToe(outputQueue, prop, playerCellType);
        } else {
            System.out.println("Unknown game type: " + type);
        }
    }

    public Game getGame() {
        return game;
    }

    public LinkedBlockingQueue<String> returnOutputInstance(){
        return outputQueue;
    }

    public LinkedBlockingQueue<String> returnInputinstance(){
        return inputQueue;
    }
}