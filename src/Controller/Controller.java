package Controller;

import Model.Model;
import Model.Cell;
import View.View;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.LinkedBlockingQueue;


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
    private LinkedBlockingQueue<String> queue1;

    @FXML
    ComboBox<String> combobox;
    @FXML
    private RadioButton radiotype2,radiotype1,radiogame1,radiogame2;
    @FXML
    private CheckBox checkboxai;
    @FXML
    private TextField login;
    @FXML
    private ToggleGroup gamegroup,typegroup;
    @FXML
    private TableView<Table> usertable;
    @FXML
    private TableColumn<Table,String> onlinecolumn;
    @FXML
    private Label invitelabel;
    @FXML
    private javafx.scene.canvas.Canvas canvas;

    final ObservableList<Table> data = FXCollections.observableArrayList(
    );
    private String selectedPlayer;
    private boolean randomqueue= false;
    private int challengenumber;
    private String playerName;
    private String opponentName;
    private Cell cellType;

    public Controller(View view,Model model){
        this.view= view;
        this.model=model;
        queue=model.returnInstance();
        queue1= model.returnInputinstance();
        Thread t1= new Thread(new InputHandler(this,model,view,queue1));
        t1.start();
    }

    public String getPlayerName() {
        return playerName;
    }

    public Cell getPlayerCellType() {
        return cellType;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    @FXML
    public void subscribebutton(ActionEvent event){
        randomqueue=true;
        final Stage popup= new Stage();
        BorderPane pane = new BorderPane();
        view.screenController.active("ChallengeScreen",popup,pane);
        popup.setScene(new Scene(pane));
        popup.setTitle("Challenge");
        popup.show();
    }

    @FXML
    public void onEnter(ActionEvent event){
        // do login
        String command= login.getText();
        queue.offer("Login "+ command);
        queue.offer("get playerlist");
        view.screenController.active("LobbyScreen");
        onlinecolumn.setCellValueFactory(new PropertyValueFactory<Table, String>("playername"));
        usertable.setItems(data);
        usertable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        usertable.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            String selected =data.get(newValue.intValue()).getPlayername();
            System.out.println(selected);
            if (selected!= null){
                selectedPlayer=data.get(newValue.intValue()).getPlayername();
            }
        });
    }

    @FXML
    public void loginClick(ActionEvent event){
        onEnter(event);
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

    @FXML
    public void challegenebuttonclicked(ActionEvent event){
        String command= "";
        if (selectedPlayer==null){
            System.out.println("No selected player");
        }
        else {
            if (randomqueue == false) {
                command = "Challenge " + "\"" + selectedPlayer + "\""
                        + "\"" + ((RadioButton) gamegroup.getSelectedToggle()).getText() + "\"";
            } else {
                command = "subscribe " + ((RadioButton) gamegroup.getSelectedToggle()).getText();
            }
            System.out.println(command);
            queue.offer(command);
        }
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    public void refresh(ActionEvent event){
        queue.offer("get playerlist");
    }
    @FXML
    public void openchallenge(ActionEvent event){
        randomqueue= false;
        final Stage popup= new Stage();
        BorderPane pane = new BorderPane();
        view.screenController.active("ChallengeScreen",popup,pane);
        popup.setScene(new Scene(pane));
        popup.setTitle("Challenge");
        popup.show();
    }

    public void updateplayerlist(String[] list){
        data.removeAll();
        for (String i: list){
            if (!i.equals("")){
                System.out.println(i);
                data.add(new Table(i));
            }
        }
    }
    @FXML
    public void giveupclicked(ActionEvent event){
        queue.offer("forfeit");
        view.screenController.active("LobbyScreen");
    }

    public void loadgame(){
        Platform.runLater(() -> {
            view.screenController.active("GameScreen");
                });
    }

    public void invitereceived(String playername,int challengenumber, String game){
        this.challengenumber=challengenumber;
        Platform.runLater(() -> {
        invitelabel.setText(playername +" has invited you to a game of " + game);
        final Stage popup= new Stage();
        BorderPane pane = new BorderPane();
        view.screenController.active("GameInvite",popup,pane);
        popup.setScene(new Scene(pane));
        popup.setTitle("Challenge");
        popup.show();
        });
    }

    @FXML
    public void inviteaccept(ActionEvent event){
        queue.offer("challenge accept "+ challengenumber);
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    public void declineinvite(ActionEvent event){
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void onupdate(){
        //view.drawcanvas(Canvas canvas)
    }

}
