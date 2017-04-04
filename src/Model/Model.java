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
        networker = new Networker(inputQueue,outputQueue);
    }

    public void makeGame(String type, Cell cellType) {
        if (type.equals("Reversi"))
            return;
        else if (type.equals("Tic-tac-toe"))
            game = new TicTacToe(outputQueue, cellType);
        else
            System.out.println("Unknown game type: " + type);
    }

    public Game getGame() {
        return game;
    }

    public LinkedBlockingQueue<String> returnInstance(){
        return inputQueue;
    }
    public LinkedBlockingQueue<String> returnInputinstance(){return outputQueue;}
}