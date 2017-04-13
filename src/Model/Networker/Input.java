package Model.Networker;

import Model.Model;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class Input implements Runnable  {
    BufferedReader in;
    LinkedBlockingQueue<String> queue2;
    public Input(BufferedReader in, LinkedBlockingQueue<String> queue2){
        this.in = in;
        this.queue2=queue2;
    }
    @Override
    public void run() {
        String line;
        while (true){
            try {
                if ((line=in.readLine())!=null){
                    if (line.startsWith("SVR")||line.startsWith("ERR")){
                        queue2.offer(line);
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
        }}
}