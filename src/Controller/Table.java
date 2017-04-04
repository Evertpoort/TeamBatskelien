package Controller;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by mark on 2-4-2017.
 */
public class Table {
    private final SimpleStringProperty playername;

    public Table(String name){
        this.playername= new SimpleStringProperty(name);
    }

    public String getPlayername() {
        return playername.get();
    }

    public SimpleStringProperty playernameProperty() {
        return playername;
    }

    public void setPlayername(String playername) {
        this.playername.set(playername);
    }
}
