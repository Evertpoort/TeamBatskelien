package Controller;

import View.View;
import Model.Model;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputHandler implements Runnable {
    Controller controller;
    View view;
    Model model;
    LinkedBlockingQueue<String> inputQueue;

    public InputHandler(Controller controller, Model model, View view, LinkedBlockingQueue<String> inputQueue){
        this.controller = controller;
        this.view = view;
        this.model = model;
        this.inputQueue = inputQueue;
    }
    @Override
    public void run() {
        while (true){
            try {
                String command=inputQueue.take();
                ArrayList<String> args = parseArgs(command); // creates an arraylist for when the string contain ""
                if (command.contains("YOURTURN")) {
                    controller.setPlayerTurn(true);
                    if (controller.getAI()) {
                        model.getGame().AIMove();
                    } else {
                        model.getGame().setPlayerTurn();
                    }
                } else if (command.contains("PLAYERLIST")) {
                    command= command.substring(command.indexOf("[")+1,command.indexOf("]")).replace(", ","");
                    String[] list = command.split("\"");
                    controller.updateplayerlist(list);
                } else if (command.contains("MATCH")) {
                    Boolean yourturn;
                    if (args.get(0).equals(controller.getPlayerName())){
                        yourturn=true;
                    } else {
                        yourturn=false;
                    }
                    model.makeGame(args.get(1),yourturn,controller.getPlayerCellType());
                    controller.loadgame();
                    controller.onupdate();
                    if (yourturn)
                        controller.setPlayerTurn(true);
                    if (args.get(1).equals("Tic-tac-toe")) {
                        controller.hidescorelabels(false);
                    }
                } else if (command.contains("MOVE")) {
                    if (!args.get(0).equals(controller.getPlayerName())) {
                        model.getGame().opponentMove(Integer.parseInt(args.get(1)));
                    }
                    controller.onupdate();
                } else if (command.contains("CHALLENGENUMBER")&&!command.contains("CANCELLED")) {
                    controller.invitereceived(args.get(0), Integer.parseInt(args.get(1)),args.get(2));
                } else if (command.contains("WIN")) {
                 controller.displaystatus("Win");
                } else if (command.contains("LOSS")) {
                    controller.displaystatus("Lose");
                } else if (command.contains("DRAW")) {
                    controller.displaystatus("Draw");
                } else if (command.contains("disabled")||command.contains("Duplicate")) {
                    controller.backToLogin();
                } else {
                    System.out.println("Warning: (unknown) command ignored: " + command);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private ArrayList<String> parseArgs(String args) {
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(args);
        ArrayList<String> list = new ArrayList<>();
        while (m.find()) {
            list.add(m.group(1));
        }
        return list;
    }
}
