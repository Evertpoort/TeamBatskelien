package Model;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by mark on 31-3-2017.
 */
public class Input implements Runnable  {
    BufferedReader in;
    JTextArea messageArea;
    public Input(BufferedReader in,JTextArea messageArea){
    this.in = in;
    this.messageArea=messageArea;
    }
    @Override
    public void run() {
        String line;
        while (true){


            try {
                if ((line=in.readLine())!=null){
                    messageArea.append(line);
                    messageArea.append("\n");
                    messageArea.setCaretPosition(messageArea.getDocument().getLength());

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }}

}
