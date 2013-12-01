package wordcram;

/**
 * A WordAngler tells WordCram what angle to draw a word at, in radians.
 * <p>
 * Some useful implementations are available in {@link Anglers}.
 *
 * @author Dan Bernier
 */
public interface WordAngler {

    /**
     * What angle should this {@link Word} be rotated at?
     *
     * @param word
     *            The Word that WordCram is about to draw, and wants to rotate
     * @return the rotation angle for the Word, in radians
     */
    public float angleFor(Word word);
}
