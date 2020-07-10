package Sprites.Weapons;

import Sprites.Slicers.Slicer;
import Sprites.Sprite;
import State.Level;
import State.Wave;
import bagel.Drawing;
import bagel.Input;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Explosive extends Sprite {

    // directory of the explosive image
    private static final String IMAGE_SRC = "res/images/explosive.png";
    // time the explosive takes to explode
    private static final int DETONATION_TIME = 2;
    // area of effect of the explosion
    private static final double EFFECT_RADIUS = 200.0;
    // amount of damage inflicted by the explosive
    private static final int DAMAGE = 500;
    private static final double HALF = 0.5;
    // time passed since the explosive was dropped
    private int timePassed;
    // if the explosive has exploded or not
    private boolean isFinished;
    // the rectangle of the area of effect
    private Rectangle effectArea;

    /**
     * Creates a new Sprite (game entity)
     *
     * @param point    The starting point for the entity
     */
    public Explosive(Point point) {
        super(point, IMAGE_SRC);
        this.timePassed = 0;
        this.isFinished = false;
        this.effectArea = new Rectangle(getCenter().x - EFFECT_RADIUS * HALF, getCenter().y - EFFECT_RADIUS * HALF, EFFECT_RADIUS, EFFECT_RADIUS);

    }

    /**
     * updates and renders the state of the explosive
     *
     * @param input the input provided by the player
     */
    @Override
    public void update(Input input) {
        timePassed += Level.getTimescale();
        // after the detonation time, damage all slicers in the radius
        if (timePassed / Level.FPS >= DETONATION_TIME) {
            for (Slicer s: Wave.getSlicersOnScreen()) {
                if (s.getRect().intersects(effectArea)) {
                    s.decrementHealth(DAMAGE);
                }
            }
            isFinished = true;
        }
        super.update(input);
    }

    /**
     *
     * @return check if the explosive has finished
     */
    public boolean isFinished() {
        return isFinished;
    }
}
