package View;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.util.HashMap;

//Source: http://stackoverflow.com/questions/37200845/how-to-switch-scenes-in-javafx
public class ScreenController {
    private HashMap<String,Pane> screenMap= new HashMap<>();
    private Stage stage;
    BorderPane root;
    public ScreenController(Stage stage,BorderPane root){
        this.stage = stage;
        this.root = root;
    }

    public void add(String name, Pane pane){
        screenMap.put(name,pane);
    }

    public void active(String name){ //make this the current active scene
        root.setCenter(screenMap.get(name));
        screenMap.get(name).autosize();
        stage.sizeToScene();
    }

    public void active(String name,Stage stage1,BorderPane pane1){
        pane1.setCenter(screenMap.get(name));
        stage1.sizeToScene();
    }
}
