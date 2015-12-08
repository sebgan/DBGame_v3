import java.util.Hashtable;

public class GameTranslator {
    private Database connection;
    private Board board;

    public GameTranslator(Database connection) {

        this.connection = connection;
    }

    public Hashtable getAllPieces() {
        //TODO lav et hashtable med alle pieces. Heriblandt players, moveable osv. og returner det derefter
        Hashtable allPieces = new Hashtable();

        Hashtable players = connection.getOnlyPlayers();
        Hashtable moveables = connection.getOnlyMoveable();
        Hashtable pieces = connection.getOnlyPieces();

        allPieces.put("players", players);
        allPieces.put("moveables", moveables);
        allPieces.put("pieces", pieces);

        return allPieces;
    }
}
