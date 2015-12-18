import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * This class extends JPanel in order to create a class that can be used as a Panel on the Frame.
 * It's here the pieces that are drawn on a board and the pieces are synchronized.
 * This class overrides some of the method from the JPanel.
 */
public class Board extends JPanel {

    private final int canvasWidth;
    private final int canvasHeight;

    private final int tileWidth = 50;
    private final int tileHeight = 50;

    private Hashtable players = new Hashtable();
    private Hashtable moveables = new Hashtable();
    private Hashtable pieces = new Hashtable();
    private long fps;

    /**
     * Board Constructor has the Width and Height of the board on the panel and sets the background color
     *      to red.
     * @param canvasWidth
     * @param canvasHeight
     */
    public Board(int canvasWidth, int canvasHeight) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;

        this.setBackground(Color.red);
    }

    @Override
    /**
     * This method uses a Graphics-object as a parameter for other methods to draw the pieces on the board.
     * @param g
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawPlayers(g);
        drawMoveables(g);
        drawPieces(g);

        g.setColor(Color.GREEN);
        g.drawString("FPS: "+fps, 20, 20);
    }

    /**
     * This method makes a list with keys from our HashTable pieces that uses a type Enumeration called pieceKey.
     * As long as there are more elements in pieceKey: first find the id of the piece from a Piece variable.
     *      Take the Piece variable's x and y position and set it on the board with color yellow, and with a name
     *      assigned to it.
     * @param g
     */
    private void drawPieces(Graphics g) {

        Enumeration pieceKey = pieces.keys();

        while(pieceKey.hasMoreElements()) {

            int id = (int) pieceKey.nextElement();
            Piece pieceToBeDrawed = (Piece) pieces.get(id);
            int x = pieceToBeDrawed.x*tileWidth;
            int y = pieceToBeDrawed.y*tileHeight;

            g.setColor(Color.yellow);
            g.fillRect(x, y, tileWidth, tileHeight);
            g.setColor(Color.white);
            g.drawString(String.valueOf(pieceToBeDrawed.id), x+8, y+tileHeight+12);
        }

    }

    /**
     * The same implementation is done on this method as explained for method drawPieces().
     * However the drawMoveables color are black.
     * @param g
     */
    private void drawMoveables(Graphics g) {

        Enumeration moveableKey = moveables.keys();

        while(moveableKey.hasMoreElements()) {

            int id = (int) moveableKey.nextElement();
            Moveable moveableToBeDrawed = (Moveable) moveables.get(id);
            int x = moveableToBeDrawed.x*tileWidth;
            int y = moveableToBeDrawed.y*tileHeight;

            g.setColor(Color.black);
            g.fillRect(x, y, tileWidth, tileHeight);
            g.setColor(Color.white);
            g.drawString(String.valueOf(moveableToBeDrawed.id), x+8, y+tileHeight+12);

        }
    }

    /**
     * This method is implemented the same way as the method drawPieces().
     * However the drawPlayers color are blue.
     * @param g
     */
    private void drawPlayers(Graphics g) {

        Enumeration playerKey = players.keys();

        while(playerKey.hasMoreElements()) {

            int id = (int) playerKey.nextElement();
            Player playerToBeDrawed = (Player) players.get(id);
            int x = playerToBeDrawed.x*tileWidth;
            int y = playerToBeDrawed.y*tileHeight;

            g.setColor(Color.blue);
            g.fillRect(x, y, tileWidth, tileHeight);
            g.setColor(Color.white);
            g.drawString(playerToBeDrawed.name, x+8, y+tileHeight+12);

        }
    }

    public void updateObjects(double delta) {

    }

    /**
     * The next three methods are for setting the Dimension of the Panel to the width and height of the Board.
     * Therefore when the dimensions are changed, the width and height of the Board changes with it.
     * @return Dimension
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(canvasWidth, canvasHeight);
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    /**
     * In this method we spilt up the data from all Pieces into data from Players, Moveables and Pieces, and
     * call the synchronized method for them separately.
     * @param allPieces
     */
    public void synchronizeAllPieces(Hashtable allPieces) {

        synchronizePlayers((Hashtable) allPieces.get("players"));
        synchronizeMoveables((Hashtable) allPieces.get("moveables"));
        synchronizePieces((Hashtable) allPieces.get("pieces"));
    }

    /**
     * This method synchronizes the only the pieces, by first checking if the condition: that pieceData is not null.
     * Then make a list with keys from our HashTable pieces that uses a type Enumeration called pieceKey.
     * Therefore as long as more elements in pieceKey: first find the id from pieceKey nextElement and set into
     *      a HashTable called thisPiece, which finds the nextElement's id.
     * Then pull all the data from the HashTable thisPiece and set the data in, the declared statements that have
     *      the same attributes that of Pieces.
     * For synchronization, we have to check if the pieces already exist and therefore it's checked in condition:
     *      if there are two pieces contains that contain the same id key.
     *      If not then we create a new Piece-object and store in it a HashTable pieces.
     * @param pieceData
     */
    private void synchronizePieces(Hashtable pieceData) {

        if(pieceData != null) {

            Enumeration pieceKey = pieceData.keys();

            while (pieceKey.hasMoreElements()) {

                int id = (int) pieceKey.nextElement();

                Hashtable thisPiece = (Hashtable) pieceData.get(id);

                int health = (int) thisPiece.get("health");
                int x = (int) thisPiece.get("x");
                int y = (int) thisPiece.get("y");
                int z = (int) thisPiece.get("z");
                int width = (int) thisPiece.get("width");
                int height = (int) thisPiece.get("height");
                int depth = (int) thisPiece.get("depth");

                if (pieces.containsKey(id)) {
                    Piece p = (Piece) pieces.get(id);
                    p.synchronizeData(id, health, x, y, z, width, height, depth);
                } else {

                    Piece tempPiece = new Piece(id, health, x, y, z, width, height, depth);

                    pieces.put(id, tempPiece);
                }
            }
        }
        else {
            log("Fik tom piece hashtable");
        }
    }

    private void log(String s) {

    }

    /**
     * This method is implementated the same way as the method above called synchronizedPieces().
     * @param moveableData
     */
    private void synchronizeMoveables(Hashtable moveableData) {

        if(moveableData != null) {

            Enumeration moveableKey = moveableData.keys();

            while (moveableKey.hasMoreElements()) {

                int id = (int) moveableKey.nextElement();

                Hashtable thisMoveable = (Hashtable) moveableData.get(id);

                int health = (int) thisMoveable.get("health");
                int x = (int) thisMoveable.get("x");
                int y = (int) thisMoveable.get("y");
                int z = (int) thisMoveable.get("z");
                int width = (int) thisMoveable.get("width");
                int height = (int) thisMoveable.get("height");
                int depth = (int) thisMoveable.get("depth");
                int weight = (int) thisMoveable.get("weight");
                int speed = (int) thisMoveable.get("speed");
                int acceleration = (int) thisMoveable.get("acceleration");

                if (moveables.containsKey(id)) {
                    Moveable m = (Moveable) moveables.get(id);
                    m.synchronizeData(id, health, x, y, z, width, height, depth, weight, speed, acceleration);

                } else {

                    Moveable tempMoveable = new Moveable(id, health, x, y, z, width, height, depth, weight, speed, acceleration);

                    moveables.put(id, tempMoveable);
                }
            }
        }
        else {
            log("Fik tom moveable hashtable");
        }
    }

    /**
     * The method synchronizePlayers are implemented the same way as the method ynchronizedPieces().
     * @param playerData
     */
    private void synchronizePlayers(Hashtable playerData) {


        if(playerData != null) {

            Enumeration playerKey = playerData.keys();

            while (playerKey.hasMoreElements()) {

                int id = (int) playerKey.nextElement();

                Hashtable thisPlayer = (Hashtable) playerData.get(id);

                int health = (int) thisPlayer.get("health");
                int x = (int) thisPlayer.get("x");
                int y = (int) thisPlayer.get("y");
                int z = (int) thisPlayer.get("z");
                int width = (int) thisPlayer.get("width");
                int height = (int) thisPlayer.get("height");
                int depth = (int) thisPlayer.get("depth");
                int weight = (int) thisPlayer.get("weight");
                int speed = (int) thisPlayer.get("speed");
                int acceleration = (int) thisPlayer.get("acceleration");
                int roll = (int) thisPlayer.get("roll");
                int pitch = (int) thisPlayer.get("pitch");
                int yaw = (int) thisPlayer.get("yaw");
                String name = (String) thisPlayer.get("name");

                if (players.containsKey(id)) {
                    Player p = (Player) players.get(id);
                    p.synchronizeData(id, health, x, y, z, width, height, depth, weight, speed, acceleration, roll, pitch, yaw, name);
                } else {

                    Player tempPlayer = new Player(id, health, x, y, z, width, height, depth, weight, speed, acceleration, roll, pitch, yaw, name);

                    players.put(id, tempPlayer);
                }

            }
        }
        else {
            log("Fik en tom player hashtable");
        }

    }

    public void setFPS(long fps) {
        this.fps = fps;
    }
}
