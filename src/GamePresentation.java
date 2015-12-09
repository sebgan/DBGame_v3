import javax.swing.*;
import java.awt.*;

public class GamePresentation extends JFrame {

    //Reference til translator, så vi kan modtage data? Skal eventuelt rykkes
    private GameTranslator translator;

    //Board
    Board board;

    public GamePresentation(GameTranslator translator, David david, int width, int height) {
        this.translator = translator;

        //Tilføj KeyListener
        addKeyListener(david);

        //Sæt op JFrame
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        board = new Board(width, height);
        add(board, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void startRenderLoop() {

        long lastLoopTime = System.nanoTime();
        final int TARGET_FPS = 30;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;

        boolean running = true;

        while(running == true) {
            long now = System.nanoTime(); //Find tidspunkt for, hvornår dette loop's iteration er
            long updateLength = now - lastLoopTime; //Find tiden der er gået mellem vores sidste loop og det nuværende
            lastLoopTime = now; //Når vi har fundet tiden mellem sidste loop og nu, sætter vi sidste loop tid til nu.

            double delta = updateLength / ((double) OPTIMAL_TIME); //Tiden mellem nu og sidste frame divideret med den tid vi gerne vil have

            board.synchronizeAllPieces(translator.getAllPieces());

            //board.updateObjects(delta);
            board.repaint();

            try {
                Thread.sleep( (lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000 );
            }
            catch (InterruptedException e) {
                System.out.println("Fejl i game loop: " + e);
            }

        }

    }
}
