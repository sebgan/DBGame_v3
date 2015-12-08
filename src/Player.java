import java.awt.*;
import java.util.Hashtable;

public class Player extends Moveable {

    int roll;
    int pitch;
    int yaw;
    public String name;

    public Player(int id, int health, int x, int y, int z, int width, int height, int depth, int weight, int speed, int acceleration, int roll, int pitch, int yaw, String name) {
        //Send data til Moveable og Piece
        super(id, health, x, y, z, width, height, depth, weight, speed, acceleration);

        //Gem player-specifikke informationer
        this.roll = roll;
        this.pitch = pitch;
        this.yaw = yaw;
        this.name = name;

    }

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
