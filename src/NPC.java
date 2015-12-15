/*
This class holds the Non playable characters
 */
public class NPC {
    Controller control;
    GameConnection connection;
    GameTranslator translator;
    Behavior behavior;

    public NPC(Controller control, GameConnection connection, GameTranslator translator, Behavior behavior){
        this.control = control;
        this.connection = connection;
        this.translator = translator;
        this.behavior = behavior;
    }

    //id of NPC that can move around the board
    public void move(int id, String position){
        switch (position){
            case "UP": control.moveUp(id);
            case "DOWN": control.moveDown(id);
            case "LEFT": control.moveLeft(id);
            case "RIGHT": control.moveRight(id);
            default:
                System.out.println("NPC doesn't want to move");
        }

    }

    public void piecesAround(){
        translator.getAllPieces();
    }

}
