package wordcram;

import java.awt.*;
import java.awt.geom.Area;
import processing.core.PImage;
import java.util.ArrayList;

public class ImageShaper {

	public Shape shape(PImage image, int color) {
		RectTree tree = new RectTree(0, 0, image.width, image.height);
		return tree.toShape(image, color);
	}

	// TODO combine this somehow with BBTree(Builder). It's the same idea.
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
}
