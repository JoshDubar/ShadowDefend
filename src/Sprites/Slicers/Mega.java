package Sprites.Slicers;

import bagel.util.Point;
import java.util.List;

/**
 *
 * more durable than the super slicer, spawns 2 super slicers on death
 *
 */
public class Mega extends Slicer {

    // directory of the image of the megaslicer
    private static final String IMAGE_FILE = "res/images/megaslicer.png";
    // speed of the mega slicer
    private static final double SPEED = 1.5;
    // health of the mega slicer
    private static final double HEALTH = 2.0;
    // reward given by the mega slicer
    private static final int REWARD = 10;
    // the penalty of 2 mega slicers = 8
    private static final int PENALTY = 8;
    // number of children spawned by the megaslicer
    private static final int NUM_CHILDREN = 2;
    // the type of children that the mega slicer spawns
    private static final String CHILD_TYPE = "superslicer";
    /**
     * Creates a new Mega slicer
     *
     * @param polyline The polyline that the slicer must traverse (must have at least 1 point)
     */
    public Mega(List<Point> polyline, Point startingPoint, int targetPointIndex) {
        super(polyline, startingPoint, targetPointIndex, IMAGE_FILE, SPEED, HEALTH, REWARD, PENALTY, NUM_CHILDREN, CHILD_TYPE);
    }

}
