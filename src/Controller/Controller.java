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
    private RadioButton radio2,radio1;
    @FXML
    private TextField login;

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
    public void selectedgamebox(ActionEvent e){
        String game = ((ComboBox)e.getSource()).getSelectionModel().getSelectedItem().toString();
        if (game.equals("Tic-tac-toe")){
            radio1.setText("Kruisje");
            radio2.setText("Rondje");
        }
        else if(game.equals("Reversi")){
            radio1.setText("Zwart");
            radio2.setText("Wit");
        }
    }

}
