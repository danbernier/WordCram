package wordcram;

import java.awt.Color;

import processing.core.PImage;

public class ImageShapeAdapter implements ShapeForBBTree {

	int[] pixels;
	int width;
	int height;

	public ImageShapeAdapter(PImage img) {
		img.loadPixels();
		this.pixels = img.pixels;
		this.width = img.width;
		this.height = img.height;
	}

	public boolean contains(float x, float y, float w, float h) {
		if (outsideImageBounds(x, y, w, h))
			return false;

		for (int m = (int) x; m < x + w; m++) {
			for (int n = (int) y; n < y + h; n++) {
				if (brightness(colorAt(m, n)) > 0.5) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean intersects(float x, float y, float w, float h) {
		if (outsideImageBounds(x, y, w, h))
			return false;

		for (int m = (int) x; m < x + w; m++) {
			for (int n = (int) y; n < y + h; n++) {
				if (brightness(colorAt(m, n)) < 0.5) {
					return true;
				}
			}
		}
		return false;
	}

	public int getLeft() {
		return 0;
	}

	public int getTop() {
		return 0;
	}

	public int getRight() {
		return this.width;
	}

	public int getBottom() {
		return this.height;
	}


	private boolean outsideImageBounds(float x, float y, float w, float h) {
		return x + w > this.width || y + h > this.height || x < 0 || y < 0 || w < 0 || h < 0;
	}

	private int colorAt(int x, int y) {
		return pixels[x + (y * width)];
	}

	private float brightness(int color) {
		int red = color >> 16 & 0xFF;
		int green = color >> 8 & 0xFF;
		int blue = color & 0xFF;
		
		return Color.RGBtoHSB(red, green, blue, null)[2];
	}
}
