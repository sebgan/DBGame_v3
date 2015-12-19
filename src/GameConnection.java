import java.util.Hashtable;
import java.sql.*;

/**
 * This class is where the connection to the Database is established, by sending SQL statements to the Database,
 *      and the information that is returned from the Database is set into a HashTable.
 */
public class GameConnection {

    //JDBC-felters
    Connection connection;
    public boolean isValid = false; // Boolean variable used from checking if the connection to the database is valid.

    /**
     * GameConnection Constructor is where the connection to the Database is constructed, by using the parameters:
     * @param host      the IP name from the User
     * @param port      a connection tunnel to the Database
     * @param database  the name for the data that is sorted on the database
     * @param username  the username for a User
     * @param password  the password for a User
     *
     *  Constructor has a reference to Connection-object that a DriverManager class needs in order to get a connection
     *        to the JDBC driver. By using the parameters explained above.
     */
    public GameConnection(String host, String port, String database, String username, String password) {
        //Usage of a JDBC-driver class that is download and used as a plugin as a .jar-file
        try {
            Class.forName("com.mysql.jdbc.Driver");
            try {
                this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
                System.out.println("Forbindelse oprettet");
                isValid = true;
            }
            catch (SQLException e) {
                System.out.println("Kunne ikke oprette forbindelse til " + host + ":" + port + ".");
                System.out.println(String.valueOf(e)); //Faild message
            }

        }
        catch (ClassNotFoundException e) {
            System.out.println("JDBC driver blev ikke fundet: " + e);
        }

        System.out.println("Database blev oprettet");
    }


