
/**
 * This class is a representation of a Piece-object
 */
public class Piece {

    int id;
    int health = 100;
    int x;
    int y;
    int z;
    int width;
    int height;
    int depth;

    /**
     * The Piece Constructor has a reference to the following attributes of a piece-object.
     * @param id    A unique value of a piece
     * @param health
     * @param x     Position of a piece-object on the x-axis
     * @param y     Position of a piece-object on the y-axis
     * @param z     Position of a piece-object on the z-axis
     * @param width corresponds to the size of a piece-object
     * @param height corresponds to the size of a piece-object
     * @param depth corresponds to the size of a piece-object
     */
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

    /**
     * This method is used in Board class, synchronizePieces(). This method checks the condition if the reference from
     *      the attributes for Piece object are not equivalent to the methods parameters. Then set the attributes that
     *      are referenced to the method parameter.
     * @param id
     * @param health
     * @param x
     * @param y
     * @param z
     * @param width
     * @param height
     * @param depth
     */
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
