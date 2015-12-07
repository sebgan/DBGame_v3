import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;
import java.sql.*;


public class GameConnection {
    //deklaration af variable
    //private };


    //JDBC-felters
    Connection connection;

    //Opretter forbindelse til databasen:
    public GameConnection(String host, String port, String database, String username, String password) {
        //Brug klasse fra JDBC-driver (vores .jar-fil)
        try {
            Class.forName("com.mysql.jdbc.Driver");
            try {
                this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
                System.out.println("Forbindelse oprettet");
            }
            catch (SQLException e) {
                System.out.println("Kunne ikke oprette forbindelse til " + host + ":" + port + ".");
                System.out.println(String.valueOf(e)); //skriv fejlmeddelelse på næste linje.
            }

        }
        catch (ClassNotFoundException e) {
            System.out.println("JDBC driver blev ikke fundet: " + e);
        }

        System.out.println("Database blev oprettet");
    }


    //funktion der muliggør download og lagring af tabeldata, funktionen kræver en string, der specificerer, hvilken data, der skal hentes
    public ResultSet getPieceData(String table) {
        String SQL = "SELECT id, x, y, z, width, height, depth";
        String pieceSQL = " FROM pieces WHERE id NOT IN (SELECT id FROM moveable)";
        String moveableSQL = ", speed, acceleration, weight FROM pieces, moveable WHERE pieces.id = moveable.id AND pieces.id NOT IN (SELECT id FROM players)";
        String playerSQL = ", speed, acceleration, weight, name, roll, pitch, yaw FROM pieces, moveable, players WHERE id NOT IN (SELECT id FROM moveable) AND id NOT IN (SELECT id FROM pieces)";
        ResultSet tableData;

        int columnsNumber;

        try {
            Statement statement = connection.createStatement();
            //Afhængigt af hvor omfattende datatrækket er, foretages der en gradvis specificering af SQL-statementet.
            //Funktionen tjekker om der spørges til pieces, moveables eller players.
            switch (table){
                case "pieces":
                    SQL = SQL + pieceSQL;
                    break;
                case "moveables":
                    SQL = SQL + moveableSQL;
                    break;
                case "players":
                    SQL = SQL + playerSQL;
                    break;
                default:
                    throw new IllegalArgumentException("Værdien "+table+" er ugyldig. Ændr værdien til pieces, moveables eller players");
            }

            //Der laves et resultset ved navn tableData, hvor den hentede information bliver lagret.
            tableData = statement.executeQuery(SQL);
            return tableData;
        }
        catch (SQLException e) {
            System.out.println("moveable sql fejl: " + e);
            return null;
        }
    }

    //funktion der laver et resulset om til et hashtable
    public Hashtable resultSetToHashtable(ResultSet resultSetData, String table){
        //0-6 pieces, 7-9 moveables, resten players
        String[] pieceAttributes = {"id", "x", "y", "z", "width", "height", "depth", "speed", "acceleration", "weight", "roll", "pitch"};
        Hashtable data = new Hashtable();
        Hashtable dataRow = new Hashtable();
        int loops;

        switch (table){
            case "pieces":
                loops = 6;
                break;
            case "moveables":
                loops = 9;
                break;
            case "players":
                loops = pieceAttributes.length;
                break;
            default:
                throw new IllegalArgumentException("Værdien "+table+" er ugyldig. Ændr værdien til pieces, players eller moveables");
        }

        try {
            while (resultSetData.next()) {
                int id = resultSetData.getInt("id");
                dataRow.put("id", id);
                //for loop, der looper pieceAttributes igennem, således at hashtabellen kommer til at indeholde alle interger values fra resultsettet.
                for(int i = 0; i<loops; i++) {
                    dataRow.put(pieceAttributes[i], resultSetData.getInt(pieceAttributes[i]));
                }
                    //Hvis det er players, som resultsettet beskriver tilføjes der også et navn til række i hashtabellen.
                if(table.equals("players")) {
                    dataRow.put("name", resultSetData.getString("name"));
                }

                //Sætter datRow (datarækken) ind i hashtabellen data.
                data.put(id, dataRow);
            }
        }
        catch (SQLException e){
            System.out.println("sql fejl: " + e);
        }

        return data;
    }

    //Funktion der muliggør en download af et board ud fra en id
    public ResultSet getBoard(int boardId){
        String boardTable = "boards";
        String SQLQuery = "SELECT * FROM " + boardTable + " WHERE id = " + boardId;

        try {
            Statement statement = connection.createStatement();
            //SQL-sætning / Syntax
            //Lav et ResultSet
            try {
                ResultSet boardData = statement.executeQuery(SQLQuery);
                //Luk statement igen, fordi det er sikkert en god idé
                statement.close();
                //Returnér Hashtable data, som indeholder id, name, width osv.
                //System.out.println("Modtog board data fra DB: " + data);

                //TODO: Overvej om vi skal tjekke om data er et tomt Hashtable
                return boardData;
            }
            catch (SQLException e) {
                System.out.println("Fejl i udførelse af query: " + e);
            }
        }
        catch (SQLException e) {
            System.out.println("Fejl ved oprettelse af SQL-statement: " + e);
        }

        //Hvis ikke vi retunerer andet før det her, returnerer vi null
        return null;
    }

    public void printResultSet(ResultSet rs){
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            System.out.println("querying SELECT * FROM XXX");
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }
                System.out.println("");
            }
        }
        catch (SQLException e) {
            System.out.println("moveable sql fejl: " + e);
        }
    }
}


