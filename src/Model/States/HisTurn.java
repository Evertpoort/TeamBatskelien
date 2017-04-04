package Model.States;

import Model.Game;

/**
 * Created by mark on 28-3-2017.
 */
public class HisTurn implements State {
    private Game game;
    public HisTurn(Game game){
        this.game= game;
    }

    public void move(){
        System.out.println("It is not your turn so you can make a move.");
    }
}
