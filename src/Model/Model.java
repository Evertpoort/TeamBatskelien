package Model;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by mark on 31-3-2017.
 */
public class Model {
    Networker networker;
    LinkedBlockingQueue<String> queue;
    public Thread t1;
    public Model(){
         queue =new LinkedBlockingQueue<String>();
        networker=new Networker(queue);
    }

    public LinkedBlockingQueue<String> returnInstance(){
        return queue;
    }
}
