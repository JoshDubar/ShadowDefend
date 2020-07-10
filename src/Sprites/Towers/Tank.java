package Sprites.Towers;

import bagel.util.Point;

/**
 *
 * The most basic active tower object,
 * shoots projectiles at a slow rate, with a small radius
 *
 */
public class Tank extends ActiveTower {

    // shooting radius of the tank
    private static final int RADIUS = 100;
    // damage of the tank's projectiles
    private static final int DAMAGE = 1;
    // directory of the tank image
    private static final String IMAGESRC = "res/images/tank.png";
    // cooldown for waiting between shots
    private static final double COOLDOWN = 1000;
    // directory of the tank's projectile image
    private static final String PROJECTILESRC = "res/images/tank_projectile.png";


    /**
     * creates a new tank
     *
     * @param location the location of the tank on the map
     */
    public Tank(Point location) {
        super(location, RADIUS, DAMAGE, IMAGESRC, COOLDOWN, PROJECTILESRC);

    }
}
