package Model;

import Model.States.State;
import Model.States.*;

import java.util.List;

/**
 * Created by mark on 31-3-2017.
 */
public abstract class Game {

    public Cell[][] board;
    public State currentstate;
    public State YourTurn= new Yourturn(this);
    public State HistTurn= new HisTurn(this);
    public State GameFinished= new GameFinished(this);


    public void move(int x, int y){

    }
}
