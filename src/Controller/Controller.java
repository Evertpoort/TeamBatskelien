package Controller;

import Model.Model;
import View.View;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.awt.*;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;

//hkeopiuhui

/*
    login <speler>          (String)        > Logs in the player
    logout | exit | quit | disconnect | bye > Logs out the player
            queue.offer("Logout");

    get gamelist                            > Returns a list of supported games
            queue.offer("get gamelist");

    get playerlist                          > Returns a list of players
            queue.offer("get playerlist");

    subscribe <speltype>    (Reversi / Tic-tac-toe) > Subscribes player to game type (case sensitive)
                queue.offer("subscribe " + command);

    move <zet>              (getal tussen 1-9)      > Executes move
                    queue.offer("move " + command);

    forfeit                                         > Player forfeits game
                        queue.offer("forfeit");

    challenge <player> <gametype>                   > Challenges <a> to play a game of <b>
    e.g. challenge "Eppo" "Tic-tac-toe" (case sensitive, requires "" around each argument)
                        queue.offer("challenge " + command);

    challenge accept <uitdaging nummer>             > Accepts challenge
                        queue.offer("challenge accept " + command);

 */


public class Controller {
    public View view;
    private Model model;
    private LinkedBlockingQueue<String> queue;

    @FXML
    ComboBox<String> combobox;
    @FXML
    private RadioButton radiotype2,radiotype1,radiogame1,radiogame2;
    @FXML
    private TextField login;
    @FXML
    private ToggleGroup gamegroup,typegroup;

    public Controller(View view,Model model){
        this.view= view;
        this.model=model;
        queue=model.returnInstance();
    }

    @FXML
    public void onEnter(ActionEvent event){
        // do login
        String command= login.getText();
        queue.offer("Login "+ command);
        view.screenController.active("GameScreen");
       // System.out.println(queue.size());
    }

    @FXML
    public void loginClick(ActionEvent event){
        onEnter(event);
    }

    @FXML
    public void gamegroupaction(ActionEvent event){
        String word =((RadioButton)event.getSource()).getText();
        if (word.equals("TicTacToe")){
            radiotype1.setText("Kruisje");
            radiotype2.setText("Rondje");
        }
        else {
            radiotype1.setText("Zwart");
            radiotype2.setText("Wit");
        }
    }

    @FXML
    public void challegenebuttonclicked(ActionEvent event){
        String command= "Challenge "+ "\"Dirk\""
                + "\""+ ((RadioButton)gamegroup.getSelectedToggle()).getText()+ "\""  ;
        System.out.println(command);
        queue.offer(command);
    }
}
