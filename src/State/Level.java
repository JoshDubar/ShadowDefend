package State;

import Events.*;
import Panels.*;
import Sprites.Slicers.*;
import Sprites.Towers.*;
import bagel.Input;
import bagel.Keys;
import bagel.Window;
import bagel.map.TiledMap;
import bagel.util.Point;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * the instance of a level in the game contains all the information
 * about one sequence of waves in the game and controls
 * everything that is rendered onto the screen
 *
 */
public class Level {

    // the FPS that the game runs at
    public static final double FPS = 60;
    // the minimum and starting timescale of the game
    private static final int INITIAL_TIMESCALE = 1;
    // the maximum timescale property of the game
    private static final int MAX_TIMESCALE = 5;
    // money we start the game with
    private static final int STARTING_CASH = 500;
    // lives we start the game with
    private static final int STARTING_LIVES = 25;
    // our statuses we have for different states in the game
    private static final String DEFAULT_STATUS = "Awaiting start";
    private static final String IN_PROGRESS_STATUS = "Wave In Progress";
    private static final String PLACING_STATUS = "Placing";
    private static final String WINNER_STATUS = "Winner!";
    // the directory of the events instructions
    private static final String WAVE_FILE = "res/levels/waves.txt";
    // character to split waves file lines on
    private static final String WAVES_FILE_SPLITTER = ",";
    // name of the spawn event
    private static final String SPAWN_EVENT_NAME = "spawn";
    // names of all our towers
    private static final String TANK_NAME = "tank";
    private static final String SUPER_TANK_NAME = "supertank";
    private static final String AIRSUPPORT_NAME = "airsupport";
    // indexes of wave file
    private static final int INDEX_OF_WAVE_NUM = 0;
    private static final int INDEX_OF_NUM_SLICERS = 2;
    private static final int INDEX_OF_SPAWN_DELAY = 4;
    private static final int INDEX_OF_ENEMY_TYPE = 3;
    private static final int INDEX_OF_DELAY = 2;
    private static final int INDEX_OF_EVENT_NAME = 1;
    // first wave
    private static final int FIRST_WAVE = 1;
    // Timescale is made static because it is a universal property of the game and the specification
    // says everything in the game is affected by this
    private static double timescale;
    // the amount of money we have, universal and needs to be accessed by other classes therefore static
    private static int money;
    // the lives we have, universal and needs to be accessed by other classes therefore static
    private static int lives;
    // the status/state of the game, universal and needs to be accessed by other classes therefore static
    private static String status;

    // the map of this level
    private final TiledMap map;
    // the polyline of the map (list of points that slicers travel between)
    public static List<Point> polyline;
    // true if a wave is in progress
    private static boolean waveStarted;
    // the buy panel of the level
    private BuyPanel buyPanel;
    // the status panel of the level
    private StatusPanel statusPanel;
    // the number of waves this level contains
    private int numWaves;
    // the waves that this level contains
    private ArrayList<Wave> waves;
    // the wave we are currently processing
    private Wave currentWave;
    // the index of the wave we are currently processing
    private static int currentWaveNum;
    // whether the level has finished or not
    private boolean isFinished;
    // all of the active towers in this level
    private static ArrayList<ActiveTower> activeTowers;
    // all of the passive towers in this level
    private static ArrayList<PassiveTower> passiveTowers;

    /**
     * Creates a new Level
     *
     * @param levelNum the level number we are on
     */
    public Level(int levelNum) {
        // load the map file
        this.map = new TiledMap("res/levels/" + levelNum + ".tmx");
        // load all of the events from the waves.txt file
        loadEvents(levelNum);
        // set default values
        timescale = INITIAL_TIMESCALE;
        money = STARTING_CASH;
        lives = STARTING_LIVES;
        status = DEFAULT_STATUS;
        polyline = this.map.getAllPolylines().get(0);
        waveStarted = false;
        this.buyPanel = new BuyPanel();
        this.statusPanel = new StatusPanel();
        this.isFinished = false;
        activeTowers = new ArrayList<>();
        passiveTowers = new ArrayList<>();
        // reset the route of the PassiveTower path
        PassiveTower.setPrevRoute(PassiveTower.getVertical());
        // Temporary fix for the weird slicer map glitch (might have to do with caching textures)
        new Regular(polyline, polyline.get(0), 0);
    }

