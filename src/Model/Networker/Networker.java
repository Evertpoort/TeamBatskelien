package Model.Networker;

import Model.Model;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

//sourec  http://cs.lmu.edu/~ray/notes/javanetexamples/

/**
 * Created by mark on 24-3-2017.
 */
public class Networker {
   private  Socket soc;
   private BufferedReader in;
   private PrintWriter out;
   private LinkedBlockingQueue<String> queue;
   private LinkedBlockingQueue<String> queue2;
    JFrame frame = new JFrame("Chatter");
    JTextField textField = new JTextField(40);
    JTextArea messageArea = new JTextArea(20, 40);
    JScrollPane jscroll= new JScrollPane(messageArea);

    public Networker(LinkedBlockingQueue<String> queue,LinkedBlockingQueue queue2){
    this.queue= queue;
    this.queue2 = queue2;
    start();
    }


    public void start() {

        frame.getContentPane().add(textField,"North");
        frame.getContentPane().add(jscroll,"Center");
        frame.pack();
        frame.setVisible(true);
//qwe
        try {
            soc = new Socket("127.0.0.1",7789);
            in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            out = new PrintWriter(soc.getOutputStream(),true);
            Thread t1= new Thread(new Input(in,messageArea,queue2));
            t1.start();
            Thread t2= new Thread(new Output(out,queue,messageArea));
            t2.start();
        } catch (IOException  e ) {
            e.printStackTrace();
        }
    }
}


