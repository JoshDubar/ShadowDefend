package Sprites.Towers;

import Sprites.Sprite;
import Sprites.Weapons.Explosive;
import State.Level;
import State.ShadowDefend;
import bagel.Input;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.ArrayList;
import java.util.Random;

public abstract class PassiveTower extends Sprite {

    // tracks whether the tower is flying horizontally or vertically
    private static final String HORIZONTAL = "h";
    private static final String VERTICAL = "v";
    // routes must alternate, first route is horizontal so the previous route would be vertical
    private static String prevRoute = VERTICAL;
    // increment the random number by (random number between 0 and 1 -> 1 and 2)
    private static final int RANDOM_INCREMENT = 1;
    private static final double NINETY_ROTATE = Math.PI/2;
    private static final double FULL_ROTATE = Math.PI;
    // the route the tower will be playing
    private String route;
    // helper to calculate a random number
    private Random random;
    // speed of the tower
    private int speed;
    // frequency of tower dropping explosives
    private double dropRate;
    // amount of time passed since the tower was instantiated
    private double timePassed;
    // all explosives dropped by the tower
    private ArrayList<Explosive> explosives;
    // checks whether the tower has finished it's path
    private boolean isFinished;
    // checks whether tower is still dropping projectiles
    private boolean stillDropping;
    /**
     * Creates a new PassiveTower
     *
     * @param point    the point where the tower is instantiated
     * @param imageSrc the image which will be rendered at the entity's point
     * @param speed    the speed that tower travels
     */
    public PassiveTower(Point point, String imageSrc, int speed) {
        super(point, imageSrc);
        this.speed = speed;
        this.random = new Random();
        // calculates a random number between 1 and 2
        this.dropRate = random.nextDouble() + RANDOM_INCREMENT;
        this.timePassed = 0;
        this.explosives = new ArrayList<>();
        this.isFinished = false;
        this.stillDropping = true;
        // determine route based on previous route
        route = (prevRoute.equals(HORIZONTAL)) ? VERTICAL : HORIZONTAL;
        prevRoute = route;
        // determine where the tower spawns and the direction it's facing based on the route
        if (route.equals(HORIZONTAL)) {
            move(new Vector2(-getCenter().x, 0));
            setAngle(NINETY_ROTATE);

        } else {
            move(new Vector2(0, -getCenter().y));
            setAngle(FULL_ROTATE);
        }
    }

    /**
     * update and render the location of the tower and it's associated explosives
     *
     * @param input the input provided by the user
     */
    public void update(Input input) {
        // keep dropping explosives while still dropping
        if (stillDropping) {
            timePassed += Level.getTimescale();
        }
        // drop an explosive when we reach the drop rate
        if (timePassed / Level.FPS >= dropRate) {
            explosives.add(new Explosive(getCenter()));
            dropRate = random.nextDouble() + RANDOM_INCREMENT;
            timePassed = 0;
        }
        // update the tower based on it's route
        if (route.equals(HORIZONTAL)) {
            horizontalUpdate();
        } else {
            verticalUpdate();
        }

        // update all explosives of the tower
        for (int i = explosives.size() - 1;i >= 0;i--) {
            Explosive e = explosives.get(i);
            e.update(input);
            if (e.isFinished()) {
                explosives.remove(i);
            }
        }
        super.update(input);
    }

    /**
     * update for if the tower is travelling horizontally

     */
    public void horizontalUpdate() {
        super.move(new Vector2((speed * Level.getTimescale()), 0));
        if (getCenter().x >= ShadowDefend.getWIDTH()) {
            stillDropping = false;
        }
        if (getCenter().x >= ShadowDefend.getWIDTH()&& explosives.size() == 0) {
            isFinished = true;
        }
    }
    /**
     * update for if the tower is travelling vertically
     */
    public void verticalUpdate() {
        super.move(new Vector2(0, speed * Level.getTimescale()));
        if (getCenter().y >= ShadowDefend.getHEIGHT()) {
            stillDropping = false;
        }
        if (getCenter().y >= ShadowDefend.getHEIGHT() && explosives.size() == 0) {
            isFinished = true;
        }
    }

    /**
     *
     * @return check if the tower has finished it's route
     */
    public boolean getIsFinished() {
        return isFinished;
    }

    /**
     * to reset the previous rate when a new level is started
     * @param route the route to be set
     */
    public static void setPrevRoute(String route) {
        prevRoute = route;
    }

    /**
     *
     * @return the vertical route of the tower
     */
    public static String getVertical() {
        return VERTICAL;
    }
}
