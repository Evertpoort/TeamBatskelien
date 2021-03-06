package Model.Networker;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class Input implements Runnable  {
    BufferedReader in;
    LinkedBlockingQueue<String> inputQueue;
    public Input(BufferedReader in, LinkedBlockingQueue<String> inputQueue){
        this.in = in;
        this.inputQueue=inputQueue;
    }

    @Override
    public void run() {
        String line;
        while (true){
            try {
                if ((line=in.readLine())!=null){
                    if (line.startsWith("SVR")||line.startsWith("ERR")){
                        inputQueue.offer(line);
                    }
                }
                else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}