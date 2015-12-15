import java.util.Hashtable;
import java.sql.*;
//Det her er den nyeste
public class GameConnection {
    //deklaration af variable
    //private };

    //JDBC-felters
    private boolean valid = false;
    Connection connection;
    private ResultSet resultset;
    private Hashtable players;

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
    public Hashtable getPieceData(String table) {
        String SQL;
        /*
        String pieceSQL;
        String moveablesSQL;
        String playerSQL;
        */
        ResultSet tableData;

        Hashtable tableDataHash = new Hashtable();
        //resultSetToHashtable();

        try {
            Statement statement = connection.createStatement();
            //Afhængigt af hvor omfattende datatrækket er, foretages der en gradvis specificering af SQL-statementet.
            //Funktionen tjekker om der spørges til pieces, moveables eller players.
            switch (table){
                case "pieces":
                    SQL = "SELECT id, name, x, y, z, width, height, depth FROM pieces WHERE id NOT IN (SELECT id FROM moveable)";
                    break;
                case "moveables":
                    SQL = "SELECT pieces.id, name, x, y, z, width, height, depth, speed, acceleration, weight, health FROM pieces, moveable WHERE pieces.id = moveable.id AND pieces.id NOT IN (SELECT id FROM players)";
                    break;
                case "players":
                    SQL ="SELECT pieces.id, name, x, y, z, width, height, depth, speed, acceleration, weight, health, roll, pitch, yaw FROM pieces, moveable, players WHERE moveable.id = pieces.id AND players.id = moveable.id";
                    break;
                default:
                    throw new IllegalArgumentException("Værdien "+table+" er ugyldig. Ændr værdien til pieces, moveables eller players");
            }

            //Der laves et resultset ved navn tableData, hvor den hentede information bliver lagret.
            tableData = statement.executeQuery(SQL);
            //printResultSet(tableData);
            tableDataHash = resultSetToHashtable(tableData, table);
            //tableDataHash;

            return tableDataHash;
        }
        catch (SQLException e) {
            System.out.println("moveable sql fejl: " + e);
            e.printStackTrace();
            return null;
        }
    }

