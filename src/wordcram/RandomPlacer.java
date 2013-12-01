package wordcram;

import java.util.Random;

import processing.core.PVector;

public class RandomPlacer extends AbstractPlacer implements WordPlacer {

	Random random;

	public RandomPlacer() {
		random = new Random();
	}

	public PVector place(Word word, int wordIndex, int wordsCount,
			int wordImageWidth, int wordImageHeight, int fieldWidth,
			int fieldHeight) {
		for (int i = 0; i < 1000; i++) {
			float newX = randomBetween(0, fieldWidth);
			float newY = randomBetween(0, fieldHeight);
			word.setProperty("x", newX);
			word.setProperty("y", newY);
			if (filter.canFit(word)) {
				return new PVector(newX, newY);
			}
		}

		return new PVector(-1, -1);
	}

	float randomBetween(float a, float b) {
		return a + random.nextFloat() * (b - a);
	}

}
