package wordcram;

import processing.core.PVector;

/**
 * Once a WordPlacer tells WordCram where a word <i>should</i> go, a WordNudger
 * tells WordCram how to nudge it around the field, until it fits in with the
 * other words around it, or the WordCram gives up on the word.
 * <p>
 * WordCram gets a PVector from the nudger, and adds it to the word's desired
 * location, to find the next spot to try fitting the word. Note that the
 * PVectors returned from a nudger <i><b>don't accumulate</b></i>: if the placer
 * puts a Word at (0, 0), and the nudger returns (1, 1), and then (2, 2),
 * WordCram will try the word at (1, 1), and then (2, 2) -- <i>not</i> (1, 1)
 * and then (3, 3).
 * <p>
 * A WordNudger should probably start nudging the word only a little, to keep it
 * near its desired location, and gradually nudge it more and more, so that,
 * even if the desired area is congested, the word can still fit in somewhere.
 * This is why the WordCram passes in <code>attemptNumber</code>: it's the
 * number of times it's attempted to place the word. This could (for example)
 * scale the PVector, since the nudges don't accumulate (see above).
 *
 * @see RandomWordNudger
 * @see SpiralWordNudger
 *
 * @author Dan Bernier
 */
public interface WordNudger {

    /**
     * How should this word be nudged, this time?
     *
     * @param word
     *            the word to nudge
     * @param attemptNumber
     *            how many times WordCram has tried to place this word; starts
     *            at zero, and ends at
     *            <code>(int)((1.0-word.weight) * 600) + 100</code>
     * @return the PVector to add to the word's desired location, to get the
     *         next spot to try fitting the word
     */
    public PVector nudgeFor(Word word, int attemptNumber);
}
