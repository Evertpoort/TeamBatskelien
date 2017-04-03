package View;

import Controller.Controller;
import Model.Model;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by mark on 30-3-2017.
 */
public class View {
    private Stage stage;
    public ScreenController screenController;
    public BorderPane root;
    public Controller controller;
    public Model model;

    public View(Stage stage,Model model){
        this.stage=stage;
        this.root= new BorderPane();
        this.controller = new Controller(this,model);
        this.screenController= new ScreenController(stage,root);
        this.model=model;


        addScene("LoginScreen","../res/LoginScreen.fxml");
        addScene("LobbyScreen","../res/LobbyScreen.fxml");
        addScene("ChallengeScreen","../res/ChallengeScreen.fxml");
        addScene("GameScreen", "../res/GameScreen.fxml");
        stage.setScene(new Scene(root));
    }

    public void addScene(String name, String path){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        loader.setController(controller);
       try {
           screenController.add(name, loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
