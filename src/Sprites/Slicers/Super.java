package Sprites.Slicers;


import bagel.util.Point;

import java.util.List;

/**
 *
 * super slicers are slightly slower than regular slicers but spawn 2 regular slicers on death
 *
 */
public class Super extends Slicer {

    // directory of the image of the superslicer
    private static final String IMAGE_FILE = "res/images/superslicer.png";
    // speed of the super slicer
    private static final double SPEED = 1.5;
    // health of the super slicer
    private static final double HEALTH = 1.0;
    // reward given by the super slicer
    private static final int REWARD = 15;
    // the penalty of 2 regular slicers = 4
    private static final int PENALTY = 4;
    // number of children spawned by the superslicer
    private static final int NUM_CHILDREN = 2;
    // the type of children that the super slicer spawns
    private static final String CHILD_TYPE = "slicer";

    /**
     * Creates a new Super slicer
     *
     * @param polyline The polyline that the slicer must traverse (must have at least 1 point)
     */
    public Super(List<Point> polyline, Point startingPoint, int targetPointIndex) {
        super(polyline, startingPoint, targetPointIndex, IMAGE_FILE, SPEED, HEALTH, REWARD, PENALTY, NUM_CHILDREN, CHILD_TYPE);
    }

}
