import java.awt.*;
import java.util.Hashtable;
/**
 * This class is a representation of a Player-object, which extends Moveable. Player class inheritances from Moveable,
 *      while Moveable inheritances from Piece.
 */
public class Player extends Moveable {

    int roll;
    int pitch;
    int yaw;
    public String name;

    /**
     * The Player Constructor has a reference to the following attributes of a player-object, and call super
     *      method in order to get the attributes from the moveable class.
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
     * @param roll
     * @param pitch
     * @param yaw
     * @param name
     */
    public Player(int id, int health, int x, int y, int z, int width, int height, int depth, int weight, int speed, int acceleration, int roll, int pitch, int yaw, String name) {

        super(id, health, x, y, z, width, height, depth, weight, speed, acceleration);

        this.roll = roll;
        this.pitch = pitch;
        this.yaw = yaw;
        this.name = name;

    }

    /**
     * This method dones the same, as the method in the Moveable class called synchronizeData, but with extra
     *      parameters:
     * @param roll
     * @param pitch
     * @param yaw
     * @param name
     */
    public void synchronizeData(int id, int health, int x, int y, int z, int width, int height, int depth, int weight, int speed, int acceleration, int roll, int pitch, int yaw, String name) {
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
        if(this.roll != roll) { this.roll = roll; }
        if(this.pitch != pitch) { this.pitch = pitch; }
        if(this.yaw != yaw) { this.yaw = yaw; }
        if(this.name != name) { this.name = name; }
    }
}
