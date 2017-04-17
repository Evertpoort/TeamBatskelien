package Controller;

import javafx.beans.property.SimpleStringProperty;

public class Table {
    private final SimpleStringProperty playername;

    public Table(String name){
        this.playername = new SimpleStringProperty(name);
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
