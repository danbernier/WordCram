package wordcram;

import java.util.Random;

import processing.core.PFont;

public class Fonters {

    public static WordFonter alwaysUse(final PFont pfont) {
        return new WordFonter() {
            public PFont fontFor(Word word) {
                return pfont;
            }
        };
    }

    public static WordFonter pickFrom(final PFont... fonts) {
        final Random r = new Random();
        return new WordFonter() {
            public PFont fontFor(Word w) {
                return fonts[r.nextInt(fonts.length)];
            }
        };
    }
}
