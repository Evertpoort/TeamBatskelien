package View;

import Controller.Controller;
import Model.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.*;
import javafx.stage.Stage;

import java.lang.Math;

import java.io.IOException;

public class View {
    private Stage stage;
    public ScreenController screenController;
    public BorderPane root;
    public Controller controller;
    public Model model;
    private Cell[] list;
    private GraphicsContext gc;


    public View(Stage stage, Model model) {
        this.stage = stage;
        this.root = new BorderPane();
        this.controller = new Controller(this, model);
        this.screenController = new ScreenController(stage, root);
        this.model = model;


        addScene("LoginScreen", "../res/LoginScreen.fxml");
        addScene("LobbyScreen", "../res/LobbyScreen.fxml");
        addScene("ChallengeScreen", "../res/ChallengeScreen.fxml");
        addScene("GameScreen", "../res/GameScreen.fxml");
        addScene("GameInvite", "../res/GameInvite.fxml");
        stage.setScene(new Scene(root));
    }

    public void addScene(String name, String path) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        loader.setController(controller);
        try {
            screenController.add(name, loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void drawcanvas(Canvas canvas, Cell[] list) {
        gc = canvas.getGraphicsContext2D();
        for (int i = 0; i < list.length; i++) {
        draw(canvas,i,list);
        }
    }


    public void draw(Canvas canvas,int index, Cell[] list) {
        double divider= Math.sqrt(list.length);
        double xpos= index%divider;
        double ypos= index/divider;
        double offsetx= canvas.getWidth()/divider*(index%divider);
        double offsety= canvas.getHeight()/divider*(index/divider);

        //Rectangle r =new Rectangle(offsetx,offsety,)
    }
}