    /**
     * processes all of the events that will be carried out during each wave in the level
     *
     * @param levelNum the number of the level we are starting
     */
    private void loadEvents(int levelNum) {

        ArrayList<Event> events = new ArrayList<Event>();
        this.numWaves = 0;
        this.waves = new ArrayList<>();
        currentWaveNum = 0;
        // read the file
        try (BufferedReader br = new BufferedReader(new FileReader(WAVE_FILE))) {
            String text;
            // process each line
            while ((text = br.readLine()) != null) {
                String[] currentLine = text.split(WAVES_FILE_SPLITTER);
                int wave = Integer.parseInt(currentLine[INDEX_OF_WAVE_NUM]);
                // when the wave number changes, add the previous wave to the list
                if (wave != FIRST_WAVE && wave != events.get(0).getWaveNum()) {
                    numWaves++;
                    waves.add(new Wave(events));
                    events = new ArrayList<>();
                }
                // differs between spawn and delay events
                if (currentLine[INDEX_OF_EVENT_NAME].equals(SPAWN_EVENT_NAME)) {
                    events.add(new SpawnEvent(wave, Integer.parseInt(currentLine[INDEX_OF_NUM_SLICERS]),Integer.parseInt(currentLine[INDEX_OF_SPAWN_DELAY]), currentLine[INDEX_OF_ENEMY_TYPE]));
                } else {
                    events.add(new DelayEvent(wave, Integer.parseInt(currentLine[INDEX_OF_DELAY])));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        waves.add(new Wave(events));
        numWaves++;

    }

    /**
     *
     * @return retrieves the current timescale of the level
     */
    public static double getTimescale() {
        return timescale;
    }
    /**
     * Increases the timescale by an increment of 1
     */
    private void increaseTimescale() {
        timescale = (timescale < MAX_TIMESCALE) ? timescale + 1 : timescale;
    }

    /**
     * Decreases the timescale by 1 but doesn't go below the base timescale
     */
    private void decreaseTimescale() {
        timescale = (timescale > 1) ? timescale - 1 : timescale;
    }

    public void update(Input input) {
        // no lives left? game over
        if (lives <= 0) {
            Window.close();
        }
        if (isFinished) {
            // game is finished, pause game and only draw User Interface and towers
            map.draw(0, 0, 0, 0, ShadowDefend.getWIDTH(), ShadowDefend.getHEIGHT());
            for (int i = activeTowers.size() - 1; i >= 0; i--) {
                ActiveTower s = activeTowers.get(i);
                s.update(input);
            }
            buyPanel.update(input, map);
            statusPanel.update();
            setStatus();
            return;
        }

        // Draw map from the top left of the window
        map.draw(0, 0, 0, 0, ShadowDefend.getWIDTH(), ShadowDefend.getHEIGHT());

        // Handle controls
        if (input.wasPressed(Keys.S) && !waveStarted) {
            waveStarted = true;
            currentWave = waves.get(currentWaveNum);
        }

        if (input.wasPressed(Keys.L)) {
            increaseTimescale();
        }

        if (input.wasPressed(Keys.K)) {
            decreaseTimescale();
        }

        // Check if it is time to spawn a new slicer (and we have some left to spawn)
        if (waveStarted && !currentWave.getIsfinished()) {
            currentWave.update(input);
        } else if (waveStarted && currentWave.getIsfinished()) {
            waveStarted = false;
            currentWaveNum++;
            if (currentWaveNum == numWaves) {
                isFinished = true;
            }
        }
        // update all towers, remove passive tower if journey is finished
        for (int i = activeTowers.size() - 1; i >= 0; i--) {
            ActiveTower s = activeTowers.get(i);
            s.update(input);
        }
        for (int i = passiveTowers.size() - 1; i >= 0; i--) {
            PassiveTower s = passiveTowers.get(i);
            s.update(input);
            if (s.getIsFinished()) {
                passiveTowers.remove(i);
            }
        }

        // update User Interface
        buyPanel.update(input, map);
        statusPanel.update();
        setStatus();

    }

    /**
     * add a new tower to the level
     *
     * @param name name of the tower we're adding
     * @param location location tower will be at
     */
    public static void addTower(String name, Point location) {
        // based on the name of the tower, create that type of tower on the map
        switch (name) {
            case TANK_NAME:
                activeTowers.add(new Tank(location));
                break;
            case SUPER_TANK_NAME:
                activeTowers.add(new SuperTank(location));
                break;
            case AIRSUPPORT_NAME:
                passiveTowers.add(new AirSupport(location));
        }
    }

    /**
     *
     * @return number of the current wave
     */
    public static int getCurrentWaveNum() {
        return currentWaveNum;
    }

    /**
     *
     * @return check whether the level is over
     */
    public boolean getIsFinished() {
        return isFinished;
    }

    /**
     *
     * @return get all of the active towers we have
     */
    public static ArrayList<ActiveTower> getActiveTowers() {
        return activeTowers;
    }

    /**
     *
     * @return total money we have
     */
    public static int getMoney() {
        return money;
    }

    /**
     * update the money we have
     *
     * @param money amount we want to change our money by
     */
    public static void changeMoney(int money) {
        Level.money += money;
    }

    /**
     *
     * @return number of lives we currently have
     */
    public static int getLives() {
        return lives;
    }

    /**
     * change the lives we have
     *
     * @param lives number of lives to set our lives to
     */
    public static void setLives(int lives) {
        Level.lives = lives;
    }

    /**
     *
     * @return the current status of the level
     */
    public static String getStatus() {
        return status;
    }

    /**
     *
     * @return check whether a wave is in progress
     */
    public static boolean getWaveStarted() {
        return waveStarted;
    }

    /**
     *
     * set the status of level
     */
    public static void setStatus() {
        // order of precedence
        if (ShadowDefend.isGameDone()) {
            status = WINNER_STATUS;
        }
        else if (BuyPanel.isHasClicked()) {
            status = PLACING_STATUS;
        }
        else if (getWaveStarted()) {
            status = IN_PROGRESS_STATUS;
        } else {
            status = DEFAULT_STATUS;
        }
    }
}
