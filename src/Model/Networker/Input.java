package Model.Networker;

import Model.Model;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class Input implements Runnable  {
    BufferedReader in;
    JTextArea messageArea;
    LinkedBlockingQueue<String> queue2;
    public Input(BufferedReader in, JTextArea messageArea, LinkedBlockingQueue<String> queue2){
    this.in = in;
    this.messageArea=messageArea;
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
                    messageArea.append(line);
                    messageArea.append("\n");
                    messageArea.setCaretPosition(messageArea.getDocument().getLength());

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }}
}
