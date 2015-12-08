/**
 * Created by Sebastian on 07/12/2015.
 */
public class Moveable extends Piece {

    int weight;
    int speed;
    int acceleration;

    public Moveable(int id, int health, int x, int y, int z, int width, int height, int depth, int weight, int speed, int acceleration) {

        //Send data krævet i piece
        super(id, health, x, y, z, width, height, depth);

        //gem moveable-specifikke informationer
        this.weight = weight;
        this.speed = speed;
        this.acceleration = acceleration;

    }

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
