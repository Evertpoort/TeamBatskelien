package Main;

import Model.Model;
import View.View;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.InputStream;
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
        InputStream file = Main.class.getResourceAsStream("config.properties");
        if (file != null) {
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
        } else {
            System.out.println("Can't find config file config.properties");
            System.exit(1);
        }
        return prop;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
