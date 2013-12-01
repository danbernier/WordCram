package wordcram;

/**
 * A WordColorer tells WordCram what color to render a word in.
 * <p>
 * <b>Note:</b> if you implement your own WordColorer, you should be familiar
 * with how <a href="http://processing.org/reference/color_datatype.html"
 * target="blank">Processing represents colors</a> -- or just make sure it uses
 * Processing's <a href="http://processing.org/reference/color_.html"
 * target="blank">color</a> method.
 * <p>
 * Some useful implementations are available in {@link Colorers}.
 *
 * @author Dan Bernier
 */
public interface WordColorer {

    /**
     * What color should this {@link Word} be?
     *
     * @param word the word to pick the color for
     * @return the color for the word
     */
    public int colorFor(Word word);
}
