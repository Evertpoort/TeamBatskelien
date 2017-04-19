package Main;

import Model.Model;
import View.View;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;
import java.util.Properties;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Properties properties = loadProperties();
        stage.setOnCloseRequest(event -> System.exit(0));
        Model model = new Model(properties);
        View view = new View(stage, model);
        view.screenController.active("LoginScreen");
        stage.setTitle("Game");
        stage.show();
    }

    private Properties loadProperties() {
        Properties prop = null;
        InputStream file = null;
        try {
            file = new FileInputStream("config.properties");
        } catch (FileNotFoundException e) {
            System.out.println("Can't find config file config.properties");
            System.exit(1);
        }
        try {
            prop = new Properties();
            prop.load(file);
        } catch (Exception e) {
            System.out.println("Can't load config file config.properties, " + e);
        } finally {
            try {
                file.close();
            } catch (IOException e) {
                System.out.println("Can't load config file config.properties, " + e);
            }
        }
        return prop;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
