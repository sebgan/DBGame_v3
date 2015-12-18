import java.util.Enumeration;
import java.util.Hashtable;

/**
 * This class holds the behavior of the NPC, that can check if which directions the player moves, then move with it.
 *      Furthermore the class gets which player is the closest to the NPC and the distance between both of them.
 */
public class Behavior {

    private final String behaviorType;
    private final Controller controller;
    private Hashtable allPlayers;
    private Hashtable ownPlayer;
    private Hashtable closestPlayer;
    private double euclideanDistance;

    /**
     * The Behavior Constructor has a reference to the Controller-object and the name of a specific behavior that a
     *      NPC can sign to, @param behaviorType.
     * @param controller
     * @param behaviorType
     */
    public Behavior(Controller controller, String behaviorType) {
        this.controller = controller;
        this.behaviorType = behaviorType;
    }


    public void setOtherPlayers(Hashtable allPlayers) {
        this.allPlayers = allPlayers;
    }

    /**
     * This method returns a string type of which direction the NPC should move, in order to move the same direction
     *      as the player.
     * This is done by checking i which direction the NPC should called ownPlayer and the other players that are
     *      closest to the NPC called closestPlayer.
     * Set a HashTable closestPlayer that is assigned to the method getClosestPlayer(). Declare the x and y position
     *      of the NPC and the closestPlayer.
     * Then we check the conditions of closestPlayer position and the position of NPC, in different cases. Then set the
     *      direction to a string type.
     * @return String
     */
    public String getMoveDirection() {

        Hashtable closestPlayer = getClosestPlayer();

        String direction = "error";

        int x1 = (int) ownPlayer.get("x");
        int y1 = (int) ownPlayer.get("y");

        int x2 = (int) closestPlayer.get("x");
        int y2 = (int) closestPlayer.get("y");

        if(x2 < x1) {
            direction = "LEFT";
        }
        else if(x2 > x1) {
            direction = "RIGHT";
        }
        else if(y2 < y1) {
            direction = "UP";
        }
        else if(y2 > y1) {
            direction = "DOWN";
        }
        else if(x2 == x1 && y2 == y1) {
            direction = "Samme position";
        }
        else {
            direction = "fejl";
        }

        return direction;
    }

    public void setOwnPlayer(Hashtable ownPlayer) {
        this.ownPlayer = ownPlayer;
    }

    public String getBehaviorType() {
        return behaviorType;
    }

    /**
     * This method gets the closest player from the NPC. It does this by declaring a new Hashtable of players, which
     *      are all the players and a HashTable thisPlayer that overrides player. This process is done, by declaring a
     *      list with keys from our HashTable allPlayers that uses a type Enumeration called playerKey.
     * As long as there are more element playerKey, then find the next elements id og playerKey and store it in
     *      a HashTable thisPlayer. If the new distance between NPC and player, set the the player that is closest to
     *      NPC to thisPlayer.
     * @return Hashtable
     */
    public Hashtable getClosestPlayer() {

        double oldDistance = 9999999;

        //1. Lav playerHashTable
        int x1 = (int) ownPlayer.get("x");
        int y1 = (int) ownPlayer.get("y");

        Hashtable player = new Hashtable(); //SKAL OVERSKRIVES

        Enumeration playerKey = allPlayers.keys();

        //Så længe der er flere elementer i playerKey
        while (playerKey.hasMoreElements()) {
            //Find først id
            int id = (int) playerKey.nextElement();

            //Find Hashtable for denne række
            Hashtable thisPlayer = (Hashtable) allPlayers.get(id);

            int x2 = (int) thisPlayer.get("x");
            int y2 = (int) thisPlayer.get("y");

            double newDistance = getDistance(x1, y1, x2, y2);
            //System.out.println(id + " Afstand mellem {" + x2 + "," + y2 + "} og {" + x1 + "," + y1 + "} = " + newDistance);

            if(newDistance <= oldDistance) {
                oldDistance = newDistance;
                player = thisPlayer;
            }

        }

        return player;
    }

    /**
     * This method returns the distance between to coordinates (p1,p2) and (q1,q2).
     * @param p1
     * @param p2
     * @param q1
     * @param q2
     * @return
     */
    public double getDistance(int p1, int p2, int q1, int q2) {

        double horizontalDistance = Math.pow((p1-q1), 2);
        double verticalDistance = Math.pow((q2-p2), 2);
        //TODO z-akse

        double distance = Math.sqrt( horizontalDistance + verticalDistance );

        return distance;
    }
}
