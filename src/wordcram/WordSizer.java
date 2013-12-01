package wordcram;

/**
 * A WordSizer tells WordCram how big to render each word.
 * You'll pass a WordSizer to WordCram via {@link WordCram#withSizer(WordSizer)}.
 * <p>
 * Some useful implementations are available in {@link Sizers}.
 *
 * @author Dan Bernier
 */
public interface WordSizer {

    /**
     * How big should this {@link Word} be rendered?
     * <p>
     * Generally, a word cloud draws more important words bigger. Two typical
     * ways to measure word's importance are its weight, and its rank (its
     * position in the list of words, sorted by weight).
     * <p>
     * Given that, sizeFor is passed the Word (which knows its own weight), its
     * rank, and the total number of words.
     * <p>
     * For example, given the text "I think I can I think", the words would look
     * like this:
     * <ul>
     * <li>"I", weight 1.0 (3/3), rank 1</li>
     * <li>"think", weight 0.667 (2/3), rank 2</li>
     * <li>"can", weight 0.333 (1/3), rank 3</li>
     * </ul>
     * ...and the WordSizer would be called with the following values:
     * <ul>
     * <li>Word "I" (weight 1.0), 1, 3</li>
     * <li>Word "think" (weight 0.667), 2, 3</li>
     * <li>Word "can" (weight 0.333), 3, 3</li>
     * </ul>
     *
     * @param word
     *            the Word to determine the size of
     * @param wordRank
     *            the rank of the Word
     * @param wordCount
     *            the total number of Words being rendered
     * @return the size to render the Word
     */
    public float sizeFor(Word word, int wordRank, int wordCount);
}
