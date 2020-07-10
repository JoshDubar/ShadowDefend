package Events;

import State.Level;
import State.Wave;

/**
 * spawn events control the spawning of slicers during a wave
 */
public class SpawnEvent extends Event {

    // the default polyline point to set a slicer towards
    private static final int DEFAULT_TARGET = 1;

    // the number of slicers the event spawns
    private int numSlicers;
    // the spawn delay between slicer spawns
    private double spawnDelay;
    // the type of slicer the event spawns
    private String enemyType;
    // how many slicers have been spawned
    private int spawnedSlicers;

    /**
     * creates a new Spawn Event
     *
     * @param waveNum the number of the wave containing this event
     * @param numSlicers the number of slicers the event spawns
     * @param spawnDelay the spawn delay between slicer spawns
     * @param enemyType the type of slicer the event spawns
     */
    public SpawnEvent(int waveNum, int numSlicers, double spawnDelay, String enemyType) {
        super(waveNum);
        this.numSlicers = numSlicers;
        // convert to seconds
        this.spawnDelay = spawnDelay / MS_TO_S;
        this.enemyType = enemyType;
        this.spawnedSlicers = 0;
    }

    /**
     * updates the event and adds slicers after delays are over
     */
    @Override
    public void update() {
        timePassed += Level.getTimescale();
        // once the delay is over, add a new slicer
        if (timePassed / Level.FPS >= spawnDelay && spawnedSlicers != numSlicers) {
            Wave.addSlicer(enemyType, Level.polyline.get(0), DEFAULT_TARGET);
            spawnedSlicers++;
            // Reset frame counter
            timePassed = 0;
            // once all slicers have been spawned, this wave has finished
            if (spawnedSlicers == numSlicers) {
                isFinished = true;
            }
        }
    }
}
