package wordcram;

import processing.core.PFont;

/**
 * A WordFonter tells WordCram what font to render a word in.
 * <p>
 * Some useful implementations are available in {@link Fonters}.
 * <p>
 * <i>The name "WordFonter" was picked because it matches the others: WordColorer,
 * WordPlacer, WordSizer, etc. Apologies if it sounds a bit weird to your ear; I
 * eventually got used to it.</i>
 *
 * @author Dan Bernier
 */
public interface WordFonter {

    /**
     * What font should this {@link Word} be drawn in?
     * @param word the word to pick the PFont for
     * @return the PFont for the word
     */
    public PFont fontFor(Word word);
}
