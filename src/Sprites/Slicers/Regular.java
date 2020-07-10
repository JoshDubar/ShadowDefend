package Sprites.Slicers;

import bagel.util.Point;

import java.util.List;

/**
 *
 * regular slicers are the most basic type of slicer, they spawn 0 children on death
 *
 */
public class Regular extends Slicer {

    // directory of the image of the slicer
    private static final String IMAGE_FILE = "res/images/slicer.png";
    // speed of the slicer
    private static final double SPEED = 2.0;
    // health of the slicer
    private static final double HEALTH = 1.0;
    // reward given by the slicer
    private static final int REWARD = 2;
    // the penalty of the regular slicer
    private static final int PENALTY = 1;
    // number of children spawned by the slicer
    private static final int NUM_CHILDREN = 0;
    // the type of children that the slicer spawns
    private static final String CHILD_TYPE = null;

    /**
     * Creates a new Regular slicer
     *
     * @param polyline The polyline that the slicer must traverse (must have at least 1 point)
     */
    public Regular(List<Point> polyline, Point startingPoint, int targetPointIndex) {
        super(polyline, startingPoint, targetPointIndex, IMAGE_FILE, SPEED, HEALTH, REWARD, PENALTY, NUM_CHILDREN, CHILD_TYPE);
    }
}
