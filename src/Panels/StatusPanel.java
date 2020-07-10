package Panels;

import State.Level;
import State.ShadowDefend;
import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.util.Colour;

/**
 * the status panel, shows information about the state of the game
 */
public class StatusPanel {

    // the font of the text on the status panel
    private static final String FONT = "res/fonts/DejaVuSans-Bold.ttf";
    // font size of text on the status panel
    private static final int FONT_SIZE = 18;
    // the directory of the image of the status panel
    private static final String IMAGE_SRC = "res/images/statuspanel.png";
    // scale multiplier on background image
    private static final int SCALE_MULTIPLIER = 2;
    // offset of the wave number we are on
    private static final int WAVE_NUM_OFFSET = 1;
    // offset of the height of the text from the status panel
    private static final int HEIGHT_OFFSET = 8;
    // the minimum time scale value
    private static final double MIN_TIMESCALE = 1.0;
    // offset of the wave number on the x axis
    private static final int WAVE_X_OFFSET = 5;
    // timescale location on x-axis relative to width
    private static final double TIMESCALE_LOC = 5.0;
    // right hand side width offset, for aesthetic purposes
    private static final int WIDTH_OFFSET = 100;
    // status location on x-axis relative to width
    private static final double STATUS_LOC = 2.0;
    // pre-fix text for all items on the panel
    private static final String STATUS_TEXT = "Status: ";
    private static final String WAVE_TEXT = "Waves: ";
    private static final String TIMESCALE_TEXT = "Time Scale: ";
    private static final String LIVES_TEXT = "Lives: ";

    // the image of the status panel background
    private Image background;
    // fonts for all of the status panel text
    private Font drawWave;
    private Font drawTimescale;
    private Font drawStatus;
    private Font drawLives;

    /**
     * creates a new status panel
     */
    public StatusPanel() {
        this.background = new Image(IMAGE_SRC);
        this.drawWave = new Font(FONT, FONT_SIZE);
        this.drawLives = new Font(FONT, FONT_SIZE);
        this.drawTimescale = new Font(FONT, FONT_SIZE);
        this.drawStatus = new Font(FONT, FONT_SIZE);
    }

    /**
     * updates and renders the status panel
     */
    public void update() {
        background.draw(0, ShadowDefend.getHEIGHT(), new DrawOptions().setScale(SCALE_MULTIPLIER,SCALE_MULTIPLIER));
        drawWave.drawString(WAVE_TEXT + (Level.getCurrentWaveNum() + WAVE_NUM_OFFSET), WAVE_X_OFFSET, ShadowDefend.getHEIGHT() - HEIGHT_OFFSET);
        drawTimescale.drawString(TIMESCALE_TEXT + Level.getTimescale(), ShadowDefend.getWIDTH() / TIMESCALE_LOC, ShadowDefend.getHEIGHT() - HEIGHT_OFFSET, new DrawOptions().setBlendColour(Level.getTimescale() > MIN_TIMESCALE ? Colour.GREEN: Colour.WHITE));
        drawStatus.drawString(STATUS_TEXT + Level.getStatus(),  ShadowDefend.getWIDTH() / STATUS_LOC - WIDTH_OFFSET, ShadowDefend.getHEIGHT() - HEIGHT_OFFSET);
        drawLives.drawString(LIVES_TEXT + Level.getLives(), ShadowDefend.getWIDTH() - WIDTH_OFFSET, ShadowDefend.getHEIGHT() - HEIGHT_OFFSET);

    }
}
