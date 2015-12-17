
public class DBGame_v3 {

    GamePresentation presentation;
    GameTranslator translator;
    GameConnection connection;

    public DBGame_v3() {
        //connection = new GameConnection("wits.ruc.dk", "3306", "gandsoe", "gandsoe", "oQevALOP");
        connection = new GameConnection("localhost", "3306", "version2", "root", "");

        if(connection.isValid) {

            Controller controller = new Controller(connection);
            David david = new David(controller);

            translator = new GameTranslator(connection);

            Behavior behavior = new Behavior(controller, "follow");
            NPC npc = new NPC(translator, controller, behavior, 2);

            presentation = new GamePresentation(translator, david, 800, 600);
            presentation.setNPC(npc);
            presentation.startRenderLoop();

        }
        else {
            System.out.println("Kunne ikke starte spillet da connection mangler.");
        }

    }

    public static void main(String[] args) {
        DBGame_v3 nyt_spil = new DBGame_v3(); //Kører denne klasses constructor
    }

}