import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

/**
 * This class represents the data from the database is separated into HashTables.
 * This is done by merging 2D arrays for each of the attributes of the different classes (Pieces, Moveables, Player)
 */
public class GameTranslator {
    private GameConnection connection;
    private Board board;

    String[][] piecesAttributes = {{"id", "health", "x", "y", "z", "width", "height", "depth"}, {"name"}};
    String[][] moveableAttributes = {{"speed", "acceleration", "weight"}, {}};
    String[][] playersAttributes = {{"yaw", "pitch", "roll"}, {}};

    /**
     * GameTranslator Constructor has a reference to GameConnection-object in order to invoke the methods in the
     *      GameConnection class.
     * @param connection
     */
    public GameTranslator(GameConnection connection) {

        this.connection = connection;
    }

    /**
     * This method returns a HashTable of all the pieces, it dones this by instantiating a HashTable-object allPieces.
     *      Then declaring a 2D array that calls the mergeArray method to merge the different global local variables:
     *      pieces, moveable,players Attributes.
     * Then declares a type HashTable for each of the local variables, by invoking GameConnection-object getData(), and
     *      the 2D array is a parameter for this method.
     * @return Hashtable
     */
    public Hashtable getAllPieces() {

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

    /**
     * This method returns a 2D array type string, that merges two arrays, by first running through a nested for loop,
     *      to merge the integers. Then after run through a second nested for loop, to merge the strings.
     * When the two nested for loops are completed, then the merge integers and merge strings are assigned to a
     *      2D array called mergeArray.
     * @param array1
     * @param array2
     * @return String [][]
     */
    public String[][] mergeArrays(String[][] array1, String[][] array2) {

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
