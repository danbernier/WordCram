package wordcram;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * If you're using a custom WordPlacer, and having difficulty seeing
 * how well it works, try wrapping it in a PlottingWordPlacer. As your
 * WordCram is drawn, it'll render tiny dots at each word's target
 * location, so you can sort-of see how far off they are when they're
 * finally rendered.
 */
public class PlottingWordPlacer implements WordPlacer {

    private PApplet parent;
    private WordPlacer wrappedPlacer;

    public PlottingWordPlacer(PApplet _parent, WordPlacer _wrappedPlacer) {
        parent = _parent;
        wrappedPlacer = _wrappedPlacer;
    }

    public PVector place(Word word, int wordIndex, int wordsCount, int wordImageWidth, int wordImageHeight, int fieldWidth, int fieldHeight) {
        PVector v = wrappedPlacer.place(word, wordIndex, wordsCount, wordImageWidth, wordImageHeight, fieldWidth, fieldHeight);
        parent.pushStyle();
        parent.noFill();

        parent.stroke(15, 255, 255, 200);

        parent.ellipse(v.x, v.y, 10, 10);
        parent.popStyle();
        return v;
    }

}
