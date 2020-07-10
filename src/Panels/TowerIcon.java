package Panels;

import Sprites.Towers.ActiveTower;
import State.Level;
import State.ShadowDefend;
import bagel.*;
import bagel.map.TiledMap;
import bagel.util.Colour;
import bagel.util.Point;
import java.util.ArrayList;

/**
 * the icons of the towers in the buy panel
 */
public class TowerIcon {

    // the blocked property
    private static final String BLOCKED_PROPERTY = "blocked";
    // name of the airsupport tower
    private static final String AIRSUPPORT_NAME = "airsupport";
    // font size of the text on the pane;
    private static final int FONT_SIZE = 22;
    // dollar sign used for money
    private static final String DOLLAR = "$";
    // the height of the buy panel
    private static final double BUY_POS = new Image("res/images/buypanel.png").getHeight();
    // the height of the status panel
    private static final double STATUS_POS = new Image("res/images/statuspanel.png").getHeight();
    // the offset of the tower's cost from the bottom of the buy panel
    private static final int TEXT_Y_OFFSET = 10;
    // the font used for the text
    private static final String FONT = "res/fonts/DejaVuSans-Bold.ttf";
    // a small offset on the text for aesthetic purposes
    private static final int COST_POS_OFFSET = 5;
    // for when we need to half a value
    private static final double HALF = 0.5;

    // the location of the icon image
    private Point location;
    // the location of the icon's cost
    private Point textLocation;
    // the image of the icon
    private Image image;
    // the settings on the font of the text
    private DrawOptions textOptions;
    // the name of the tower
    private String name;
    // the cost of the tower
    private int cost;
    // the text of the cost
    private Font costText;
    // whether the icon has been clicked
    private boolean clicked;

    /**
     * creates a new Tower Icon
     *
     * @param location the location of the icon
     * @param imageSrc the directory of the image of the tower
     * @param name the name of the tower
     * @param cost the cost of the tower
     */
    public TowerIcon(Point location, String imageSrc, String name, int cost) {
        this.location = location;
        this.image = new Image(imageSrc);
        // sets the location of the cost text
        this.textLocation = new Point(location.x - image.getWidth() * HALF + COST_POS_OFFSET, BUY_POS - TEXT_Y_OFFSET);
        this.textOptions = new DrawOptions();
        this.name = name;
        this.cost = cost;
        this.costText = new Font(FONT, FONT_SIZE);
        this.clicked = false;
    }

    /**
     * render and update the tower icon along with the icon on the mouse if it has been clicked
     *
     * @param input the input given by the player
     * @param map the map of the level we are on
     * @param curMousePos the position of the mouse on the screen
     */
    public void render(Input input, TiledMap map, Point curMousePos) {
        // disable clicking once the game is over
        if (!ShadowDefend.isGameDone()) {
            // check if an icon has been clicked and update icon if it has
            checkClick(input, curMousePos, map);
        }

        affordColour();
        image.draw(location.x, location.y);
        // draw the tower and it's cost onto the buy panel
        costText.drawString(DOLLAR + cost, textLocation.x, textLocation.y, textOptions);

    }

    /**
     * checks if we have clicked an icon and place it when we click again
     *
     * @param input the input given by the player
     * @param curMousePos the current position of the mouse on the screen
     * @param map the map of the level we are on
     */
    public void checkClick(Input input, Point curMousePos, TiledMap map) {
        // check if we have clicked an icon and the mouse is on the screen
        if (clicked && (0 <= curMousePos.x && curMousePos.x < ShadowDefend.getWIDTH()) && (0 <= curMousePos.y && curMousePos.y < ShadowDefend.getHEIGHT())) {
            // check if the mouse is on a blocked tile
            boolean blockedTile = checkIntersection(curMousePos, map);
            // if the tile isn't blocked and the player left clicks then a tower is placed
            if (!blockedTile && input.wasPressed(MouseButtons.LEFT)) {
                Level.addTower(name, curMousePos);
                Level.changeMoney(-cost);
                clicked = false;
                BuyPanel.setHasClicked(false);
            }
            // draw the tower icon at the mouse if tile isnt blocked
            else if (!blockedTile) {
                image.draw(curMousePos.x, curMousePos.y);
            }
            // cancels the icon hover
            if (input.wasPressed(MouseButtons.RIGHT)) {
                clicked = false;
                BuyPanel.setHasClicked(false);
            }
        }

        // checks if a tower can be placed
        if (Level.getMoney() >= cost && !BuyPanel.isHasClicked() && input.wasPressed(MouseButtons.LEFT) && image.getBoundingBoxAt(location).intersects(curMousePos)) {
            clicked = true;
            BuyPanel.setHasClicked(true);
        }
    }

    /**
     * changes the colour of the text based on if we can afford it
     */
    private void affordColour() {
        textOptions.setBlendColour(Level.getMoney() >= cost ? Colour.GREEN : Colour.RED);
    }

    /**
     * checks if the mouse is intersecting with a blocked tile/tower/panel
     *
     * @param curMousePos current position of the mouse on the screen
     * @param map the map of the level we are on
     * @return whether or not the tile is blocked
     */
    private boolean checkIntersection(Point curMousePos, TiledMap map) {
        // check if the tile is blocked
        if (map.getPropertyBoolean((int)curMousePos.x, (int)curMousePos.y, BLOCKED_PROPERTY, false) && !name.equals(AIRSUPPORT_NAME)) return true;
        // check if the mouse is not on a panel
        if (curMousePos.y < BUY_POS || curMousePos.y > ShadowDefend.getHEIGHT() - STATUS_POS) return true;
        // checks if a tower is not at the mouse
        ArrayList<ActiveTower> towers = Level.getActiveTowers();
        for (ActiveTower tower : towers) {
            if (tower.getRect().intersects(curMousePos)) {
                return true;
            }
        }
        // if the tile is not blocked, then the mouse is not intersecting
        return false;
    }
}
