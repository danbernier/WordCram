package wordcram;

import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import processing.core.PVector;

public class ShapeBasedPlacer implements WordPlacer, WordNudger {

	public static boolean PRECISE = false;
	public static int GLYPH_SIZE = 500;
	public static int FONT_STYLE = Font.BOLD;

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

	public static ShapeBasedPlacer fromTextGlyphs(String text, String fontName) {
		Font font = new Font(fontName, FONT_STYLE, GLYPH_SIZE);

		WordShaper shaper = new WordShaper();
		Shape shape = shaper.getShapeFor(text, font, 0);
		return new ShapeBasedPlacer(shape);
	}

	public static ShapeBasedPlacer fromImageFile(String imageFilePath, Color color) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(imageFilePath));
		} catch (IOException e) {
			e.printStackTrace();
		}

		ImageShaper shaper = new ImageShaper();
		Shape shape = PRECISE ? shaper.shapePrecisely(image, color) : shaper.shapeSloppily(image, color);

		return new ShapeBasedPlacer(shape);
	}

	public PVector place(Word w, int rank, int count, int ww, int wh, int fw,
			int fh) {

		w.setProperty("width", ww);
		w.setProperty("height", wh);

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
		float ww = (Integer) word.getProperty("width");
		float wh = (Integer) word.getProperty("height");

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
