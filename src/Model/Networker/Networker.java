package Model.Networker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

//Source  http://cs.lmu.edu/~ray/notes/javanetexamples/
public class Networker {
    private LinkedBlockingQueue<String> inputQueue;
    private LinkedBlockingQueue<String> outputQueue;
    private String serverIp;
    private int serverPort;

    public Networker(String serverIp, int serverPort, LinkedBlockingQueue<String> outputQueue, LinkedBlockingQueue<String> intputQueue){
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.inputQueue = intputQueue;
        this.outputQueue = outputQueue;
        start();
    }

    public void start() {
        try {
            Socket soc = new Socket(serverIp, serverPort);
            BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            PrintWriter out = new PrintWriter(soc.getOutputStream(), true);
            Thread t1 = new Thread(new Input(in, inputQueue));
            t1.start();
            Thread t2 = new Thread(new Output(out, outputQueue));
            t2.start();
        } catch (IOException  e) {
            System.out.println("Can't connect to " + serverIp + ":" + serverPort);
            e.printStackTrace();
            System.exit(1);
        }
    }
}


