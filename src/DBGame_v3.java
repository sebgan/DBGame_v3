/**
 * This class is the main class of all the other classes.
 * Therefore this class has to have references of all other classes.
 */
public class DBGame_v3 {

    GamePresentation presentation;
    GameTranslator translator;
    GameConnection connection;

    /**
     * DBGame constructor that creates a connection to a database by instancing a GameConnection-object with the
     *      host name, port, database name, username and password.
     * Then if the GameConnection-object is valid, then we instantiates Controller, UserInput, GameTranslator and
     *      NPC-objects.
     */
    public DBGame_v3() {
        //connection = new GameConnection("wits.ruc.dk", "3306", "gandsoe", "gandsoe", "oQevALOP");
        connection = new GameConnection("localhost", "3306", "version2", "root", "");

        if(connection.isValid) {

            Controller controller = new Controller(connection);
            UserInput userInput = new UserInput(controller);

            translator = new GameTranslator(connection);

            Behavior behavior = new Behavior(controller, "follow");
            NPC npc = new NPC(translator, controller, behavior, 2);

            presentation = new GamePresentation(translator, userInput, 800, 600);
            presentation.setNPC(npc);
            presentation.startRenderLoop();

        }
        else {
            System.out.println("Kunne ikke starte spillet da connection mangler.");
        }

    }

    public static void main(String[] args) {
        DBGame_v3 nyt_spil = new DBGame_v3();
    }

}