package Model;

import Model.Networker.Networker;
import java.util.concurrent.LinkedBlockingQueue;

public class Model {
    Networker networker;
    LinkedBlockingQueue<String> queue;
    LinkedBlockingQueue<String> queue2;
    Game game;
    public Model(){
        queue = new LinkedBlockingQueue<String>();
        queue2 = new LinkedBlockingQueue<String>();
        networker = new Networker(queue,queue2);
    }

    public void makeGame(Cell cellType) {
        game = new TicTacToe(cellType);
    }

    public Game getGame() {
        return game;
    }

    public LinkedBlockingQueue<String> returnInstance(){
        return queue;
    }
    public LinkedBlockingQueue<String> returnInputinstance(){return queue2;}
}