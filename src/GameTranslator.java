import java.util.Hashtable;

public class GameTranslator {
    private GameConnection connection;
    private Board board;

    public GameTranslator(GameConnection connection) {

        this.connection = connection;
    }

    public Hashtable getAllPieces() {
        //TODO lav et hashtable med alle pieces. Heriblandt players, moveable osv. og returner det derefter
        Hashtable allPieces = new Hashtable();

        Hashtable players = (Hashtable) connection.getOnlyPlayers();
        Hashtable moveables = (Hashtable) connection.getOnlyMoveables();
        Hashtable pieces = (Hashtable) connection.getOnlyPieces();

        allPieces.put("players", players);
        allPieces.put("moveables", moveables);
        allPieces.put("pieces", pieces);

        return allPieces;
    }
}
