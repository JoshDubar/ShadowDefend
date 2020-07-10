package Sprites.Slicers;

import Sprites.Sprite;
import Sprites.Towers.ActiveTower;
import State.Level;
import State.Wave;
import bagel.Input;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * The logic behind slicers, which all other slicers inherit from
 */
public abstract class Slicer extends Sprite {

    // speed at which the slicer travels
    private double speed;
    // health of the slicer
    private double health;
    // reward given by the slicer
    private int reward;
    // live penalty taken by the slicer
    private int penalty;
    // path followed by the slicer
    private final List<Point> polyline;
    // point in the polyline the slicer is going towards
    private int targetPointIndex;
    // whether the slicer has finished it's path
    private boolean finished;
    // the number of children the slicer spawns
    private int numChildren;
    // the type of slicer the slice spawns
    private String childType;

    /**
     * Creates a new Slicer
     *
     * @param polyline The polyline that the slicer must traverse (must have at least 1 point)
     */

    public Slicer(List<Point> polyline, Point startingPoint, int targetPointIndex, String imageSrc, double speed, double health, int reward, int penalty, int numChildren, String childType) {
        super(startingPoint, imageSrc);
        this.speed = speed;
        this.health = health;
        this.reward = reward;
        this.penalty = penalty;
        this.polyline = polyline;
        this.targetPointIndex = targetPointIndex;
        this.finished = false;
        this.numChildren = numChildren;
        this.childType = childType;
    }

    /**
     * Updates the current state of the slicer. The slicer moves towards its next target point in
     * the polyline at its specified movement rate.
     *
     * @param input the input given by the player
     */
    @Override
    public void update(Input input) {
        if (finished) {
            return;
        }
        // Obtain where we currently are, and where we want to be
        Point currentPoint = getCenter();
        Point targetPoint = polyline.get(targetPointIndex);
        // Convert them to vectors to perform some very basic vector math
        Vector2 target = targetPoint.asVector();
        Vector2 current = currentPoint.asVector();
        Vector2 distance = target.sub(current);
        // Distance we are (in pixels) away from our target point
        double magnitude = distance.length();
        // Check if we are close to the target point
        if (magnitude < speed * Level.getTimescale()) {
            // Check if we have reached the end
            if (targetPointIndex == polyline.size() - 1) {
                finished = true;
                return;
            } else {
                // Make our focus the next point in the polyline
                targetPointIndex += 1;
            }
        }
        // Move towards the target point
        // We do this by getting a unit vector in the direction of our target, and multiplying it
        // by the speed of the slicer (accounting for the timescale)
        super.move(distance.normalised().mul(speed * Level.getTimescale()));
        // Update current rotation angle to face target point
        setAngle(Math.atan2(targetPoint.y - currentPoint.y, targetPoint.x - currentPoint.x));
        // check if the slicer is in the range of any towers
        for (ActiveTower tower: Level.getActiveTowers()) {
            if (getRect().intersects(tower.getRadiusBox()) && tower.getTarget() == null) {
                // tower locks onto slicer if it is in it's range
                tower.setTarget(this);
                break;
            }
        }
        super.update(input);
    }

    /**
     *
     * @return whether the slicer has finished
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     *
     * @return the slicer's health penalty
     */
    public int getPenalty() {
        return penalty;
    }

    /**
     *
     * @return the current health of the slicer
     */
    public double getHealth() {
        return health;
    }

    /**
     *
     * @return the reward given by the slicer
     */
    public int getReward() {
        return reward;
    }

    /**
     * decrements the health of the slicer
     *
     * @param damage the amount to decrement the slicer's health by
     */
    public void decrementHealth(double damage) {
        health -= damage;
    }

    /**
     *
     * @return get the polyline that this slicer follows
     */
    public List<Point> getPolyline() {
        return polyline;
    }

    /**
     * spawn the children of the slicer when it dies
     */
    public void spawnChildren() {
        for (int i = 0;i < numChildren;i++) {
            Wave.addSlicer(childType, getCenter(), targetPointIndex);
        }
    }
}
