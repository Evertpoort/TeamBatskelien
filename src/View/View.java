package View;

import Controller.Controller;
import Model.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
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

//bu
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
     //   gc.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
        Cell cell = list[index];
        double divider= Math.sqrt(list.length);
        double xpos= index%divider;
        double ypos= Math.floor(index/divider);
        double offsetx= canvas.getWidth()/divider;
        double offsety= canvas.getHeight()/divider;
        double textheight= offsety/2;
        //System.out.println(cell);
        gc.setStroke(Color.DARKGREY);

        InnerShadow is = new InnerShadow();
        is.setOffsetX(2);
        is.setOffsetY(2);
        is.setColor(Color.DIMGREY);
        gc.setEffect(is);
        gc.setFill(Color.ALICEBLUE);
        gc.fillRect(xpos*offsetx,ypos*offsety,offsetx,offsety);
        gc.setEffect(null);


        gc.setFill(Color.BLACK);
        gc.setFontSmoothingType(FontSmoothingType.LCD);
        gc.setFont(Font.font("Helvetica", FontWeight.BOLD, textheight));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        String character = "";

        if (cell==Cell.WIT){

        }
        else if (cell==Cell.ZWART){

        }
        else if (cell==Cell.KRUISJE){
            character="X";
        }
        else if (cell==Cell.RONDJE){
        character="O";
        }
        else{

        }
        gc.fillText(character,xpos*offsetx+offsetx/2,ypos*offsety+offsety/2);
    }

    public void drawstatus(String str,Canvas canvas){
        gc= canvas.getGraphicsContext2D();
        gc.clearRect(0,0,canvas.getWidth(),canvas.getHeight());


        double textheight = canvas.getHeight()/3;
        // add nice back ground
        gc.setFill(Color.BLACK);
        gc.setFontSmoothingType(FontSmoothingType.LCD);
        gc.setFont(Font.font("Helvetica", FontWeight.BOLD, textheight));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText(str,canvas.getWidth()/2,canvas.getHeight()/2);
    }
}