package State;

import Events.Event;
import Sprites.Slicers.*;
import bagel.Input;
import bagel.util.Point;
import java.util.ArrayList;

/**
 * a Wave is the entity used to control and run through events,
 * after all events have been processed the wave is complete
 */
public class Wave {

    // base amount of cash rewarded to the player after each wave, without the multiplier
    private static final int baseCash = 150;
    // the multiplier used for the amount of cash rewarded after each wave
    private static final int cashMultiplier = 100;
    // names of all of the different slicers
    private static final String SLICER_NAME = "slicer";
    private static final String SUPER_SLICER_NAME = "superslicer";
    private static final String MEGA_SLICER_NAME = "megaslicer";
    private static final String APEX_SLICER_NAME = "apexslicer";

    // stores all of this wave's events
    private ArrayList<Event> events;
    // checks if the wave is finished or not
    private boolean isFinished;
    // the index of the event we are currently processing
    private int eventNum;
    // stores all of the slicers currently on the screen
    private static ArrayList<Slicer> slicersOnScreen;
    // the wave number of this wave in the level
    private int waveNum;
    // the event we are currently processing
    private Event currentEvent;
    // the overall reward from this wave
    private int reward;

    /**
     * Creates a new Wave
     *
     * @param events the events that this wave has to process
     */
    public Wave(ArrayList<Event> events) {
        this.events = events;
        this.waveNum = events.get(0).getWaveNum();
        this.isFinished = false;
        this.eventNum = 0;
        this.currentEvent = events.get(eventNum);
        this.reward = baseCash + cashMultiplier * waveNum;
        slicersOnScreen = new ArrayList<>();
    }

    /**
     * updates the state of the Wave object and the slicers it contains
     *
     * @param input the input given from the user
     */
    public void update(Input input) {
        if (!currentEvent.getIsFinished()) {
            currentEvent.update();
        }
        // when an event has ended, move on
        else if (currentEvent.getIsFinished()){
            eventNum++;
        }
        // if all events have been processed, reward is given and the wave is over
        if (eventNum >= events.size() && slicersOnScreen.size() == 0) {
            isFinished = true;
            Level.changeMoney(reward);
        } else if (eventNum < events.size()) {
            currentEvent = events.get(eventNum);
        }
        // remove slicers if they've finished their path
        for (int i = slicersOnScreen.size() - 1; i >= 0; i--) {
            Slicer s = slicersOnScreen.get(i);
            if (s.isFinished()) {
                // decrement the player's lives if the slicer is finished
                Level.setLives(Level.getLives() - slicersOnScreen.get(i).getPenalty());
                slicersOnScreen.remove(i);
            } else if (s.getHealth() <= 0) {
                // if the slicer has children, spawn them at the slicer's location
                s.spawnChildren();
                slicersOnScreen.remove(i);
                Level.changeMoney(s.getReward());
            }
        }
        // update all slicers
        for (Slicer s : slicersOnScreen) {
            s.update(input);
        }

    }

    /**
     * adds a slicer to the wave
     *
     * @param slicerString the name of the slicer we are adding
     * @param startingPoint the starting point of the slicer on the map
     * @param targetPointIndex the location that the slicer is heading towards on the polyline
     */
    public static void addSlicer(String slicerString, Point startingPoint, int targetPointIndex) {
        switch (slicerString) {
            case SLICER_NAME:
                slicersOnScreen.add(new Regular(Level.polyline, startingPoint, targetPointIndex));
                break;
            case SUPER_SLICER_NAME:
                slicersOnScreen.add(new Super(Level.polyline, startingPoint, targetPointIndex));
                break;
            case MEGA_SLICER_NAME:
                slicersOnScreen.add(new Mega(Level.polyline, startingPoint, targetPointIndex));
                break;
            case APEX_SLICER_NAME:
                slicersOnScreen.add(new Apex(Level.polyline, startingPoint, targetPointIndex));
                break;
        }
    }

    /**
     *
     * @return whether the wave has finished
     */
    public boolean getIsfinished() {
        return isFinished;
    }

    /**
     *
     * @return all of the active slicers
     */
    public static ArrayList<Slicer> getSlicersOnScreen() {
        return slicersOnScreen;
    }

}
