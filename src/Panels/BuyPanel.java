package Panels;

import State.Level;
import State.ShadowDefend;
import bagel.*;
import bagel.map.TiledMap;
import bagel.util.Point;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * the planel where our money is tracked and towers can be bought from
 */
public class BuyPanel {

    // cost of the tank
    private static final int TANKCOST = 250;
    // cost of the super tank
    private static final int SUPERTANKCOST = 600;
    // cost of the air support
    private static final int AIRSUPPORTCOST = 500;
    // the default left offset on the x-axis in the buy panel of the first icon
    private static final int DEFAULTOFFSET = 64;
    // the extra offset for each addition icon
    private static final int EXTRAOFFSET = 120;
    // the offset of the money from the right of the buy panel
    private static final int MONEYWIDTHOFFSET = 200;
    // the offset of the money from the top of the window
    private static final int MONEYHEIGHTOFFSET = 65;
    // the font size of the keybinds text
    private static final int KEYBINDS_FONT_SIZE = 14;
    // the font size of the player's money
    private static final int MONEY_FONT_SIZE = 50;
    // scale multiplier for the background
    private static final int DOUBLE_SCALE = 2;
    // middle offset divisor to find the middle of a rectangle
    private static final int MIDDLE_OFFSET = 2;
    // quarter offset divisor to find the quartile of a rectangle
    private static final int QUARTER_OFFSET = 4;
    // offset from the bottom of the buy panel
    private static final int BORDER_OFFSET = 10;
    // dollar sign to be used with money
    private static final String DOLLAR = "$";
    // font used in the text
    private static final String FONT = "res/fonts/DejaVuSans-Bold.ttf";
    // directory of the image of the buy panel
    private static final String BUYPANEL_IMAGE = "res/images/buypanel.png";
    // directory of the image of the tank
    private static final String TANK_IMAGE = "res/images/tank.png";
    // directory of the image of the super tank
    private static final String SUPER_TANK_IMAGE = "res/images/supertank.png";
    // directory of the image of the air support
    private static final String AIRSUPPORT_IMAGE = "res/images/airsupport.png";
    // the text for the key binds controls
    private static final String INSTRUCTIONS_TEXT = "Key binds:\n\nS - Start Wave\nL - Increase Timescale\nK - Decrease Timescale";
    // the names of the towers
    private static final String TANK_NAME = "tank";
    private static final String SUPER_TANK_NAME = "supertank";
    private static final String AIRSUPPORT_NAME = "airsupport";

    // the background image
    private Image background;
    // the icon of the different buy-able towers
    private TowerIcon tank;
    private TowerIcon superTank;
    private TowerIcon airsupport;
    // the fonts of the text in the buy panel
    private Font keyBinds;
    private Font currentMoney;

    // whether we have clicked an icon on the buy panel
    private static boolean hasClicked;

    /**
     * creates a new BuyPanel
     */
    public BuyPanel() {
        this.background = new Image(BUYPANEL_IMAGE);
        double towerYPos = background.getHeight()/MIDDLE_OFFSET - BORDER_OFFSET;
        this.tank = new TowerIcon(new Point(DEFAULTOFFSET, towerYPos), TANK_IMAGE, TANK_NAME, TANKCOST);
        this.superTank = new TowerIcon(new Point(DEFAULTOFFSET + EXTRAOFFSET, towerYPos), SUPER_TANK_IMAGE, SUPER_TANK_NAME, SUPERTANKCOST);
        this.airsupport = new TowerIcon(new Point(DEFAULTOFFSET + EXTRAOFFSET + EXTRAOFFSET,  towerYPos), AIRSUPPORT_IMAGE, AIRSUPPORT_NAME, AIRSUPPORTCOST);

        this.keyBinds = new Font(FONT, KEYBINDS_FONT_SIZE);
        this.currentMoney = new Font(FONT, MONEY_FONT_SIZE);

    }

    /**
     * updates and renders the buy panel and it's icons
     *
     * @param input the input given by the player
     * @param map the map of the current level
     */
    public void update(Input input, TiledMap map) {
        Point curMousePos = input.getMousePosition();
        background.draw(0,0, new DrawOptions().setScale(DOUBLE_SCALE, DOUBLE_SCALE));
        // update the icons and controls placing of towers
        tank.render(input, map, curMousePos);
        superTank.render(input, map, curMousePos);
        airsupport.render(input, map, curMousePos);
        // draw the key-bind controls and money onto the screen
        keyBinds.drawString(INSTRUCTIONS_TEXT, background.getWidth() / MIDDLE_OFFSET - DEFAULTOFFSET, background.getHeight() / QUARTER_OFFSET);
        currentMoney.drawString(moneyString(), ShadowDefend.getWIDTH() - MONEYWIDTHOFFSET, MONEYHEIGHTOFFSET);
    }

    /**
     * converts money to a string with commas for thousands
     *
     * @return the money string
     */
    private String moneyString() {
        return DOLLAR + NumberFormat.getNumberInstance(Locale.US).format(Level.getMoney());
    }

    /**
     *
     * @return whether an icon has been clicked or not
     */
    public static boolean isHasClicked() {
        return hasClicked;
    }

    /**
     * change the hasClicked value
     *
     * @param hasClicked whether an icon has been clicked or not
     */
    public static void setHasClicked(boolean hasClicked) {
        BuyPanel.hasClicked = hasClicked;
    }


}