    //funktion der laver et resultset om til et hashtable, kræver tabellen og et resultset
    public Hashtable resultSetToHashtable(ResultSet resultSetData, String table){
        //printResultSet(resultSetData);
        //0-6 pieces, 7-10 moveables, resten players
        String[] pieceAttributes = {"id", "x", "y", "z", "width", "height", "depth", "speed", "acceleration", "weight", "health", "roll", "pitch"};
        Hashtable data = new Hashtable();
        int loops;
        System.out.println("resulSetToHashtable kører");
        switch (table){
            case "pieces":
                loops = 7;
                break;
            case "moveables":
                loops = 12;
                break;
            case "players":
                loops = pieceAttributes.length;
                break;
            default:
                throw new IllegalArgumentException("Værdien "+table+" er ugyldig. Ændr værdien til pieces, players eller moveables");
        }

        try {
            while (resultSetData.next()) {
                Hashtable dataRow = new Hashtable();
                int id = resultSetData.getInt("id");
                dataRow.put("id", id);
                //for loop, der looper pieceAttributes igennem, således at hashtabellen kommer til at indeholde alle interger values fra resultsettet.
                for(int i = 0; i<loops; i++) {
                    System.out.println(pieceAttributes[i]);
                    dataRow.put(pieceAttributes[i], resultSetData.getInt(pieceAttributes[i]));
                }

                //Hvis det er players, som resultsettet beskriver tilføjes der også et navn til række i hashtabellen.
                if(table.equals("players")){
                    //dataRow.put("name", resultSetData.getString("name"));
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
    public ResultSet getBoard(int boardId) {
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

    /*Uses move's method's in Controller to change the values of the variables
(x,y,z) when there is an action performed (keyPressed)
*/
    public void updatePiece(int id, String var, int value){
        //String SQL = "UPDATE pieces SET "+var+"="+var+"+"+value+" WHERE id=" +id;
        System.out.println("Forsøger at inserte!!!");
        int currentValue;
        String selectSQL = "SELECT "+var+" FROM `pieces` WHERE id="+id+";";
        String insertSQL;
        ResultSet rsCurrentValue;
        try{
            System.out.println("1");
            Statement statement = connection.createStatement();
            rsCurrentValue=statement.executeQuery(selectSQL);
            while(rsCurrentValue.next())
            {
                currentValue=rsCurrentValue.getInt(var);
                currentValue = currentValue + value;
                insertSQL = "INSERT INTO actions (id,"+var+") values ("+id+","+currentValue+");";
                try{
                    System.out.println("2");
                    Statement stmnt = connection.createStatement();
                    stmnt.executeUpdate(insertSQL);
                    //updateData = statement.executeQuery(SQL);
                    //updateDataHash = resultSetToHashtable(updateData, table); //??

                } catch (SQLException e) {
                    System.out.println("moveable sql fejl: " + e);
                }
            }

        } catch (SQLException e) {
            System.out.println("moveable sql fejl: " + e);
        }
    }


    //Funktion jeg har stjålet, der printer et resultset
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
            System.out.println("fejl i forbindelse med udprintningen af dataTable: " + e);
        }
    }

    public ResultSet getResultset() {
        return resultset;
    }

    public Hashtable getPlayers(String[][] attributes) {
        String sql = "SELECT " + getAttributes(attributes, true) + " FROM pieces, moveable, players WHERE moveable.id = pieces.id AND players.id = moveable.id";

        try {
            //Forsøg at opret statement
            Statement statement = connection.createStatement();
            try {
                //Hvis statement blev oprettet, så forsøg at udfør query
                ResultSet rows = statement.executeQuery(sql);
                Hashtable allHashtableRows = new Hashtable();
                while(rows.next()) {
                    Hashtable hashtableRow = new Hashtable();

                    //Smid Integers over i
                    for(int i = 0; i < attributes[0].length; i++) {
                        hashtableRow.put(attributes[0][i], rows.getInt(attributes[0][i]));
                    }

                    //Smid Strings over i
                    for(int i = 0; i < attributes[1].length; i++) {
                        hashtableRow.put(attributes[1][i], rows.getString(attributes[1][i]));
                    }

                    allHashtableRows.put(hashtableRow.get("id"), hashtableRow);
                }

                return allHashtableRows;
            }
            catch (SQLException e) {
                System.out.println("Fejl ved eksekvering af query.:");
                System.out.println("\t" + e);
            }
        }
        catch (SQLException e) {
            System.out.println("Fejl ved oprettelse af sql statement.");
            System.out.println("\t" + e);
        }

        return null;
    }

    public Hashtable getMoveables(String[][] attributes) {
        String sql = "SELECT " + getAttributes(attributes, true) + " FROM pieces, moveable, players WHERE pieces.id = moveable.id AND pieces.id NOT IN (SELECT id FROM players)";

        try {
            //Forsøg at opret statement
            Statement statement = connection.createStatement();
            try {
                //Hvis statement blev oprettet, så forsøg at udfør query
                ResultSet rows = statement.executeQuery(sql);
                Hashtable allHashtableRows = new Hashtable();
                while(rows.next()) {
                    Hashtable hashtableRow = new Hashtable();

                    //Smid Integers over i
                    for(int i = 0; i < attributes[0].length; i++) {
                        hashtableRow.put(attributes[0][i], rows.getInt(attributes[0][i]));
                    }

                    //Smid Strings over i
                    for(int i = 0; i < attributes[1].length; i++) {
                        hashtableRow.put(attributes[1][i], rows.getString(attributes[1][i]));
                    }

                    allHashtableRows.put(hashtableRow.get("id"), hashtableRow);
                }

                return allHashtableRows;
            }
            catch (SQLException e) {
                System.out.println("Fejl ved eksekvering af query:");
                System.out.println("\t" + e);
            }
        }
        catch (SQLException e) {
            System.out.println("Fejl ved oprettelse af sql statement.");
            System.out.println("\t" + e);
        }

        return null;
    }

    public Hashtable getPieces(String[][] attributes) {
        String sql = "SELECT " + getAttributes(attributes, false) + " FROM pieces WHERE id NOT IN (SELECT id FROM moveable)";

        try {
            //Forsøg at opret statement
            Statement statement = connection.createStatement();
            try {
                //Hvis statement blev oprettet, så forsøg at udfør query
                ResultSet rows = statement.executeQuery(sql);
                Hashtable allHashtableRows = new Hashtable();
                while(rows.next()) {
                    Hashtable hashtableRow = new Hashtable();

                    //Smid Integers over i
                    for(int i = 0; i < attributes[0].length; i++) {
                        hashtableRow.put(attributes[0][i], rows.getInt(attributes[0][i]));
                    }

                    //Smid Strings over i
                    for(int i = 0; i < attributes[1].length; i++) {
                        hashtableRow.put(attributes[1][i], rows.getString(attributes[1][i]));
                    }

                    allHashtableRows.put(hashtableRow.get("id"), hashtableRow);
                }

                return allHashtableRows;
            }
            catch (SQLException e) {
                System.out.println("Fejl ved eksekvering af query.:");
                System.out.println("\t" + e);
            }
        }
        catch (SQLException e) {
            System.out.println("Fejl ved oprettelse af sql statement.");
            System.out.println("\t" + e);
        }

        return null;
    }

    public Hashtable getData(String[][] attributes, String table) {
        String pieceSql = "SELECT " + getAttributes(attributes, false) + " FROM pieces WHERE id NOT IN (SELECT id FROM moveable);";
        String moveableSql = "SELECT " + getAttributes(attributes, true) + " FROM pieces, moveable WHERE pieces.id = moveable.id AND pieces.id NOT IN (SELECT id FROM players);";
        String playerSql = "SELECT " + getAttributes(attributes, true) + " FROM pieces, moveable, players WHERE moveable.id = pieces.id AND players.id = moveable.id;";
        String sql;

        switch (table){
            case "pieces":
                sql=pieceSql;
                break;
            case "moveables":
                sql=moveableSql;
                break;
            case "players":
                sql=playerSql;
                break;
            default:
                throw new IllegalArgumentException("Værdien "+table+" er ugyldig. Ændr værdien til pieces, players eller moveables");
        }

        try {
            //Forsøg at opret statement
            Statement statement = connection.createStatement();
            try {
                //Hvis statement blev oprettet, så forsøg at udfør query
                ResultSet rows = statement.executeQuery(sql);
                Hashtable allHashtableRows = new Hashtable();
                while(rows.next()) {
                    Hashtable hashtableRow = new Hashtable();

                    //Smid Integers over i
                    for(int i = 0; i < attributes[0].length; i++) {
                        hashtableRow.put(attributes[0][i], rows.getInt(attributes[0][i]));
                    }

                    //Smid Strings over i
                    for(int i = 0; i < attributes[1].length; i++) {
                        hashtableRow.put(attributes[1][i], rows.getString(attributes[1][i]));
                    }

                    allHashtableRows.put(hashtableRow.get("id"), hashtableRow);
                }

                return allHashtableRows;
            }
            catch (SQLException e) {
                System.out.println("Fejl ved eksekvering af query:");
                System.out.println("\t" + e);
            }
        }
        catch (SQLException e) {
            System.out.println("Fejl ved oprettelse af sql statement.");
            System.out.println("\t" + e);
        }

        return null;
    }


    private String getAttributes(String[][] attributes, boolean piecesBeforeId) {
        String attributesString = null;
        for(int i = 0; i < attributes.length; i++) {
            for (int j = 0; j < attributes[i].length; j++) {

                String value = attributes[i][j];
                if(piecesBeforeId == true && value.equals("id")) {
                    value = "pieces.id";
                }

                if(attributesString == null) {
                    attributesString = value;
                }
                else {
                    attributesString = attributesString + ", " + value;
                }
            }
        }
        return attributesString;
    }




    private void print2dArray(String[][] mergedArray) {
        for(int i = 0; i < mergedArray.length; i++) {
            for(int j = 0; j < mergedArray[i].length; j++) {
                System.out.println(i+","+j+": " + mergedArray[i][j]);
            }
        }
    }

    private void printArray(String[] integerArray) {
        for(int i = 0; i < integerArray.length; i++) {
            System.out.println(i + ": " + integerArray[i]);
        }
    }

}


