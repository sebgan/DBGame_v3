/**
 * This class holds the behavior of the NPC
 */
public class Behavior {
    //What the class knows
    GamePresentation presentation;
    GameConnection connection;
    GameTranslator translator;
    NPC npc;

    //What the class does

    // method finding when the player is close to the NPC
    public void playerClose(){
        while(connection.getPlayers() == npc){
            moveToPlayer();
        }
    }

    //method to move NPC to player id and the players position
    public void moveToPlayer(){
        if()
            npc.move();
    }

    //method to check if the player is moving
    public void playerMoves(){
        if(){
            followPlayer();
        }
    }

    //method to follow the player when player moves
    //while player continues to move call npc.move
    public void followPlayer(){
        while()
    }
}
