package State;

import bagel.AbstractGame;
import bagel.Input;
import bagel.Window;

import java.io.File;

/**
 * ShadowDefend, a tower defence game.
 * Some code was taken from the sample solution:
 * https://gitlab.eng.unimelb.edu.au/swen20003-S1-2020/rohylj/rohylj-project-1
 */

public class ShadowDefend extends AbstractGame {



    private static final int HEIGHT = 768;
    private static final int WIDTH = 1024;
    private static final int NEXT_WAVE = 1;
    // the number of levels in the game
    private int numLevels;
    // the level number we are currently on
    private int curLevelNum;
    // the level we are currently on
    private Level currentLevel;
    // if the game has finished or not
    private static boolean gameDone;

    /**
     * Creates a new instance of the ShadowDefend game
     */
    public ShadowDefend() {
        super(WIDTH, HEIGHT, "ShadowDefend");
        loadLevels();
        currentLevel = new Level(curLevelNum);
        gameDone = false;

    }

    /**
     * The entry-point for the game
     *
     * @param args Optional command-line arguments
     */
    public static void main(String[] args) {
        new ShadowDefend().run();
    }

    /**
     * determines how many levels we have in the game
     */
    private void loadLevels() {
        numLevels = 0;
        int i = 1;
        while (true) {
            // checks if the next level exists or not
            if (new File("res/levels/" + i + ".tmx").exists()) {
                numLevels++;
            } else {
                break;
            }
            i++;
        }
        this.curLevelNum = 1;
    }

    /**
     * Update the state of the game, potentially reading from input
     *
     * @param input The current mouse/keyboard state
     */

    @Override
    protected void update(Input input) {
        // continue updating the current level while it is incomplete
        if (!currentLevel.getIsFinished()) {
            currentLevel.update(input);
        } else {
            // when the last level is over, move on to the next
            if (curLevelNum + NEXT_WAVE <= numLevels) {
                curLevelNum++;
                currentLevel = new Level(curLevelNum);
            } else {
                // if the game is over, stop everything
                gameDone = true;
                currentLevel.update(input);
            }
        }
    }

    /**
     *
     * @return whether the game has finished or not
     */
    public static boolean isGameDone() {
        return gameDone;
    }

    /**
     *
     * @return get the height of the game window
     */
    public static int getHEIGHT() {
        return HEIGHT;
    }

    /**
     *
     * @return get the width of the game window
     */
    public static int getWIDTH() {
        return WIDTH;
    }
}
