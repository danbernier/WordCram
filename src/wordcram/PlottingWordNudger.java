package wordcram;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * If you're using a custom WordNudger, and having difficulty seeing
 * how well it works, try wrapping it in a PlottingWordNudger. As your
 * WordCram is drawn, it'll render tiny dots at each location it
 * nudges your words to, so you can see how well it's working.
 */
public class PlottingWordNudger implements WordNudger {

    private PApplet parent;
    private WordNudger wrappedNudger;

    public PlottingWordNudger(PApplet _parent, WordNudger _wrappedNudger) {
        parent = _parent;
        wrappedNudger = _wrappedNudger;
    }

    public PVector nudgeFor(Word word, int attempt) {
        PVector v = wrappedNudger.nudgeFor(word, attempt);
        parent.pushStyle();
        parent.noStroke();

        float alpha = attempt/700f;
        //alpha = (float) Math.pow(alpha, 3);
        parent.fill(40, 255, 255); //, alpha * 255);

        PVector wordLoc = PVector.add(v, word.getTargetPlace());
        parent.ellipse(wordLoc.x, wordLoc.y, 3, 3);
        parent.popStyle();
        return v;
    }

}
