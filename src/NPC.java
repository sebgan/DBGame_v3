import java.sql.Connection;
import java.util.Hashtable;

/*
This class holds the Non playable characters
 */
public class NPC {

    int id;
    Controller controller;
    GameTranslator translator;
    Behavior behavior;

    public NPC(GameTranslator translator, Controller controller, Behavior behavior, int id) {
        this.translator = translator;
        this.controller = controller;
        this.behavior = behavior;
        this.id = id;

        System.out.println("Ny NPC oprettet, id=" + id);
    }

    public void nextAction() {
        //System.out.println("Foretager ny action");

        //hent all players og fjern vores eget id.
        Hashtable allPlayers = getPlayers();

        //Sæt npc'ens eget id til behavior
        behavior.setOwnPlayer((Hashtable) allPlayers.get(id));
        allPlayers.remove(id);

        //send alle players til behavior
        behavior.setOtherPlayers(allPlayers);

        //3. kør behavior (TODO adskil behaviorType og information der skal bruges til pågældende behaviorType
        if(behavior.getBehaviorType() == "follow") {
            String direction = behavior.getMoveDirection();
            move(direction);
        }

    }

    public void move(String direction){

        if(direction.equals("UP")) {
            controller.moveUp(id);
        }
        else if(direction.equals("DOWN")) {
            controller.moveDown(id);
        }
        else if(direction.equals("LEFT")) {
            controller.moveLeft(id);
        }
        else if(direction.equals("RIGHT")) {
            controller.moveRight(id);
        }
        else {
            System.out.println("fuck off");
        }

    }

    public Hashtable getPlayers() {
        return (Hashtable) translator.getAllPieces().get("players");
    }

}
