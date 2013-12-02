package wordcram;

import java.util.Random;

import processing.core.PVector;

public class RandomPlacer implements WordPlacer {

	Random random;

	public RandomPlacer() {
		random = new Random();
	}

	public PVector place(Word word, int wordIndex, int wordsCount,
			int wordImageWidth, int wordImageHeight, int fieldWidth,
			int fieldHeight) {
		float newX = randomBetween(0, fieldWidth);
		float newY = randomBetween(0, fieldHeight);
		return new PVector(newX, newY);
	}

	float randomBetween(float a, float b) {
		return a + random.nextFloat() * (b - a);
	}

}
