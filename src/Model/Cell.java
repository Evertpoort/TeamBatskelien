package Model;

/**
 * Created by mark on 31-3-2017.
 */
public class Cell {
    private int xpos;
    private int ypos;
    private String contains;

    public Cell(int x, int y, String contains){
        this.xpos=x;
        this.ypos=y;
        this.contains=contains;
    }
}
