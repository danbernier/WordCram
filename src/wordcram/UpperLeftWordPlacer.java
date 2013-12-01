package wordcram;

import java.util.Random;

import processing.core.PApplet;
import processing.core.PVector;

public class UpperLeftWordPlacer implements WordPlacer {

    private Random r = new Random();

    public PVector place(Word word, int wordIndex, int wordsCount, int wordImageWidth, int wordImageHeight, int fieldWidth, int fieldHeight) {
        int x = getOneUnder(fieldWidth - wordImageWidth);
        int y = getOneUnder(fieldHeight - wordImageHeight);
        return new PVector(x, y);
    }

    private int getOneUnder(int limit) {
        return PApplet.round(PApplet.map(random(random(random(random(random(1.0f))))), 0, 1, 0, limit));
    }

    private float random(float limit) {
        return r.nextFloat() * limit;
    }

}
