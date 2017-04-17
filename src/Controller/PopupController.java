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
        });
    }


    @FXML
    public void inviteaccept(ActionEvent event){
        queue.offer("challenge accept "+ challengenumber);
        if (invitelabel.getText().contains("Reversi")){
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

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void declineinvite(ActionEvent event){
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
            String game =((RadioButton) gamegroup.getSelectedToggle()).getText();
            if (randomqueue == false) {
                command = "Challenge " + "\"" + selectedPlayer + "\""
                        + "\"" + game  + "\"";
            } else {
                command = "subscribe " + game;
            }

            if (game.equals("Reversi")) {
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
