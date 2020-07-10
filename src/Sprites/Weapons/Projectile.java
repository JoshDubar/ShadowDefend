package Sprites.Weapons;

import Sprites.Slicers.Slicer;
import Sprites.Sprite;
import Sprites.Towers.ActiveTower;
import State.Level;
import bagel.Input;
import bagel.util.Point;
import bagel.util.Vector2;

/**
 *
 * the projectile that ActiveTowers shoot
 */
public class Projectile extends Sprite {

    // the speed of the projectile
    private static final int SPEED = 10;
    // the tower that shot the projectile
    private ActiveTower tower;
    // the target slicer of the projectile
    private Slicer targetSlicer;
    // whether the projectile has reached it's target
    private boolean finished;

    /**
     * creates a projectile
     *
     * @param tower tower that shot the projectile
     * @param target target of the projectile
     * @param imageSrc image of the projectile
     */
    public Projectile(ActiveTower tower, Slicer target, String imageSrc) {
        super(tower.getCenter(), imageSrc);
        this.tower = tower;
        this.targetSlicer = target;
        this.finished = false;
    }

    /**
     * updates and renders the position of the projectile
     *
     * @param input the input that the use has given
     */
    @Override
    public void update(Input input) {
        // get the location of the projectile and it's target
        Point currentPoint = getCenter();
        Point targetPoint = targetSlicer.getCenter();
        // Convert them to vectors to perform some very basic vector math
        Vector2 target = targetPoint.asVector();
        Vector2 current = currentPoint.asVector();
        Vector2 distance = target.sub(current);
        // check if projectile has reached the slicer
        if (getRect().intersects(targetSlicer.getCenter())) {
            targetSlicer.decrementHealth(tower.getDamage());
            finished = true;
            return;
        }
        // move the projectile
        super.move(distance.normalised().mul(SPEED * Level.getTimescale()));
        super.update(input);
    }

    /**
     *
     * @return check if the projectile has finished
     */
    public boolean isFinished() {
        return finished;
    }

}
