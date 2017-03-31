package Model.States;

import Model.Game;

/**
 * Created by mark on 29-3-2017.
 */
public class GameFinished implements State {
    private Game game;
    public GameFinished(Game game){
        this.game= game;
    }
}
