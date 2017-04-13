package Model.Networker;

import Model.Model;

import javax.swing.*;
import java.io.PrintWriter;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Output extends TimerTask implements Runnable {
    private PrintWriter out;
    private LinkedBlockingQueue<String> queue;
    private JTextArea messageArea;
    public Output(PrintWriter out,LinkedBlockingQueue<String> queue){
        this.out=out;
        this.queue=queue;
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                updatetable();
            }
        }, 0, 5000, TimeUnit.MILLISECONDS);
    }
    @Override
    public void run() {
        while (true) {
            if (queue.size() > 0) {
                makecommand(queue.remove());
            }
            else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updatetable(){
        makecommand("get playerlist");
    }

    public void makecommand(String str){
        out.println(str);
    }
}
