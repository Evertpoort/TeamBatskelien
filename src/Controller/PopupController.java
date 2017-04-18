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
import java.util.concurrent.LinkedBlockingQueue;

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

    public void invitereceived(String playername,int challengenumber, String game) { // saves the challengenumber and show the invitescreen
        this.challengenumber = challengenumber;
        Platform.runLater(() -> {
            invitelabel.setText(playername + " has invited you to a game of " + game);
            final Stage popup = new Stage();
            BorderPane pane = new BorderPane();
            view.popupscreenController.active("GameInvite", popup, pane);
            popup.setScene(new Scene(pane));
            popup.setTitle("Challenge");
            popup.show();
        });
    }


    @FXML
    public void inviteaccept(ActionEvent event){
        queue.offer("challenge accept "+ challengenumber); //accept the challenge and sends it to the server
        if (invitelabel.getText().contains("Reversi")){
            setpreferance("Reversi");
        }
        else {
            setpreferance("Tic-tac-toe");
        }

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void declineinvite(ActionEvent event){ // closes the invite popup
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
        controller.setCellType(null);
    }

    @FXML
    public void challegenebuttonclicked(ActionEvent event){
        String command= "";
        if (controller.getPlayerCellType()!=null){
            controller.setCellType(null);
        }
        if (selectedPlayer==null&&randomqueue==false){
            System.out.println("No selected player");
        }
        else {
            String game =((RadioButton) gamegroup.getSelectedToggle()).getText(); // check if a  player is selected else random que
            if (randomqueue == false) {
                command = "Challenge " + "\"" + selectedPlayer + "\""
                        + "\"" + game  + "\"";
            } else {
                command = "subscribe " + game;
            }

            setpreferance(game);
            queue.offer(command);
        }
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }


    public void openchallenge(){
        randomqueue= false; // opens the subsribe screen
        final Stage popup= new Stage();
        BorderPane pane = new BorderPane();
        view.popupscreenController.active("ChallengeScreen",popup,pane);
        popup.setScene(new Scene(pane));
        popup.setTitle("Challenge");
        popup.show();
    }

    public void subscribeopen(){ // opens the challenge screen
        randomqueue=true;
        final Stage popup= new Stage();
        BorderPane pane = new BorderPane();
        view.popupscreenController.active("ChallengeScreen",popup,pane);
        popup.setScene(new Scene(pane));
        popup.setTitle("Challenge");
        popup.show();
    }


    public void setpreferance(String game){
        if (game.equals("Reversi")) {// checks game type and sets the preferred celltype
            if (controller.prefferedtype.equals("Kruisje/Zwart")){
                controller.setCellType(Cell.ZWART);
            }
            else{
                controller.setCellType(Cell.WIT);
            }
        }
        else {
            if (controller.prefferedtype.equals("Kruisje/Zwart")){
                controller.setCellType(Cell.KRUISJE);
            }
            else {
                controller.setCellType(Cell.RONDJE);
            }
            controller.setAI(false);
        }
    }
}
