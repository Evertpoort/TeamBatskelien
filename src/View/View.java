package View;

import Controller.Controller;
import Model.*;
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

import java.io.InputStream;
import java.lang.Math;
import java.io.IOException;

public class View {
    public ScreenController screenController;
    public ScreenController popupscreenController;
    public BorderPane root;
    public Controller controller;
    public Model model;
    private GraphicsContext gc;
    private InnerShadow is;

    public View(Stage stage, Model model) {
        this.root = new BorderPane();
        this.controller = new Controller(this, model);
        this.screenController = new ScreenController(stage, root);
        this.popupscreenController = new ScreenController(stage,root);
        this.model = model;
        is = new InnerShadow();
        is.setOffsetX(2);
        is.setOffsetY(2);
        is.setColor(Color.DIMGREY);

        addScene(0,screenController,"LoginScreen", "/res/LoginScreen.fxml");
        addScene(0,screenController,"LobbyScreen", "/res/LobbyScreen.fxml");
        addScene(0,screenController,"GameScreen", "/res/GameScreen.fxml");

        addScene(1,popupscreenController,"ChallengeScreen", "/res/ChallengeScreen.fxml");
        addScene(1,popupscreenController,"GameInvite", "/res/GameInvite.fxml");
        stage.setScene(new Scene(root));
    }

    public void addScene(int identifier,ScreenController scr, String name, String path) {//creates an fxml loader and adds it to the screencontroller
        FXMLLoader loader = new FXMLLoader();
        if (identifier==0){
            loader.setController(controller);
        }
        else {
            loader.setController(controller.popcontr);
        }
        try {
            scr.add(name, loader.load(getClass().getResourceAsStream(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void drawcanvas(Canvas canvas, Cell[] list) { // draws for each item in the list
        gc = canvas.getGraphicsContext2D();
        for (int i = 0; i < list.length; i++) {
            draw(canvas,i,list);
        }
    }


    public void draw(Canvas canvas,int index, Cell[] list) {
        Cell cell = list[index];
        double divider= Math.sqrt(list.length);
        double xpos= index%divider;
        double ypos= Math.floor(index/divider);
        double offsetx= canvas.getWidth()/divider;
        double offsety= canvas.getHeight()/divider;
        double textheight= offsety/2;
        gc.setStroke(Color.DARKGREY);

        if (cell==Cell.EMPTY_VALID)  // makes the valid moves a different color then the others
            gc.setFill(Color.CYAN);
        else
            gc.setFill(Color.ALICEBLUE);
        gc.strokeRect(xpos*offsetx,ypos*offsety,offsetx,offsety);
        gc.fillRect(xpos*offsetx,ypos*offsety,offsetx,offsety);

        if (cell==Cell.KRUISJE||cell==Cell.RONDJE){ // fills the cells with a x or o
            gc.setFill(Color.BLACK);
            gc.setFontSmoothingType(FontSmoothingType.LCD);
            gc.setFont(Font.font("Helvetica", FontWeight.BOLD, textheight));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);
            String character = "";
            if (cell==Cell.KRUISJE){
                character="X";
            } else {
                character="O";
            }

            gc.fillText(character,xpos*offsetx+offsetx/2,ypos*offsety+offsety/2);
        }
        else if (cell==Cell.WIT||cell==Cell.ZWART){ // fills the cells with a black or white circle.
            gc.setFill(Color.BLACK);
            gc.strokeArc(xpos*offsetx+offsetx/2-textheight/2,ypos*offsety+offsety/2-textheight/2,textheight,textheight,0,360,ArcType.ROUND);
            if (cell==Cell.ZWART){
                gc.setFill(Color.BLACK);
            } else {
                gc.setFill(Color.WHITE);
            }

            gc.fillArc(xpos*offsetx+offsetx/2-textheight/2,ypos*offsety+offsety/2-textheight/2,textheight,textheight,0,360,ArcType.ROUND);
        }
    }

    public void drawstatus(String str,Canvas canvas){ //draws the current status of the game // will display win,lose or draw.
        gc= canvas.getGraphicsContext2D();
        gc.clearRect(0,0,canvas.getWidth(),canvas.getHeight());

        double textheight = canvas.getHeight()/3;
        if (str.equals("Win")){
            gc.setFill(Color.rgb(153,255,153));
        } else if (str.equals("Lose")){
            gc.setFill(Color.rgb(255,102,102));
        } else {
            gc.setFill(Color.ALICEBLUE);
        }
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
        gc.setFill(Color.BLACK);
        gc.setFontSmoothingType(FontSmoothingType.LCD);
        gc.setFont(Font.font("Helvetica", FontWeight.BOLD, textheight));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText(str,canvas.getWidth()/2,canvas.getHeight()/2);
    }
}