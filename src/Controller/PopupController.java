package Controller;

import Model.Cell;
import View.View;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.naming.ldap.Control;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by mark on 5-4-2017.
 */
public class PopupController {

    @FXML
    private ToggleGroup typegroup2;
    @FXML
    private Label invitelabel;
    @FXML
    private RadioButton radiotype2,radiotype1, radiotype21, radiotype22;
    @FXML
    private ToggleGroup gamegroup,typegroup;
    @FXML
    private CheckBox checkboxai,checkboxai1;

    private Controller controller;
    private View view;
    private int challengenumber;
    private LinkedBlockingQueue<String> queue;
    private String selectedPlayer;
    private Boolean randomqueue;

    public PopupController(Controller controller, View view, LinkedBlockingQueue<String> queue){
        this.controller= controller;
        this.view= view;
        this.queue=queue;
    }
    public void setselectedPlayer(String name){
        this.selectedPlayer=name;
    }

    public void invitereceived(String playername,int challengenumber, String game) {
        this.challengenumber = challengenumber;
        Platform.runLater(() -> {
            invitelabel.setText(playername + " has invited you to a game of " + game);
            final Stage popup = new Stage();
            BorderPane pane = new BorderPane();
            view.popupscreenController.active("GameInvite", popup, pane);
            popup.setScene(new Scene(pane));
            popup.setTitle("Challenge");
            popup.show();
            if (game.equals("Tic-tac-toe")) {
                radiotype21.setText("Kruisje");
                radiotype22.setText("Rondje");
                checkboxai1.setDisable(true);
            } else {
                radiotype21.setText("Zwart");
                radiotype22.setText("Wit");
                checkboxai1.setDisable(false);
            }
        });
    }


    @FXML
    public void inviteaccept(ActionEvent event){
        queue.offer("challenge accept "+ challengenumber);
        if (((RadioButton) typegroup2.getSelectedToggle()).getText().equals("Kruisje")){
            controller.setCellType(Cell.KRUISJE);
        }
        else if (((RadioButton) typegroup2.getSelectedToggle()).getText().equals("Rondje")){
            controller.setCellType(Cell.RONDJE);
        }
        else if (((RadioButton) typegroup2.getSelectedToggle()).getText().equals("Zwart")){

            controller.setCellType(Cell.ZWART);
        }
        else {
            controller.setCellType(Cell.WIT);
        }

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void declineinvite(ActionEvent event){
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void challegenebuttonclicked(ActionEvent event){
        String command= "";
        if (selectedPlayer==null&&randomqueue==false){
            System.out.println("No selected player");
        }
        else {
            if (randomqueue == false) {
                command = "Challenge " + "\"" + selectedPlayer + "\""
                        + "\"" + ((RadioButton) gamegroup.getSelectedToggle()).getText() + "\"";
            } else {
                command = "subscribe " + ((RadioButton) gamegroup.getSelectedToggle()).getText();
            }

            if (((RadioButton) typegroup.getSelectedToggle()).getText().equals("Kruisje")){
                controller.setCellType(Cell.KRUISJE);
            }
            else if (((RadioButton) typegroup.getSelectedToggle()).getText().equals("Rondje")){
                controller.setCellType(Cell.RONDJE);
            }

            queue.offer(command);
        }
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }


    public void openchallenge(){
        randomqueue= false;
        final Stage popup= new Stage();
        BorderPane pane = new BorderPane();
        view.popupscreenController.active("ChallengeScreen",popup,pane);
        popup.setScene(new Scene(pane));
        popup.setTitle("Challenge");
        popup.show();
    }

    @FXML
    public void gamegroupaction(ActionEvent event){
        String word =((RadioButton)event.getSource()).getText();
        if (word.equals("Tic-tac-toe")){
            radiotype1.setText("Kruisje");
            radiotype2.setText("Rondje");
            checkboxai.setDisable(true);
        }
        else {
            checkboxai.setDisable(false);
            radiotype1.setText("Zwart");
            radiotype2.setText("Wit");
        }
    }

    public void subscribeopen(){
        randomqueue=true;
        final Stage popup= new Stage();
        BorderPane pane = new BorderPane();
        view.popupscreenController.active("ChallengeScreen",popup,pane);
        popup.setScene(new Scene(pane));
        popup.setTitle("Challenge");
        popup.show();
    }
}
