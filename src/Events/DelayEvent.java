package Events;

import State.Level;
import Events.Event;

/**
 * delay events control delays between spawns in waves
 */
public class DelayEvent extends Event {

    // the length of the delay
    private double delayTime;

    /**
     * creates a new Delay Event
     *
     * @param waveNum the number of the wave containing this event
     * @param delayTime the length of the delay
     */
    public DelayEvent(int waveNum, double delayTime) {
        super(waveNum);
        // convert to seconds
        this.delayTime = delayTime / MS_TO_S;
    }

    /**
     * measures whether the event is over yet
     */
    @Override
    public void update() {
        timePassed += Level.getTimescale();
        if (timePassed / Level.FPS >= delayTime) {
            isFinished = true;
        }
    }


}
