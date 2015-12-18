/**
 * The class controller has a reference to GameConnection in order to send the changed position.
 */
public class Controller {
    private GameConnection connection;

    //Controller constructor that has a references to the GameConnection-object.
    public Controller(GameConnection connection) {
        this.connection = connection;
    }

    /**
     * These methods have a param id which is the User id.
     * Each method calls a method updatePiece from GameConnection-object, where the arguments for the method are:
     * id from user, the position in which the user moves and the incrementation.
     * @param id
     */
    // Method is called when "W" is pressed
    public void moveUp(int id){
        connection.updatePiece(id, "y", -1);
    }
    // Method is called when "S" is pressed
    public void moveDown(int id){
        connection.updatePiece(id, "y", +1);
    }
    //Method is called when "A" is pressed
    public void moveLeft(int id){
        connection.updatePiece(id, "x", -1);
    }
    //Method is called when "D" is pressed
    public void moveRight(int id){
        connection.updatePiece(id, "x", +1);
    }
}
