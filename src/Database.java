import java.sql.*;
import java.util.Hashtable;

public class Database {

    //JDBC-felter
    Connection connection;

    //Navne til tabeller i vores database
    private String boardTable = "boards";
    private String pieceTable = "pieces";
    private Hashtable onlyPlayers;
    private Hashtable onlyMoveable;
    private Hashtable onlyPieces;

    public Database(String host, String port, String database, String username, String password) {

        //Brug klasse fra JDBC-driver (vores .jar-fil)
        try {
            Class.forName("com.mysql.jdbc.Driver");
            try {
                this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
                log("Forbindelse oprettet");
            }
            catch (SQLException e) {
                log("Kunne ikke oprette forbindelse til " + host + ":" + port + ".");
                log(String.valueOf(e)); //skriv fejlmeddelelse på næste linje.
            }

        }
        catch (ClassNotFoundException e) {
            log("JDBC driver blev ikke fundet: " + e);
        }

        log("Database blev oprettet");
    }

    public Hashtable getBoard(int boardId) {

        //Forsøg at opret et statement
        try {
            Statement statement = connection.createStatement();

            //SQL-sætning / Syntax
            String SQLQuery = "SELECT * FROM " + boardTable + " WHERE id = " + boardId;

            //Lav et ResultSet
            try {
                ResultSet boardData = statement.executeQuery(SQLQuery);
                Hashtable data = new Hashtable();

                //Loop igennem alle rækkerne, der er gemt i boardData
                //Vi forventer at lykken KUN kører 1 gang.
                while(boardData.next()) {
                    data.put("id", boardData.getInt("id"));
                    data.put("name", boardData.getString("name"));
                    data.put("width", boardData.getInt("width"));
                    data.put("height", boardData.getInt("height"));
                    data.put("depth", boardData.getInt("depth"));
                }

                //Luk statement igen, fordi det er sikkert en god idé
                statement.close();

                //Returnér Hashtable data, som indeholder id, name, width osv.
                log("Modtog board data fra DB: " + data);

                //TODO: Overvej om vi skal tjekke om data er et tomt Hashtable
                return data;
            }
            catch (SQLException e) {
                log("Fejl i udførelse af query: " + e);
            }
        }
        catch (SQLException e) {
            log("Fejl ved oprettelse af SQL-statement: " + e);
        }

        //Hvis ikke vi retrunerer andet før det her, returnerer vi null
        return null;
    }

    private void log(String logMessage) {
        System.out.println(logMessage);
    }

    public Hashtable getOnlyPlayers() {
        Hashtable players = new Hashtable();

        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT pieces.id, health, x, y, z, width, height, depth, speed, acceleration, weight, name, roll, pitch, yaw FROM pieces, moveable, players WHERE moveable.id = pieces.id AND players.id = moveable.id";

            ResultSet playerData = statement.executeQuery(SQL);

            //change
            //Vi looper igennem playerData (som forhåbentligt indeholder  nogle rækker=
            while(playerData.next()) {

                //Vi gemmer denne række i et Hashtable, som vi kalder playerRow. Derefter smider vi playerRow ind i vores players
                Hashtable playerRow = new Hashtable();

                //TODO sæt id, x, y, z ind i array og loop igennem array.

                int id = playerData.getInt("id");
                playerRow.put("id", id);
                playerRow.put("health", playerData.getInt("health"));
                playerRow.put("x", playerData.getInt("x"));
                playerRow.put("y", playerData.getInt("y"));
                playerRow.put("z", playerData.getInt("z"));
                playerRow.put("width", playerData.getInt("width"));
                playerRow.put("height", playerData.getInt("height"));
                playerRow.put("depth", playerData.getInt("depth"));
                playerRow.put("speed", playerData.getInt("speed"));
                playerRow.put("acceleration", playerData.getInt("acceleration"));
                playerRow.put("weight", playerData.getInt("weight"));
                playerRow.put("name", playerData.getString("name"));
                playerRow.put("roll", playerData.getInt("roll"));
                playerRow.put("pitch", playerData.getInt("pitch"));
                playerRow.put("yaw", playerData.getInt("yaw"));

                players.put(id, playerRow);
            }

        }
        catch (SQLException e) {
            log("sql fejl: " + e);
        }

        return players;
    }

    public Hashtable getOnlyMoveable() {
        Hashtable moveables = new Hashtable();

        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT pieces.id, health, x, y, z, width, height, depth, speed, acceleration, weight FROM pieces, moveable WHERE pieces.id = moveable.id AND pieces.id NOT IN (SELECT id FROM players)";

            ResultSet moveableData = statement.executeQuery(SQL);

            //change
            //Vi looper igennem playerData (som forhåbentligt indeholder  nogle rækker=
            while(moveableData.next()) {

                //Vi gemmer denne række i et Hashtable, som vi kalder playerRow. Derefter smider vi playerRow ind i vores players
                Hashtable moveableRow = new Hashtable();

                //TODO sæt id, x, y, z ind i array og loop igennem array.

                int id = moveableData.getInt("id");
                moveableRow.put("id", id);
                moveableRow.put("health", moveableData.getInt("health"));
                moveableRow.put("x", moveableData.getInt("x"));
                moveableRow.put("y", moveableData.getInt("y"));
                moveableRow.put("z", moveableData.getInt("z"));
                moveableRow.put("width", moveableData.getInt("width"));
                moveableRow.put("height", moveableData.getInt("height"));
                moveableRow.put("depth", moveableData.getInt("depth"));
                moveableRow.put("speed", moveableData.getInt("speed"));
                moveableRow.put("acceleration", moveableData.getInt("acceleration"));
                moveableRow.put("weight", moveableData.getInt("weight"));

                moveables.put(id, moveableRow);
            }

        }
        catch (SQLException e) {
            log("moveable sql fejl: " + e);
        }

        return moveables;
    }

    public Hashtable getOnlyPieces() {
        Hashtable pieces = new Hashtable();

        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT id, health, x, y, z, width, height, depth FROM pieces WHERE id NOT IN (SELECT id FROM moveable)";

            ResultSet pieceData = statement.executeQuery(SQL);

            //change
            //Vi looper igennem playerData (som forhåbentligt indeholder  nogle rækker=
            while(pieceData.next()) {

                //Vi gemmer denne række i et Hashtable, som vi kalder playerRow. Derefter smider vi playerRow ind i vores players
                Hashtable pieceRow = new Hashtable();

                //TODO sæt id, x, y, z ind i array og loop igennem array.

                int id = pieceData.getInt("id");
                pieceRow.put("id", id);
                pieceRow.put("health", pieceData.getInt("health"));
                pieceRow.put("x", pieceData.getInt("x"));
                pieceRow.put("y", pieceData.getInt("y"));
                pieceRow.put("z", pieceData.getInt("z"));
                pieceRow.put("width", pieceData.getInt("width"));
                pieceRow.put("height", pieceData.getInt("height"));
                pieceRow.put("depth", pieceData.getInt("depth"));

                pieces.put(id, pieceRow);
            }

        }
        catch (SQLException e) {
            log("moveable sql fejl: " + e);
        }

        return pieces;
    }

}
