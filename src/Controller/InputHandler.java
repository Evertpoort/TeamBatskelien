package Controller;

import View.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
otes bij server antwoorden:
Items tussen vierkante haken ('[' en ']') geven een lijst weer.
Items tussen accolades ('{' en '}') geven een map weer. Zoals bij alle maps, is de volgorde niet bepaald.

Notes bij client commando's:
Alle commando's zijn niet hoofdlettergevoelig.
Alle argumenten zijn niet hoofdlettergevoelig, m.u.v. namen van spelers en speltypes.


Lijst opvragen met ondersteunde spellen:
C: get gamelist
S: OK
S: SVR GAMELIST ["<speltype>", ...]
->Lijst met spellen ontvangen.

Lijst opvragen met verbonden spelers:
C: get playerlist
S: OK
S: SVR PLAYERLIST ["<speler>", ...]
->Lijst met spelers ontvangen.

Match aangeboden krijgen, bericht naar beide spelers:
S: SVR GAME MATCH {GAMTYPE: "<speltype>", PLAYERTOMOVE: "<naam speler1>", OPPONENT: "<naam tegenstander>"}
->Nu bezig met een match, de inschrijving voor een speltype is vervallen.

De beurt toegewezen krijgen tijdens match:
S: SVR GAME YOURTURN {TURNMESSAGE: "<bericht voor deze beurt>"}
->Nu mogelijkheid een zet te doen.

Resultaat van een zet ontvangen, bericht naar beide spelers:
S: SVR GAME MOVE {PLAYER: "<speler>", DETAILS: "<reactie spel op zet>", MOVE: "<zet>"}
->Er is een zet gedaan, dit bericht geeft aan wie deze gezet heeft, wat de reactie van het spel erop is

Resultaat van een match ontvangen, bericht naar beide spelers:
S: SVR GAME <speler resultaat> {PLAYERONESCORE: "<score speler1>", PLAYERTWOSCORE: "<score speler2>", COMMENT: "<commentaar op resultaat>"}
->De match is afgelopen, <speler resultaat> kan de waarde 'WIN', 'LOSS' of 'DRAW' bevatten.

Resultaat van een match die opgegeven is door een speler, bericht naar beide spelers:
S: SVR GAME <speler resultaat> {PLAYERONESCORE: "<score speler1>", PLAYERTWOSCORE: "<score speler2>", COMMENT: "Player forfeited match"}
->De match is afgelopen, <speler> heeft de match opgegeven.

// save score and go b
Resultaat van een match, speler heeft de verbinding verbroken:
S: SVR GAME <speler resultaat> {PLAYERONESCORE: "<score speler1>", PLAYERTWOSCORE: "<score speler2>", COMMENT: "Client disconnected"}
->De match is afgelopen, <speler> heeft de verbinding verbroken.

//handle in controller
Een uitdaging ontvangen:
S: SVR GAME CHALLENGE {CHALLENGER: "Sjors", GAMETYPE: "Guess Game", CHALLENGENUMBER: "1"}
->Nu mogelijkheid de uitdaging te accepteren.

//not sure if needs to be handled..
Resultaat van een uitdaging die is komen te vervallen:
S: SVR GAME CHALLENGE CANCELLED {CHALLENGENUMBER: "<uitdaging nummer>"}
->De uitdaging is vervallen. Mogelijke oorzaken: speler heeft een andere uitdaging gestart, speler is een match begonnen, speler heeft de verbinding verbroken.

---------------------------------------------
Overzicht van client-commando's:

login				Aanmelden als speler
logout | exit | quit | disconnect | bye
					Uitloggen en verbinding verbreken
get <gamelist | playerlist>
					Opvragen van gegevens
	gamelist		Opvragen van de lijst met ondersteunde speltypes
	playerlist		Opvragen van de lijst met aangemelde spelers
subscribe			Inschrijven voor een speltype
move				Een zet doen tijdens een match
challenge [accept]	Uitdagingen behandelen
	accept			Uitdaging accepteren
forfeit				De huidige match opgeven
help [commando]		Help weergeven
 */

                System.out.println(command);
            if (command.contains("YOURTURN")){
                //handle in the model
            }

            else if(command.contains("PLAYERLIST")){
                command= command.substring(command.indexOf("[")+1,command.indexOf("]")).replace(", ","");
                String[] list =command.split("\"");
                controller.updateplayerlist(list);
            }
           else if (command.contains("MATCH")){
                controller.loadgame();
            }
                //SVR GAME CHALLENGE {CHALLENGER: "wqegqwe", CHALLENGENUMBER: "0", GAMETYPE: "Tic-tac-toe"}
            // needs some programming
                else if (command.contains("CHALLENGENUMBER")&&!command.contains("CANCELLED")){
                Pattern p = Pattern.compile("\"([^\"]*)\"");
                Matcher m = p.matcher(command);
                List list = new ArrayList();
                while (m.find()) {
                    System.out.println(m.group(1));
                    list.add(m.group(1));
                }
               controller.invitereceived((String) list.get(0), Integer.parseInt((String) list.get(1)),(String) list.get(2));
            }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
