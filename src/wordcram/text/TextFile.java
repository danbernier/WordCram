package wordcram.text;

import processing.core.PApplet;

public class TextFile implements TextSource {

    private String path;

    // TODO if we move all .text.* classes into WordCram, we can kill this, and
    // use pkg-local methods for setting the parent...
    private PApplet parent;

    public TextFile(String path, PApplet parent) {
        this.path = path;
        this.parent = parent;
    }

    public String getText() {
        return PApplet.join(parent.loadStrings(path), ' ');
    }

}
