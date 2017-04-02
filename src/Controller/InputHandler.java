package Controller;

import View.View;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by mark on 2-4-2017.
 */
public class InputHandler implements Runnable {
    Controller controller;
    View view;
    LinkedBlockingQueue<String> queue1;

    public InputHandler(Controller controller, View view, LinkedBlockingQueue<String> queue1){
        this.controller= controller;
        this.view=view;
        this.queue1=queue1;
    }
    @Override
    public void run() {


        while (true){
            try {
                String command=queue1.take();


                /*
    login <speler>          (String)        > Logs in the player
    logout | exit | quit | disconnect | bye > Logs out the player
            queue.offer("Logout");

    get gamelist                            > Returns a list of supported games
            queue.offer("get gamelist");

    get playerlist                          > Returns a list of players
            queue.offer("get playerlist");

    subscribe <speltype>    (Reversi / Tic-tac-toe) > Subscribes player to game type (case sensitive)
                queue.offer("subscribe " + command);

    move <zet>              (getal tussen 1-9)      > Executes move
                    queue.offer("move " + command);

    forfeit                                         > Player forfeits game
                        queue.offer("forfeit");

    challenge <player> <gametype>                   > Challenges <a> to play a game of <b>
    e.g. challenge "Eppo" "Tic-tac-toe" (case sensitive, requires "" around each argument)
                        queue.offer("challenge " + command);

    challenge accept <uitdaging nummer>             > Accepts challenge
                        queue.offer("challenge accept " + command);

 */

















            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
