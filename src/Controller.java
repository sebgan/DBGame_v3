/**
 * Created by jbo on 07-12-2015.
 */
public class Controller {
    private GameConnection connection;

    public Controller(GameConnection connection) {
        this.connection = connection;
    }

    /* Uses method from David which implements keyListener?? to call the
method moveUp when the key "W" is pressed. Argument players id
*/
    public void moveUp(int id){
        connection.updatePiece(id, "y", -1);
    }
    // "S" pressed
    public void moveDown(int id){
        connection.updatePiece(id, "y", +1);
    }
    // "A" pressed
    public void moveLeft(int id){
        connection.updatePiece(id, "x", -1);
    }
    //"D" pressed
    public void moveRight(int id){
        connection.updatePiece(id, "x", +1);
    }
}
