import java.awt.*;

public class Piece {

    int id;
    int health = 100;
    int x;
    int y;
    int z;
    int width;
    int height;
    int depth;

    public Piece(int id, int health, int x, int y, int z, int width, int height, int depth) {
        this.id = id;
        this.health = health;
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public void draw(Graphics g) {
        System.out.println("Tegner " + id + ": " + width+"," +height);
        g.setColor(Color.black);
        g.fillRect(x, y, width, height);
    }

    public void synchronizeData(int id, int health, int x, int y, int z, int width, int height, int depth) {
        if(this.id != id) { this.id = id; }
        if(this.health != health) { this.health = health; }
        if(this.x != x) { this.x = x; }
        if(this.y != y) { this.y = y; }
        if(this.z != z) { this.z = z; }
        if(this.width != width) { this.width = width; }
        if(this.height != height) { this.height = height; }
        if(this.depth != depth) { this.depth = depth; }
    }
}
