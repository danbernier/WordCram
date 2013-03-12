package wordcram;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;

public class ImageShaper {
	
	public static int TOLERANCE = 5;
	
	public Shape shapePrecisely(BufferedImage image, Color color) {
		
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
		return area;
	}
	
	public Shape shapeSloppily(BufferedImage image, Color color) {
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
		return area;
	}
	
	private boolean isIncluded(Color target, Color pixel) {
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
