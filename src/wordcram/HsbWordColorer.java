package wordcram;

import processing.core.PApplet;
import processing.core.PConstants;

abstract class HsbWordColorer implements WordColorer {
    private PApplet host;
    private int range;

    HsbWordColorer(PApplet host) {
        this(host, 255);
    }
    HsbWordColorer(PApplet host, int range) {
        this.host = host;
        this.range = range;
    }

    public int colorFor(Word word) {
        host.pushStyle();
        host.colorMode(PConstants.HSB, range);
        int color = getColorFor(word);
        host.popStyle();
        return color;
    }

    abstract int getColorFor(Word word);
}
