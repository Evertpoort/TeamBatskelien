package Controller;

import Model.Model;
import View.View;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.awt.*;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;

//hkeopiuhui
public class Controller {
    public View view;
    private Model model;
    private LinkedBlockingQueue<String> queue;

    @FXML
    ComboBox<String> combobox;
    @FXML
    private RadioButton radio2,radio1;

    public Controller(View view,Model model){
        this.view= view;
        this.model=model;
        queue=model.returnInstance();
    }

    @FXML
    public void onEnter(ActionEvent event){
        // do login
        String command=((javafx.scene.control.TextField) event.getSource()).getText();
        queue.offer("Login "+ command);
        view.screenController.active("GameScreen");
       // System.out.println(queue.size());
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
