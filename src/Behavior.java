import java.util.Enumeration;
import java.util.Hashtable;

/**
 * This class holds the behavior of the NPC
 */
public class Behavior {

    private final String behaviorType;
    private final Controller controller;
    private Hashtable allPlayers;
    private Hashtable ownPlayer;
    private Hashtable closestPlayer;
    private double euclideanDistance;

    public Behavior(Controller controller, String behaviorType) {
        this.controller = controller;
        this.behaviorType = behaviorType;
    }

    public void setOtherPlayers(Hashtable allPlayers) {
        this.allPlayers = allPlayers;
    }

    public String getMoveDirection() {
        //Undersøg hvilken retning vi skal rykke os ud fra npc's position (ownPlayer) og de andre spillere
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

        //Undersøg hvilke retning
        return direction;
    }

    public void setOwnPlayer(Hashtable ownPlayer) {
        this.ownPlayer = ownPlayer;
    }

    public String getBehaviorType() {
        return behaviorType;
    }

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

            double newDistance = getEuclideanDistance(x1, y1, x2, y2);
            //System.out.println(id + " Afstand mellem {" + x2 + "," + y2 + "} og {" + x1 + "," + y1 + "} = " + newDistance);

            if(newDistance <= oldDistance) {
                oldDistance = newDistance;
                player = thisPlayer;
            }

        }

        return player;
    }

    public double getEuclideanDistance(int p1, int p2, int q1, int q2) {

        double horizontalDistance = p1-q1;
        double verticalDistance = Math.pow((q2-p2), 2);
        //TODO z-akse

        double euclideanDistance = Math.sqrt( horizontalDistance + verticalDistance );

        return euclideanDistance;
    }
}
