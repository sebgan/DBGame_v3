import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/*
 * This class implements keylistener in order for the user to move.
 * Since Keylistener is an interface all it's method has to be overriden
 */
public class UserInput implements KeyListener {

    int id = 1; // id of the user
    Controller controller;

    //UserInput Constructor that has a reference to the Controller-object
    public UserInput(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    /**
     * This is the only method that we will be using from the KeyListener interface.
     * This method checks which key the user has pressed, this is done by defining an integer variable key,
     *      which gets the @param e method (getKeyCode).
     *  The key variable checks if the keyCode is "W", "S", "A", and "D" and calls the Controller-object's
     *      method (moveUp), which uses the @param id from user.
     */
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_W){
            controller.moveUp(id);
        }
        if(key == KeyEvent.VK_S){
            controller.moveDown(id);
        }
        if(key == KeyEvent.VK_A){
            controller.moveLeft(id);
        }
        if(key == KeyEvent.VK_D){
            controller.moveRight(id);
        }
        //System.out.println(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}