package Model.States;

import Model.Game;

/**
 * Created by mark on 28-3-2017.
 */
public class Yourturn implements State {
    private Game game;
    public Yourturn(Game game){
        this.game= game;
    }
}
