package wordcram;

import java.util.Random;

import processing.core.PApplet;

public class Colorers {

    public static WordColorer twoHuesRandomSats(final PApplet host) {

        final float[] hues = new float[] { host.random(256), host.random(256) };

        return new HsbWordColorer(host) {
            public int getColorFor(Word w) {

                float hue = hues[(int)host.random(hues.length)];
                float sat = host.random(256);
                float val = host.random(100, 256);

                return host.color(hue, sat, val);
            }
        };
    }

    public static WordColorer twoHuesRandomSatsOnWhite(final PApplet host) {

        final float[] hues = new float[] { host.random(256), host.random(256) };

        return new HsbWordColorer(host) {
            public int getColorFor(Word w) {

                float hue = hues[(int)host.random(hues.length)];
                float sat = host.random(256);
                float val = host.random(156);

                return host.color(hue, sat, val);
            }
        };
    }

    public static WordColorer pickFrom(final int... colors) {
        final Random r = new Random();
        return new WordColorer() {
            public int colorFor(Word w) {
                return colors[r.nextInt(colors.length)];
            }
        };
    }

    // TODO add an overload that takes 1 int (greyscale), 2 ints (greyscale/alpha), etc
    public static WordColorer alwaysUse(final int color) {
        return new WordColorer() {
            public int colorFor(Word w) {
                return color;
            }
        };
    }
}
