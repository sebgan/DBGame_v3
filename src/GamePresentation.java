import javax.swing.*;
import java.awt.*;

/**
 * This class represents the window (frame) in which the game is show. This is done by the class extending JFrame.
 * The class job is to have a game loop.
 */
public class GamePresentation extends JFrame {

    //Reference til translator, så vi kan modtage data? Skal eventuelt rykkes
    private GameTranslator translator;

    //Board
    Board board;
    private NPC npc = null;

    /**
     * GamePresentation Constructor has a reference to GameTranslator, UserInput-objects and the width and height
     *      of the frame. In the constructor is where the frame is set up and the keyListeners implemented.
     * @param translator
     * @param userInput
     * @param width
     * @param height
     */
    public GamePresentation(GameTranslator translator, UserInput userInput, int width, int height) {
        this.translator = translator;

        addKeyListener(userInput);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        board = new Board(width, height);
        add(board, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * The methods job is to control the game flow in the number of frame per second. This process is done by as long
     *      as the loop is running find when time when the loop started and find the time difference between the last
     *      and the current loop.
     *      When we have fund the time mellem last loop and current loop. Set the the last loop in the current loop.
     */
    public void startRenderLoop() {

        long lastLoopTime = System.nanoTime();
        final int TARGET_FPS = 20;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;

        boolean running = true;

        int frames = 0;

        while(running == true) {
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;

            board.setFPS(frames);

            lastLoopTime = now;

            double delta = updateLength / ((double) OPTIMAL_TIME);

            frames++;

            if(npc != null) {
                npc.nextAction();
            }

            board.synchronizeAllPieces(translator.getAllPieces());
            //board.updateObjects(delta);
            board.repaint();

            long time = Math.abs((lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000);


            try {

                Thread.sleep(time);
            }
            catch (Exception e) {
                System.out.println("Fejl i game loop: " + e);
            }

        }
    }

    public void setNPC(NPC npc) {
        this.npc = npc;
    }
}
