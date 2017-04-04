package Model.Networker;

import Model.Model;

import javax.swing.*;
import java.io.PrintWriter;
import java.util.concurrent.LinkedBlockingQueue;

public class Output implements Runnable {
    private PrintWriter out;
    private LinkedBlockingQueue<String> queue;
    private JTextArea messageArea;
    public Output(PrintWriter out,LinkedBlockingQueue<String> queue,JTextArea messageArea){
        this.out=out;
        this.queue=queue;
        this.messageArea=messageArea;
    }
    @Override
    public void run() {
        while (true) {
            if (queue.size() > 0) {
                makecommand(queue.remove());
            }
        }
    }


    public void makecommand(String str){
        messageArea.append(str);
        messageArea.append("\n");
        out.println(str);
    }
}