    /**
     * This method returns a HashTable that stores the data in a data table for which data is a piece, moveable or a
     *      player. That is to be send to the database. This is done by using a switch statement.
     * For example if the data is a piece, then received information about the piece is declared in a ResultSet called
     *      tableData. To store the data received the ResultSet has to be changed into a HashTable.
     * @param table
     * @return HashTable
     */
    public Hashtable getPieceData(String table) {
        String SQL;
        ResultSet tableData;

        Hashtable tableDataHash = new Hashtable();

        try {
            Statement statement = connection.createStatement();
            switch (table){
                case "pieces":
                    SQL = "SELECT id, name, x, y, z, width, height, depth FROM pieces WHERE id " +
                            "NOT IN (SELECT id FROM moveable)";
                    break;
                case "moveables":
                    SQL = "SELECT pieces.id, name, x, y, z, width, height, depth, speed, acceleration, weight, " +
                            "health FROM pieces, moveable WHERE pieces.id = moveable.id AND pieces.id NOT IN " +
                            "(SELECT id FROM players)";
                    break;
                case "players":
                    SQL ="SELECT pieces.id, name, x, y, z, width, height, depth, speed, acceleration, weight," +
                            " health, roll, pitch, yaw FROM pieces, moveable, players WHERE moveable.id = pieces.id " +
                            "AND players.id = moveable.id";
                    break;
                default:
                    throw new IllegalArgumentException("Værdien "+table+" er ugyldig. Ændr værdien til pieces," +
                            " moveables eller players");
            }

            tableData = statement.executeQuery(SQL);

            tableDataHash = resultSetToHashtable(tableData, table);

            return tableDataHash;
        }
        catch (SQLException e) {
            System.out.println("moveable sql fejl: " + e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method changes a ResultSet into a HashTable, where the parameters table and a ResultSet are needed.
     * The @param table is check by a switch statement, by looking at the number of loops that is represented for
     *      pieces, moveables and player respectively. Furthermore looking a the 1D array pieceAttribute.
     *      Therefore 0-7 loops i pieces, 8-10 is moveables and the rest is players.
     * Then we check as long as there is data is in the @param resultSetData, loop through pieceAttributes, so that
     *      a HashTable can hold all the integer values from the resultSetData.
     * @param resultSetData a ResultSet
     * @param table     a data table that represents pieces, or moveables, or players
     * @return
     */
    public Hashtable resultSetToHashtable(ResultSet resultSetData, String table){

        String[] pieceAttributes = {"id", "x", "y", "z", "width", "height", "depth", "speed", "acceleration", "weight",
                "health", "roll", "pitch"};
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
                throw new IllegalArgumentException("Værdien "+table+" er ugyldig. Ændr værdien til pieces," +
                        " players eller moveables");
        }

        try {
            while (resultSetData.next()) {
                Hashtable dataRow = new Hashtable();
                int id = resultSetData.getInt("id");
                dataRow.put("id", id);

                for(int i = 0; i<loops; i++) {
                    System.out.println(pieceAttributes[i]);
                    dataRow.put(pieceAttributes[i], resultSetData.getInt(pieceAttributes[i]));
                }
                //!!!!!!!
                if(table.equals("players")){
                    //dataRow.put("name", resultSetData.getString("name"));
                }

                data.put(id, dataRow);
            }
        }
        catch (SQLException e){
            System.out.println("sql fejl: " + e);
        }

        return data;
    }

    /**
     * This method allows to download a board by it´s id.
     * @param boardId
     * @return
     */
    public ResultSet getBoard(int boardId) {
        String boardTable = "boards";
        String SQLQuery = "SELECT * FROM " + boardTable + " WHERE id = " + boardId;

        try {
            Statement statement = connection.createStatement();
            try {
                ResultSet boardData = statement.executeQuery(SQLQuery);
                statement.close();

                return boardData;
            }
            catch (SQLException e) {
                System.out.println("Fejl i udførelse af query: " + e);
            }
        }
        catch (SQLException e) {
            System.out.println("Fejl ved oprettelse af SQL-statement: " + e);
        }

        return null;
    }

    /**
     * This method jobs is to update the pieces (moveable, or player) to which direction the piece wants to move.
     * @param id    unique value of a piece (moveable, players)
     * @param var   which attribute the piece want to be changed
     * @param value the incrementing or decrementing of the @param var
     */
    public void updatePiece(int id, String var, int value){

        int currentValue;
        String selectSQL = "SELECT "+var+" FROM `pieces` WHERE id="+id+";";
        String insertSQL;
        ResultSet rsCurrentValue;
        try{
            Statement statement = connection.createStatement();
            rsCurrentValue=statement.executeQuery(selectSQL);
            while(rsCurrentValue.next())
            {
                currentValue=rsCurrentValue.getInt(var);
                currentValue = currentValue + value;
                insertSQL = "INSERT INTO actions (id,"+var+") values ("+id+","+currentValue+");";
                try{
                    Statement stmnt = connection.createStatement();
                    stmnt.executeUpdate(insertSQL);

                } catch (SQLException e) {
                    System.out.println("moveable sql fejl: " + e);
                }
            }

        } catch (SQLException e) {
            System.out.println("moveable sql fejl: " + e);
        }
    }


    /**
     * This code is used from:
     * @Title: Test source code
     * @Author: Untitled
     * @Date: 4th May 2015
     * @Availability: http://pastebin.com/C0RRzUb2
     */
    // This method is used to see the ResultSet received from the database.
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

    /**
     * This method gets specific data (piece, players and moveables) from the database.
     *      This is done by first checking if the SQL statement, that is used to send to the database is created.
     *      The SQL statement invokes the method getAttributes().
     * A HashTable allHashtableRows is created in order to get the store the players rows from the database into a
     *          HashTable. This is done by running through as long as there is data in the ResultSet that is received,
     *          put the data into allHashtableRows.
     * @param attributes
     * @param table
     * @return HashTable
     */
    public Hashtable getData(String[][] attributes, String table) {
        String pieceSql = "SELECT " + getAttributes(attributes, false) + " FROM pieces WHERE id " +
                "NOT IN (SELECT id FROM moveable);";
        String moveableSql = "SELECT " + getAttributes(attributes, true) + " FROM pieces, moveable " +
                "WHERE pieces.id = moveable.id AND pieces.id NOT IN (SELECT id FROM players);";
        String playerSql = "SELECT " + getAttributes(attributes, true) + " FROM pieces, moveable, players " +
                "WHERE moveable.id = pieces.id AND players.id = moveable.id;";
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
                throw new IllegalArgumentException("Værdien "+table+" er ugyldig. Ændr værdien til pieces," +
                        " players eller moveables");
        }

        try {

            Statement statement = connection.createStatement();
            try {

                ResultSet rows = statement.executeQuery(sql);
                Hashtable allHashtableRows = new Hashtable();
                while(rows.next()) {
                    Hashtable hashtableRow = new Hashtable();

                    for(int i = 0; i < attributes[0].length; i++) {
                        hashtableRow.put(attributes[0][i], rows.getInt(attributes[0][i]));
                    }

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

    /**
     *
     * @param attributes
     * @param piecesBeforeId
     * @return String
     */
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


    /**
     * The two last methods are used for debugging.
     * @param mergedArray
     */
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


