package wordcram;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import processing.core.PImage;
import java.util.ArrayList;

public class ImageShaper {

	public static int TOLERANCE = 5;

	public Shape shape(PImage image, int color) {
		RectTree tree = new RectTree(0, 0, image.width, image.height);
		return tree.toShape(image, color);
	}


	class RectTree {
		ArrayList<RectTree> kids = null;

		int left; int top; int right; int bottom;
		int width; int height;
		RectTree(int l, int t, int r, int b) {
			left = l;
			top = t;
			right = r;
			bottom = b;

			width = right - left;
			height = bottom - top;  // Yep: upside-down.

			split();
		}

		private void split() {

			/*
			Saying width < 2 OR height < 2 means we miss a few odd pixels.
			Saying width < 2 AND height < 2 means we get them, but it goes a bit slower.
			For now, we'll go with faster.
			*/
			if (width < 2 || height < 2) return;

			int centerX = avg(left, right);
			int centerY = avg(top, bottom);
			kids = new ArrayList<RectTree>();
			kids.add(new RectTree(left, top, centerX, centerY));
			kids.add(new RectTree(centerX, top, right, centerY));
			kids.add(new RectTree(left, centerY, centerX, bottom));
			kids.add(new RectTree(centerX, centerY, right, bottom));
		}

		private int avg(int a, int b) {
			// reminder: x >> 1 == x / 2
			// avg = (a+b)/2 = (a/2)+(b/2) = (a>>1)+(b>>1)
			return (a + b) >> 1;
		}

		Shape toShape(PImage img, int color) {
			Area area = new Area();
			if (isAllCovered(img, color)) {
				area.add(new Area(new Rectangle(left, top, width, height)));
			}
			else if (kids != null) {
				for (RectTree kid : kids) {
					area.add(new Area(kid.toShape(img, color)));
				}
			}
			return area;
		}

		private Boolean isAllCoveredMemo;
		private boolean isAllCovered(PImage img, int color) {
			if (isAllCoveredMemo == null) {
				if (kids == null) {
					isAllCoveredMemo = (img.get(left, top) == color);
				}
				else {
					// isAllCoveredMemo = kids.all?(&:isAllCovered);
					isAllCoveredMemo = true;
					for (RectTree kid : kids) {
						if (!kid.isAllCovered(img, color)) {
							isAllCoveredMemo = false;
							break;
						}
					}
				}
			}
			return isAllCoveredMemo;
		}
	}











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
