package Sprites.Towers;

import Sprites.Slicers.Slicer;
import Sprites.Sprite;
import Sprites.Weapons.Projectile;
import State.Level;
import bagel.Input;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.ArrayList;

/**
 *
 * Active towers are towers that actively lock on to enemies and shoot projectiles at them to inflict damage
 *
 */
public abstract class ActiveTower extends Sprite {

    // degree to correct angle by from the default value
    private static final double ANGLE_CORRECTION = Math.PI / 2;
    // convert Milliseconds to seconds
    private static final double MS_TO_S = 1000;

    // damage that the tower inflicts
    private int damage;
    // cooldown in between shots
    private double cooldown;
    // the directory containing the projectile image
    private String projectileSrc;
    // the time passed until the cooldown is over
    private double cooldownTimer;
    // the target of the tower
    private Slicer target;
    // the shooting radius of the tower
    private Rectangle radiusBox;
    // the projectiles in the air from the tower
    private ArrayList<Projectile> projectiles;

    /**
     * creates a new ActiveTower
     *
     * @param location the location of the tower
     * @param effectRadius the radius the tower can shoot in
     * @param damage the damage inflicted by the tower's projectiles
     * @param imageSrc the image of the tower
     * @param cooldown the cooldown between shots for the tower
     * @param projectileSrc the image of the tower's projectiles
     */
    public ActiveTower(Point location, int effectRadius, int damage, String imageSrc, double cooldown, String projectileSrc) {
        super(location, imageSrc);
        this.damage = damage;
        // convert the cooldown to seconds
        this.cooldown = cooldown / MS_TO_S;
        this.projectileSrc = projectileSrc;
        this.cooldownTimer = 0.0;
        this.target = null;
        // create a rectangle around the tower representing it's radius
        this.radiusBox = new Rectangle(getCenter().x - effectRadius, getCenter().y - effectRadius, effectRadius*2, effectRadius*2);
        this.projectiles = new ArrayList<>();
    }

    /**
     * updates and renders the tower and it's projectiles
     *
     * @param input the input given by the player
     */
    @Override
    public void update(Input input) {
        cooldownTimer += Level.getTimescale();
        // removes the target if it is no longer in the target's range or it is dead
        if (target != null && (!target.getRect().intersects(radiusBox) || target.getHealth() <= 0)) {
            target = null;
        } else if (target != null) {
            // shoot a new projectile once the cooldown has expired
            if (cooldownTimer / Level.FPS >= cooldown) {
                shoot();
                // set the angle of the tower to the slicer it is firing at
                setAngle(Math.atan2(target.getCenter().y - getCenter().y, target.getCenter().x - getCenter().x) + ANGLE_CORRECTION);
                cooldownTimer = 0;
            }
        }
        // update all projectiles and remove them if they've reached their target
        for (int i = projectiles.size() - 1;i >= 0;i--){
            Projectile proj = projectiles.get(i);
            proj.update(input);
            if (proj.isFinished()) {
                projectiles.remove(i);
            }
        }
        super.update(input);
    }

    /**
     *
     * @return gets the target slicer of the tower
     */
   public Slicer getTarget() {
        return target;
    }

    /**
     * sets the target of the tower
     *
     * @param target the slicer the tower will now be targeting
     */
    public void setTarget(Slicer target) {
        this.target = target;
    }

    /**
     *
     * @return gets the radius of the tower's attack
     */
    public Rectangle getRadiusBox() {
        return radiusBox;
    }

    /**
     * shoots a projectile at the target slicer
     */
    private void shoot() {
        projectiles.add(new Projectile(this, target, projectileSrc));
    }

    /**
     *
     * @return gets the damage that the tower's projectiles inflict
     */
    public int getDamage() {
        return damage;
    }

}
