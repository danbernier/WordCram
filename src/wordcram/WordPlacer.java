package wordcram;

import processing.core.PVector;

/**
 * A WordPlacer tells WordCram where to place a word (in x,y coordinates) on the field.
 * <p>
 * A WordPlacer only suggests: the WordCram will try to place the Word where the
 * WordPlacer tells it to, but if the Word overlaps other Words, a WordNudger
 * will suggest different near-by spots for the Word until it fits, or until the
 * WordCram gives up.
 * <p>
 * Some useful implementations are available in {@link Placers}.
 *
 * @author Dan Bernier
 */
public interface WordPlacer {

    /**
     * Where should this {@link Word} be drawn on the field?
     *
     * @param word
     *            The Word to place. A typical WordPlacer might use the Word's
     *            weight.
     * @param wordIndex
     *            The index (rank) of the Word to place. Since this isn't a
     *            property of the Word, it's passed in as well.
     * @param wordsCount
     *            The total number of words. Gives a context to wordIndex:
     *            "Word {wordIndex} of {wordsCount}".
     * @param wordImageWidth
     *            The width of the word image.
     * @param wordImageHeight
     *            The height of the word image.
     * @param fieldWidth
     *            The width of the field.
     * @param fieldHeight
     *            The height of the field.
     * @return the desired location for a Word on the field, as a 2D PVector.
     */
    public abstract PVector place(Word word, int wordIndex, int wordsCount,
            int wordImageWidth, int wordImageHeight, int fieldWidth,
            int fieldHeight);
}
