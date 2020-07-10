package Sprites.Towers;

import bagel.util.Point;

/**
 *
 * A stronger tank than the basic tank,
 * has a larger attack radius and fire rate
 *
 */
public class SuperTank extends ActiveTower {

    // shooting radius of the super tank
    private static final int RADIUS = 150;
    // damage of the super tank's projectiles
    private static final int DAMAGE = 3;
    // directory of the super tank image
    private static final String IMAGESRC = "res/images/supertank.png";
    // cooldown for waiting between shots
    private static final double COOLDOWN = 500;
    // directory of the super tank's projectile image
    private static final String PROJECTILESRC = "res/images/supertank_projectile.png";

    /**
     * creates a new super tank
     *
     * @param location the location of the super tank on the map
     */
    public SuperTank(Point location) {
        super(location, RADIUS, DAMAGE, IMAGESRC, COOLDOWN, PROJECTILESRC);
    }
}
