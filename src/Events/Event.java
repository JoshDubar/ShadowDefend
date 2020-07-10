package Events;

/**
 * events control the delays/spawning of slicers within a wave
 */
public abstract class Event {

    // convert milliseconds to seconds
    protected static final int MS_TO_S = 1000;

    // the wave number containing this event
    private int waveNum;
    // the time passed during the event
    protected int timePassed;
    // whether the event has finished
    protected boolean isFinished;

    /**
     * creates a new Event
     *
     * @param waveNum the number of the wave containing this event
     */
    public Event(int waveNum) {
        this.waveNum = waveNum;
        this.timePassed = 0;
        this.isFinished = false;
    }

    /**
     * updates the current event
     */
    public abstract void update();

    /**
     *
     * @return the wave number of the event
     */
    public int getWaveNum() {
        return waveNum;
    }

    /**
     *
     * @return whether the event is over
     */
    public boolean getIsFinished() { return isFinished; }

}
