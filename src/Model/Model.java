package Model;

import Model.Networker.Networker;

import java.util.concurrent.LinkedBlockingQueue;

public class Model {
    Networker networker;
    LinkedBlockingQueue<String> queue;
    LinkedBlockingQueue<String> queue2;
    Game game;
    public Thread t1;
    public Model(){
        queue =new LinkedBlockingQueue<String>();
        queue2 = new LinkedBlockingQueue<String>();
        networker=new Networker(queue,queue2);
        game= new TicTacToe(false, Cell.KRUISJE);
    }

    public LinkedBlockingQueue<String> returnInstance(){
        return queue;
    }
    public LinkedBlockingQueue<String> returnInputinstance(){return queue2;}
 }
