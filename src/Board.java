import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;
import java.util.Hashtable;

public class Board extends JPanel {

    private final int canvasWidth;
    private final int canvasHeight;

    private final int tileWidth = 50;
    private final int tileHeight = 50;

    private Hashtable players = new Hashtable();
    private Hashtable moveables = new Hashtable();
    private Hashtable pieces = new Hashtable();

    public Board(int canvasWidth, int canvasHeight) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;

        this.setBackground(Color.red);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawPlayers(g);
        drawMoveables(g);
        drawPieces(g);
    }

    private void drawPieces(Graphics g) {
        //Lav liste med keys i vores Hashtable
        Enumeration pieceKey = pieces.keys();

        //Så længe der er flere elementer i pieceKey
        while(pieceKey.hasMoreElements()) {
            //Find først id
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

    private void drawMoveables(Graphics g) {
        //Lav liste med keys i vores Hashtable
        Enumeration moveableKey = moveables.keys();

        //Så længe der er flere elementer i moveableKey
        while(moveableKey.hasMoreElements()) {
            //Find først id
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

    private void drawPlayers(Graphics g) {
        //Lav liste med keys i vores Hashtable
        Enumeration playerKey = players.keys();

        //Så længe der er flere elementer i playerKey
        while(playerKey.hasMoreElements()) {
            //Find først id
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

    public void synchronizeAllPieces(Hashtable allPieces) {
        //Split data op til kun player hashtablet
        synchronizePlayers((Hashtable) allPieces.get("players"));
        synchronizeMoveables((Hashtable) allPieces.get("moveables"));
        synchronizePieces((Hashtable) allPieces.get("pieces"));

    }

    private void synchronizePieces(Hashtable pieceData) {

        //Lav liste med keys i vores Hashtable
        Enumeration pieceKey = pieceData.keys();

        //Så længe der er flere elementer i playerKey
        while(pieceKey.hasMoreElements()) {
            //Find først id
            int id = (int) pieceKey.nextElement();

            //Find hashtable med denne id
            Hashtable thisPiece = (Hashtable) pieceData.get(id);

            //Hiv data ud og lav det til de rigtige typer
            int health = (int) thisPiece.get("health");
            int x = (int) thisPiece.get("x");
            int y = (int) thisPiece.get("y");
            int z = (int) thisPiece.get("z");
            int width = (int) thisPiece.get("width");
            int height = (int) thisPiece.get("height");
            int depth = (int) thisPiece.get("depth");

            //Vi skal derefte tjekke om spilleren findes i forvejen
            if(pieces.containsKey(id)) {
                Piece p = (Piece) pieces.get(id);
                p.synchronizeData(id, health, x, y, z, width, height, depth);
            }
            else {
                //Spilleren findes ikke, vi opretter derfor
                Piece tempPiece = new Piece(id, health, x, y, z, width, height, depth);
                //Nu gemmer vi vores nye Player i vores players hashtable, så vi kun opdaterer dette objekt næste gang
                pieces.put(id, tempPiece);
            }
        }
    }

    private void synchronizeMoveables(Hashtable moveableData) {

        //Lav liste med keys i vores Hashtable
        Enumeration moveableKey = moveableData.keys();

        //Så længe der er flere elementer i playerKey
        while(moveableKey.hasMoreElements()) {
            //Find først id
            int id = (int) moveableKey.nextElement();

            //Find hashtable med denne id
            Hashtable thisMoveable = (Hashtable) moveableData.get(id);

            //Hiv data ud og lav det til de rigtige typer
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

            //Vi skal derefte tjekke om spilleren findes i forvejen
            if(moveables.containsKey(id)) {
                Moveable m = (Moveable) moveables.get(id);
                m.synchronizeData(id, health, x, y, z, width, height, depth, weight, speed, acceleration);

            }
            else {
                //Spilleren findes ikke, vi opretter derfor
                Moveable tempMoveable = new Moveable(id, health, x, y, z, width, height, depth, weight, speed, acceleration);
                //Nu gemmer vi vores nye Player i vores players hashtable, så vi kun opdaterer dette objekt næste gang
                moveables.put(id, tempMoveable);
            }
        }
    }

    private void synchronizePlayers(Hashtable playerData) {
        //Lav liste med keys i vores Hashtable
        Enumeration playerKey = playerData.keys();

        //Så længe der er flere elementer i playerKey
        while(playerKey.hasMoreElements()) {
            //Find først id
            int id = (int) playerKey.nextElement();

            //Find hashtable med denne id
            Hashtable thisPlayer = (Hashtable) playerData.get(id);

            //Hiv data ud og lav det til de rigtige typer
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


            //Vi skal derefte tjekke om spilleren findes i forvejen
            if(players.containsKey(id)) {
                Player p = (Player) players.get(id);
                p.synchronizeData(id, health, x, y, z, width, height, depth, weight, speed, acceleration, roll, pitch, yaw, name);
            }
            else {
                //Spilleren findes ikke, vi opretter derfor
                Player tempPlayer = new Player(id, health, x, y, z, width, height, depth, weight, speed, acceleration, roll, pitch, yaw, name);
                //Nu gemmer vi vores nye Player i vores players hashtable, så vi kun opdaterer dette objekt næste gang
                players.put(id, tempPlayer);
            }
        }

    }
}
