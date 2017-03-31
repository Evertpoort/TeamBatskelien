package sample;

import Controller.Controller;
import Model.Model;
import View.View;
import com.sun.javafx.sg.prism.NGShape;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
    Stage stage;
    View view;
    Model model;
    Controller controller;
    @Override
    public void start(Stage primaryStage) throws Exception{
        this.stage=primaryStage;
        this.model= new Model();
        this.view= new View(stage,model);

        view.screenController.active("LoginScreen");
        primaryStage.setTitle("Game");
        primaryStage.show();

    }



    public static void main(String[] args) {
        launch(args);
    }
}
