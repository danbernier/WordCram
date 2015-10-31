package wordcram;

import java.awt.Shape; // awt: passed as arg, turned into Area
import java.awt.geom.Area; // awt: from Shape, getBounds2D(), contains(box)
import java.awt.geom.Rectangle2D; // awt: from Area, getMinX() and Y(), getMaxX() and Y()
import java.util.Random;
import processing.core.PVector;

public class ShapeBasedPlacer implements WordPlacer, WordNudger {

	Area area;
	float minX;
	float minY;
	float maxX;
	float maxY;
	Random random;

	public ShapeBasedPlacer(Shape shape) {
		this.area = new Area(shape);
		random = new Random();
		Rectangle2D areaBounds = area.getBounds2D();
		this.minX = (float) areaBounds.getMinX();
		this.minY = (float) areaBounds.getMinY();
		this.maxX = (float) areaBounds.getMaxX();
		this.maxY = (float) areaBounds.getMaxY();
	}

	public PVector place(Word w, int rank, int count, int ww, int wh, int fw,
			int fh) {

		for (int i = 0; i < 1000; i++) {
			float newX = randomBetween(minX, maxX);
			float newY = randomBetween(minY, maxY);
			if (area.contains(newX, newY, ww, wh)) {
				return new PVector(newX, newY);
			}
		}

		return new PVector(-1, -1);
	}

	public PVector nudgeFor(Word word, int attempt) {
		PVector target = word.getTargetPlace();
		float wx = target.x;
		float wy = target.y;
		float ww = word.getRenderedWidth();
		float wh = word.getRenderedHeight();

		for (int i = 0; i < 1000; i++) {
			float newX = randomBetween(minX, maxX);
			float newY = randomBetween(minY, maxY);

			if (area.contains(newX, newY, ww, wh)) {
				return new PVector(newX - wx, newY - wy);
			}
		}

		return new PVector(-1, -1);
	}

	float randomBetween(float a, float b) {
		return a + random.nextFloat() * (b - a);
	}
}
