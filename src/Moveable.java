/**
 * This class is a representation of a Moveable-object, which extends Piece.
 */
public class Moveable extends Piece {

    int weight;
    int speed;
    int acceleration;

    /**
     * The Moveable Constructor has a reference to the following attributes of a moveable-object, and call super
     *      method in order to get the attributes from the Piece class.
     * @param id
     * @param health
     * @param x
     * @param y
     * @param z
     * @param width
     * @param height
     * @param depth
     * @param weight
     * @param speed
     * @param acceleration
     */
    public Moveable(int id, int health, int x, int y, int z, int width, int height, int depth, int weight, int speed, int acceleration) {

        super(id, health, x, y, z, width, height, depth);

        this.weight = weight;
        this.speed = speed;
        this.acceleration = acceleration;

    }

    /**
     * This method dones the same, as the method in the Piece class called synchronizeData, but with extra
     *      parameters:
     * @param weight
     * @param speed
     * @param acceleration
     */
    public void synchronizeData(int id, int health, int x, int y, int z, int width, int height, int depth, int weight, int speed, int acceleration) {
        if(this.id != id) { this.id = id; }
        if(this.health != health) { this.health = health; }
        if(this.x != x) { this.x = x; }
        if(this.y != y) { this.y = y; }
        if(this.z != z) { this.z = z; }
        if(this.width != width) { this.width = width; }
        if(this.height != height) { this.height = height; }
        if(this.depth != depth) { this.depth = depth; }
        if(this.weight != weight) { this.weight = weight; }
        if(this.speed != speed) { this.speed = speed; }
        if(this.acceleration != acceleration) { this.acceleration = acceleration; }
    }
}
