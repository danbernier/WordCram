package wordcram;

import java.util.Random;

import processing.core.PVector;

/**
 * A RandomWordNudger, where each attempt's PVector has X & Y coords
 * distributed randomly around the desired point, multiplied by a standard deviation,
 * and multiplied by the attempt number (so it gets farther, as it gets more
 * desperate).
 *
 * @author Dan Bernier
 */
public class RandomWordNudger implements WordNudger {

    private Random r = new Random();
    private float stdDev;

    /**
     * Create a RandomWordNudger with a standard deviation of 0.6.
     */
    public RandomWordNudger() {
        this(0.6f);
    }

    /**
     * Create a RandomWordNudger with your own standard deviation.
     */
    public RandomWordNudger(float stdDev) {
        this.stdDev = stdDev;
    }

    public PVector nudgeFor(Word w, int attempt) {
        return new PVector(next(attempt), next(attempt));
    }

    private float next(int attempt) {
        return (float)r.nextGaussian() * attempt * stdDev;
    }

}
