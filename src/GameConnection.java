import java.awt.*;
import java.util.Hashtable;
import java.sql.*;

public class GameConnection {
    //deklaration af variable
    private String[] pieceAttributes = {"id", "x", "y", "z", "width", "height", "depth", "speed", "acceleration", "weight", "roll", "pitch"};
    private String boardTable = "boards";

    //JDBC-felter
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

    //Lav funktioner der returnerer resultsets, der skal oversættes af Translator

    //getBoard - boardTable er en tabel, der indeholder width, height og depth
    public ResultSet getBoard(int boardId) {
        //Forsøg at oprette et statement
        try {
            Statement statement = connection.createStatement();
            //SQL-sætning / Syntax
            String SQLQuery = "SELECT * FROM " + boardTable + " WHERE id = " + boardId;
            //Lav et ResultSet
            try {
                ResultSet boardData = statement.executeQuery(SQLQuery);
                //Luk statement igen, fordi det er sikkert en god idé
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
        //Hvis ikke vi returnerer andet før det her, returnerer vi null
        return null;
    }


    public ResultSet getOnlyPieces() {
        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT id, x, y, z, width, height, depth FROM pieces WHERE id NOT IN (SELECT id FROM moveable)";
            ResultSet pieceData = statement.executeQuery(SQL);
            return pieceData;

        }
        catch (SQLException e) {
            System.out.println("moveable sql fejl: " + e);
            return null;
        }
    }
    public ResultSet getOnlyPlayers(){
        try{
            Statement statement = connection.createStatement();
            String SQL = "SELECT id, x, y, z, width, height, depth, weight, speed, acceleration, health, yaw, pitch, roll " +
                    "FROM pieces, moveable, players WHERE id NOT IN (SELECT id FROM moveable) AND id NOT IN (SELECT id FROM pieces)";
            ResultSet playerData = statement.executeQuery(SQL);
            return playerData;

    }catch (SQLException e){
            System.out.println("moveable sql fejl: " + e);
            return null;
        }
    }

    public ResultSet getOnlyMoveables(){
        try{
            Statement statement = connection.createStatement();
            String SQL = "SELECT id, x, y, z, width, height, depth, weight, speed, acceleration, " +
                    "FROM pieces, moveable, players WHERE id NOT IN (SELECT id FROM pieces)";
            ResultSet moveableData = statement.executeQuery(SQL);
            return moveableData;

        }catch (SQLException e){
            System.out.println("moveable sql fejl: " + e);
            return null;
        }
    }
}