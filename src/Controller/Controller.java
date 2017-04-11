package Controller;

import Model.Model;
import Model.Cell;
import View.View;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.util.concurrent.LinkedBlockingQueue;

public class Controller {
    public View view;
    private Model model;
    private LinkedBlockingQueue<String> queue;
    private LinkedBlockingQueue<String> queue1;

    @FXML
    ComboBox<String> combobox;
    @FXML
    private RadioButton radiotype1,radiotype2;

    @FXML
    private ToggleGroup typegroup1;

    @FXML
    private TextField login;

    @FXML
    private CheckBox checkboxai;
    @FXML
    private TableView<Table> usertable;
    @FXML
    private TableColumn<Table,String> onlinecolumn;
    @FXML
    private Label turnLabel, score1,score2;
    @FXML
    private javafx.scene.canvas.Canvas canvas;
    @FXML
    private Button gamebutton;

    final ObservableList<Table> data = FXCollections.observableArrayList(
    );
    private String playerName;
    private boolean AI;
    private Cell cellType;
    public PopupController popcontr;
    public String prefferedtype= "Kruisje/Zwart";

    public Controller(View view,Model model){
        this.view= view;
        this.model=model;
        queue=model.returnInstance();
        queue1= model.returnInputinstance();
        Thread t1= new Thread(new InputHandler(this,model,view,queue1));
        t1.start();
        popcontr=new PopupController(this,view,queue);
    }

    public String getPlayerName() {
        return playerName;
    }

    public Cell getPlayerCellType() {
        return cellType;
    }

    public void setCellType(Cell cellType){
        this.cellType= cellType;
    }


    public boolean getAI() {
        return AI;
    }

    public void setAI(boolean AI) {
        this.AI = AI;
    }

    @FXML
    public void subscribebutton(ActionEvent event){
        popcontr.subscribeopen();
    }

    @FXML
    public void onEnter(ActionEvent event){
        String name = login.getText();
        if (!name.equals("")) {
            playerName = name;
            queue.offer("Login " + name);
            queue.offer("get playerlist");
            view.screenController.active("LobbyScreen");
            onlinecolumn.setCellValueFactory(new PropertyValueFactory<Table, String>("playername"));
            usertable.setItems(data);
            usertable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            usertable.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.intValue() >= 0) {
                    String selected = data.get(newValue.intValue()).getPlayername();
                    if (selected != null) {
                        popcontr.setselectedPlayer(data.get(newValue.intValue()).getPlayername());
                    }
                }
            });
        }
    }

    @FXML
    public void loginClick(ActionEvent event){
        onEnter(event);
    }

    public void backToLogin(){
        Platform.runLater(() -> {
            view.screenController.active("LoginScreen");
        });
    }

    @FXML
    public void refresh(ActionEvent event){
        queue.offer("get playerlist");
    }
    @FXML
    public void openchallenge(ActionEvent event){
        popcontr.openchallenge();
    }
    @FXML
    public void radiotypeselected(ActionEvent event){
        String word=((RadioButton)event.getSource()).getText();
        if (!word.equals(prefferedtype))
        {prefferedtype=word;}
    }

    @FXML
    public void checkboxsel(ActionEvent event){
        if (((CheckBox)event.getSource()).isSelected()){
            setAI(true);
        }
        else {
            setAI(false);
        }
    }

    public void updateplayerlist(String[] list){
        data.clear();
        for (String i: list){
            if (!i.equals("")&&!i.equals(playerName)){
                //System.out.println(i);
                data.add(new Table(i));
            }
        }
    }

    @FXML
    public void giveupclicked(ActionEvent event){
        if (((Button)event.getSource()).getText().equals("Give up")){queue.offer("forfeit");}
        view.screenController.active("LobbyScreen");
        cellType=null;
    }

    public void loadgame(){
        Platform.runLater(() -> {
            view.screenController.active("GameScreen");
            gamebutton.setText("Give up");
            canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Cell[] list =model.getGame().getBoard();
                    int length= (int) Math.sqrt(list.length);
                    double x =canvas.getWidth()/length;
                    double y = canvas.getHeight()/length;
                    model.getGame().move((int)(event.getX()/x),(int) (event.getY()/y));
                }
            });
                });
    }

    public void invitereceived(String playername,int challengenumber, String game){
    popcontr.invitereceived(playername,challengenumber,game);
    }

    public void onupdate(){
        setPlayerTurn(model.getGame().getPlayerTurn());
        setScore(model.getGame().getPlayerScore(), model.getGame().getOpponentScore());
        view.drawcanvas(canvas,model.getGame().getBoard());
    }

    public void displaystatus(String str){
        view.drawstatus(str,canvas);
        Platform.runLater(() -> {
            gamebutton.setText("Close");
        });
        checkai();
    }

    public void checkai(){
        if (checkboxai.isSelected()){
            setAI(true);
        }
    }
    public void setPlayerTurn(boolean playerTurn){
        String turnname;
        if (playerTurn) {
            if (AI)
                turnname = "AI";
            else
                turnname = "You";
        } else {
            turnname ="Opponent";
        }
        Platform.runLater(() -> {
            turnLabel.setText(turnname);
        });
    }

    public void setScore(int playerScore, int opponentScore) {
        Platform.runLater(() -> {
            score1.setText("Your score: " + Integer.toString(playerScore));
            score2.setText("Opponent's score: " + Integer.toString(opponentScore));
        });
    }

    public void hidescorelabels(Boolean bool){
        score1.setVisible(bool);
        score2.setVisible(bool);
    }
}
