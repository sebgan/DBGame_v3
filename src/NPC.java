import java.sql.Connection;
import java.util.Hashtable;

/**
 * This class represents a Non Playable Character, where the NPC can move and do an action, that is defined
 *      in the Behavior class.
 */
public class NPC {

    int id;
    Controller controller;
    GameTranslator translator;
    Behavior behavior;

    /**
     * NPC Constructor holds the references to Controller, GameTranslator, Behavior-objects and a specific id for
     *      each NPC.
     * @param translator
     * @param controller
     * @param behavior
     * @param id
     */
    public NPC(GameTranslator translator, Controller controller, Behavior behavior, int id) {
        this.translator = translator;
        this.controller = controller;
        this.behavior = behavior;
        this.id = id;

        System.out.println("Ny NPC oprettet, id=" + id);
    }

    /**
     * This method gets all the information from the HashTable players, which calls getPlayer() below.
     * Then we use Behavior-object to set NPC's own id to the object, by calling setOwnPlayer(), which
     *      gets all the players id and the removes the NPC id from the HashTable allPlayers.
     * Then we use Behavior-object to set the all the other player instead of the NPC id, that we remove to a method
     *      called setOtherPlayers().
     * To run the Behavior, an if statement checks if the Behavior-object that invokes behaviorType() is equivalent
     *      to the name of a specific behavior name called follow.
     *      Then assign a direction variable, that gets the direction in which the NPC can move, by the Behavior-object
     *      invokes getMoveDirection().
     *      Therefore set the direction variable as a parameter for the move method.
     */
    public void nextAction() {

        Hashtable allPlayers = getPlayers();

        behavior.setOwnPlayer((Hashtable) allPlayers.get(id));
        allPlayers.remove(id);

        behavior.setOtherPlayers(allPlayers);

        //3. kør behavior
        if(behavior.getBehaviorType() == "follow") {
            String direction = behavior.getMoveDirection();
            move(direction);
        }

    }

    /**
     * This method uses the direction parameter to check the conditions, if parameter is equals to the direction in
     *      which the NPC wants to move.
     *      If it's so, then a Controller-object invokes a method move the NPC in the direction it wants, which is
     *      controlled by the behavior.
     * @param direction
     */
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
    }

    /**
     * This method simple job is to return a HashTable of all the players. This is done by an GameTranslator-object
     *      invoking a method getAllPieces that are only players.
     * @return Hashtable
     */
    public Hashtable getPlayers() {
        return (Hashtable) translator.getAllPieces().get("players");
    }

}
