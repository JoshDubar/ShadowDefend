package Sprites.Towers;

import bagel.util.Point;

/**
 *
 * the only passive tower in the game,
 * flies through the map and drops explosives randomly, which detonate and do damage in an area
 *
 */
public class AirSupport extends PassiveTower {

    // directory of the airsupport image
    private static final String IMAGE_SRC = "res/images/airsupport.png";
    // flying speed of the airsupport
    private static final int SPEED = 3;


    /**
     * Creates a new AirSupport
     *
     * @param point The point where the airsupport is placed
     */
    public AirSupport(Point point) {
        super(point, IMAGE_SRC, SPEED);

    }
}
