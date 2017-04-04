package Model;

import java.util.List;
import java.lang.Math.*;

import static java.lang.Math.sqrt;

/**
 * Created by mark on 31-3-2017.
 */
public class TicTacToe extends Game {

    public TicTacToe(int size){
        board= boardCreator(size);

    }


    public Cell[][] boardCreator(int size){
        int arraysize=(int)(sqrt(size));
        Cell[][] mylist = new Cell[arraysize][arraysize];
        //for
        return mylist;
    }
}
