package wordcram;

import processing.core.*;

public class SwirlWordPlacer implements WordPlacer {

    public PVector place(Word word, int wordIndex, int wordsCount,
            int wordImageWidth, int wordImageHeight, int fieldWidth,
            int fieldHeight) {

        float normalizedIndex = (float) wordIndex / wordsCount;

        float theta = normalizedIndex * 6 * PConstants.TWO_PI;
        float radius = normalizedIndex * fieldWidth / 2f;

        float centerX = fieldWidth * 0.5f;
        float centerY = fieldHeight * 0.5f;

        float x = PApplet.cos(theta) * radius;
        float y = PApplet.sin(theta) * radius;

        return new PVector(centerX + x, centerY + y);
    }
}
