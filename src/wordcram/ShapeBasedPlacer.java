package wordcram;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import processing.core.PVector;

class ShapeBasedPlacer implements WordPlacer, WordNudger {

	public static int TOLERANCE = 5;
	public static boolean PRECISE = false;
	public static int GLYPH_SIZE = 500;
	public static int FONT_STYLE = Font.BOLD;

	Area area;
	float minX;
	float minY;
	float maxX;
	float maxY;
	Random random;
	BufferedImage image = null;

	public ShapeBasedPlacer(Shape shape) {
		this.area = new Area(shape);
		init();
	}

	private void init() {
		random = new Random();
		Rectangle2D areaBounds = area.getBounds2D();
		this.minX = (float) areaBounds.getMinX();
		this.minY = (float) areaBounds.getMinY();
		this.maxX = (float) areaBounds.getMaxX();
		this.maxY = (float) areaBounds.getMaxY();
	}
	
	public static ShapeBasedPlacer fromTextGlyphs(String text, String fontName) {
		Font font = new Font(fontName, FONT_STYLE, GLYPH_SIZE);
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		FontRenderContext frc = img.createGraphics().getFontRenderContext();
		GlyphVector glyphVector = font.createGlyphVector(frc, text);
		return new ShapeBasedPlacer(glyphVector.getOutline(GLYPH_SIZE / 10, GLYPH_SIZE));
	}
	
	private ShapeBasedPlacer(BufferedImage image) {
		this.image = image;
	}

	public static ShapeBasedPlacer fromFile(String path, Color color) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ShapeBasedPlacer result = new ShapeBasedPlacer(image);
		if (PRECISE) {
			result.fromImagePrecise(color);
		} else {
			result.fromImageSloppy(color);
		}
		result.init();
		return result;
	}

	private void fromImagePrecise(Color color) {
		Area area = new Area();
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				Color pixel = new Color(image.getRGB(x, y));
				if (isIncluded(color, pixel)) {
					Rectangle r = new Rectangle(x, y, 1, 1);
					area.add(new Area(r));
				}
			}
		}
		this.area = area;
	}

	private void fromImageSloppy(Color color) {
		Area area = new Area();
		Rectangle r;
		int y1, y2;
		for (int x = 0; x < image.getWidth(); x++) {
			y1 = 99;
			y2 = -1;
			for (int y = 0; y < image.getHeight(); y++) {
				Color pixel = new Color(image.getRGB(x, y));
				if (isIncluded(color, pixel)) {
					if (y1 == 99) {
						y1 = y;
						y2 = y;
					}
					if (y > (y2 + 1)) {
						r = new Rectangle(x, y1, 1, y2 - y1);
						area.add(new Area(r));
						y1 = y;
						y2 = y;
					}
					y2 = y;
				}
			}
			if ((y2 - y1) >= 0) {
				r = new Rectangle(x, y1, 1, y2 - y1);
				area.add(new Area(r));
			}
		}
		this.area = area;
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

	boolean isIncluded(Color target, Color pixel) {
		int rT = target.getRed();
		int gT = target.getGreen();
		int bT = target.getBlue();
		int rP = pixel.getRed();
		int gP = pixel.getGreen();
		int bP = pixel.getBlue();
		return ((rP - TOLERANCE <= rT) && (rT <= rP + TOLERANCE)
				&& (gP - TOLERANCE <= gT) && (gT <= gP + TOLERANCE)
				&& (bP - TOLERANCE <= bT) && (bT <= bP + TOLERANCE));
	}
}
