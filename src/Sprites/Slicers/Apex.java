package Sprites.Slicers;

import bagel.util.Point;
import java.util.List;

/**
 *
 * by far the most durable slicer, spawns 4 mega slicers on death but is very slow in exchange
 *
 */
public class Apex extends Slicer {
    // directory of the image of the apexslicer
    private static final String IMAGE_FILE = "res/images/apexslicer.png";
    // speed of the apex slicer
    private static final double SPEED = 1.0;
    // health of the apex slicer
    private static final double HEALTH = 25.0;
    // reward given by the apex slicer
    private static final int REWARD = 150;
    // the penalty of 4 apex slicers = 32
    private static final int PENALTY = 32;
    // number of children spawned by the apexslicer
    private static final int NUM_CHILDREN = 4;
    // the type of children that the apex slicer spawns
    private static final String CHILD_TYPE = "megaslicer";
    /**
     * Creates a new Apex slicer
     *
     * @param polyline The polyline that the slicer must traverse (must have at least 1 point)
     */
    public Apex(List<Point> polyline, Point startingPoint, int targetPointIndex) {
        super(polyline, startingPoint, targetPointIndex, IMAGE_FILE, SPEED, HEALTH, REWARD, PENALTY, NUM_CHILDREN, CHILD_TYPE);
    }

}
