package wordcram;

import java.util.Random;

import processing.core.PApplet;
import processing.core.PVector;

public class Placers {

    public static WordPlacer horizLine() {
        final Random r = new Random();

        return new WordPlacer() {
            public PVector place(Word word, int wordIndex, int wordsCount,
                    int wordImageWidth, int wordImageHeight, int fieldWidth, int fieldHeight) {
                int centerHorizLine = (int) ((fieldHeight - wordImageHeight) * 0.5);
                int centerVertLine = (int) ((fieldWidth - wordImageWidth) * 0.5);

                float xOff = (float) r.nextGaussian() * ((fieldWidth - wordImageWidth) * 0.2f);
                float yOff = (float) r.nextGaussian() * 50;

                return new PVector(centerVertLine + xOff, centerHorizLine + yOff);
/*
                int adjFieldWidth = fieldWidth - wordImageWidth;
                int adjFieldHeight = fieldHeight - wordImageHeight;

                float xOff = (float) r.nextGaussian();// * 0.2f;
                float yOff = (float) r.nextGaussian();// * 0.5f;
                yOff = (float)Math.pow(yOff, 3) * 1.5f;

                return new PVector(PApplet.map(xOff, -2, 2, 0, adjFieldWidth),
                                    PApplet.map(yOff, -2, 2, 0, adjFieldHeight));
*/
            }
        };
    }

    public static WordPlacer centerClump() {
        final Random r = new Random();
        final float stdev = 0.4f;

        return new WordPlacer() {

            public PVector place(Word word, int wordIndex, int wordsCount,
                    int wordImageWidth, int wordImageHeight, int fieldWidth, int fieldHeight) {
                return new PVector(getOneUnder(fieldWidth - wordImageWidth),
                        getOneUnder(fieldHeight - wordImageHeight));
            }

            private int getOneUnder(float upperLimit) {
                return PApplet.round(PApplet.map((float) r.nextGaussian()
                        * stdev, -2, 2, 0, upperLimit));
            }
        };
    }

    public static WordPlacer horizBandAnchoredLeft() {
        final Random r = new Random();
        return new WordPlacer() {
            public PVector place(Word word, int wordIndex, int wordsCount,
                    int wordImageWidth, int wordImageHeight, int fieldWidth,
                    int fieldHeight) {
                float x = (1 - word.weight) * fieldWidth * r.nextFloat(); // big=left, small=right
                float y = ((float) fieldHeight) * 0.5f;
                return new PVector(x, y);
            }
        };
    }

    public static WordPlacer swirl() {
        return new SwirlWordPlacer();
    }

    public static WordPlacer upperLeft() {
        return new UpperLeftWordPlacer();
    }

    public static WordPlacer wave() {
        return new WaveWordPlacer();
    }
}
