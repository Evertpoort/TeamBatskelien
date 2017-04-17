package Model.Networker;

import java.io.PrintWriter;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Output extends TimerTask implements Runnable {
    private PrintWriter out;
    private LinkedBlockingQueue<String> outputQueue;
    public Output(PrintWriter out,LinkedBlockingQueue<String> outputQueue){
        this.out=out;
        this.outputQueue=outputQueue;
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(this::updatetable, 0, 5000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        while (true) {
            if (outputQueue.size() > 0) {
                makecommand(outputQueue.remove());
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
