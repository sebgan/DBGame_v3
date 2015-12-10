import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

public class GameTranslator {
    private GameConnection connection;
    private Board board;

    String[][] piecesAttributes = {{"id", "health", "x", "y", "z", "width", "height", "depth"}, {"name"}};
    String[][] moveableAttributes = {{"speed", "acceleration", "weight"}, {}};
    String[][] playersAttributes = {{"yaw", "pitch", "roll"}, {}};

    public GameTranslator(GameConnection connection) {

        this.connection = connection;
    }


    public Hashtable getAllPieces() {
        //TODO lav et hashtable med alle pieces. Heriblandt players, moveable osv. og returner det derefter
        Hashtable allPieces = new Hashtable();

        String[][] allAttributesForPlayers = mergeArrays(mergeArrays(piecesAttributes, moveableAttributes), playersAttributes);
        Hashtable players = connection.getData(allAttributesForPlayers, "players");

        String[][] allAttributesForMoveables = mergeArrays(piecesAttributes, moveableAttributes);
        Hashtable moveables = connection.getData(allAttributesForMoveables, "moveables");

        String[][] allAttributesForPieces = piecesAttributes;
        Hashtable pieces = connection.getData(allAttributesForPieces, "pieces");

        allPieces.put("players", players);
        allPieces.put("moveables", moveables);
        allPieces.put("pieces", pieces);

        return allPieces;
    }

    public String[][] mergeArrays(String[][] array1, String[][] array2) {

        //Merge integers
        int integerLength = array1[0].length + array2[0].length;
        String[] integerArray = new String[integerLength];
        int integerArrayNum = 0;
        for(int i = 0; i < array1[0].length; i++) {
            integerArray[integerArrayNum] = array1[0][i];
            integerArrayNum++;
        }
        for(int i2 = 0; i2 < array2[0].length; i2++) {
            integerArray[integerArrayNum] = array2[0][i2];
            integerArrayNum++;
        }

        //Merge strings
        int stringLength = array1[1].length + array2[1].length;
        String[] stringArray = new String[stringLength];
        int stringArrayNum = 0;
        for(int i = 0; i < array1[1].length; i++) {
            stringArray[stringArrayNum] = array1[1][i];
            stringArrayNum++;
        }
        for(int i2 = 0; i2 < array2[1].length; i2++) {
            stringArray[stringArrayNum] = array2[1][i2];
            stringArrayNum++;
        }

        String[][] mergedArray = {integerArray, stringArray};
        return mergedArray;
    }

}
