package wordcram;

/*
 Copyright 2010 Daniel Bernier

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import processing.core.PImage;

class BBTreeBuilder {
	public BBTree makeTree(Shape shape, int minBoxSize) {
		Rectangle2D bounds = shape.getBounds2D();
		int x = (int) bounds.getX();
		int y = (int) bounds.getY();
		int x2 = x + (int) bounds.getWidth();
		int y2 = y + (int) bounds.getHeight();
		return makeTree(shape, minBoxSize, x, y, x2, y2);
	}

	private BBTree makeTree(Shape shape, int minBoxSize, int x1, int y1,
			int x2, int y2) {

		boolean intersects = shape.intersects(x1, y1, x2 - x1, y2 - y1);
		boolean contains = shape.contains(x1, y1, x2 - x1, y2 - y1);

		if (!intersects && !contains) {
			return null;
		} else {
			BBTree tree = new BBTree(x1, y1, x2, y2);
			if (!contains) {
				boolean smallEnoughToStop = x2 - x1 <= minBoxSize;
				if (!smallEnoughToStop) {
					int newX = avg(x1, x2);
					int newY = avg(y1, y2);

					// upper left
					BBTree t0 = makeTree(shape, minBoxSize, x1, y1, newX, newY);
					// upper right
					BBTree t1 = makeTree(shape, minBoxSize, newX, y1, x2, newY);
					// lower left
					BBTree t2 = makeTree(shape, minBoxSize, x1, newY, newX, y2);
					// lower right
					BBTree t3 = makeTree(shape, minBoxSize, newX, newY, x2, y2);

					tree.addKids(t0, t1, t2, t3);
				}
			}

			return tree;
		}
	}
		  
		  
	public BBTree makeTree(PImage buffer, int bgColor, int minBoxSize) {
		return makeTree(buffer, bgColor, minBoxSize, 0, 0, buffer.width,
				buffer.height);
	}

	private BBTree makeTree(PImage buffer, int bgColor, int minBoxSize, int x1,
			int y1, int x2, int y2) {

		int colorInfo = getColorInfo(buffer, bgColor, x1, y1, x2, y2);

		// boolean hasBgColor = (colorInfo & 1) > 0;
		// boolean hasFill = (colorInfo & 2) > 0;
		boolean hasBgColor = colorInfo == 3 || colorInfo == 1;
		boolean hasFill = colorInfo == 3 || colorInfo == 2;

		if (!hasFill) {
			return null;
		} else {
			BBTree tree = new BBTree(x1, y1, x2, y2);
			if (hasBgColor) {
				boolean smallEnoughToStop = x2 - x1 <= minBoxSize;
				if (!smallEnoughToStop) {
					int newX = avg(x1, x2);
					int newY = avg(y1, y2);

					// upper left
					BBTree t0 = makeTree(buffer, bgColor, minBoxSize, x1, y1,
							newX, newY);
					// upper right
					BBTree t1 = makeTree(buffer, bgColor, minBoxSize, newX, y1,
							x2, newY);
					// lower left
					BBTree t2 = makeTree(buffer, bgColor, minBoxSize, x1, newY,
							newX, y2);
					// lower right
					BBTree t3 = makeTree(buffer, bgColor, minBoxSize, newX,
							newY, x2, y2);

					tree.addKids(t0, t1, t2, t3);
				}
			}

			return tree;
		}
	}

	private int avg(int a, int b) {
		// reminder: x >> 1 == x / 2
		// avg = (a+b)/2 = (a/2)+(b/2) = (a>>1)+(b>>1)
		return (a >> 1) + (b >> 1);
	}

	/*
	 * Has one job: find whether the region contains a) any bgColor, and b) any
	 * OTHER color. If it has NO bgColor, we can stop recursing. If it has ONLY
	 * bgColor, we can stop recursing. Returns a 2-bit int, basically: lowest
	 * bit being hasBgColor, highest bit being hasOtherColor: 0 = neither
	 * (impossible, really) 1 = has bgColor only 2 = has no bgColor, only other
	 * color(s) 3 = has both bgColor and some other color
	 */
	private int getColorInfo(PImage buffer, int bgColor, int x1, int y1,
			int x2, int y2) {

		boolean seenBgColor = false;
		boolean seenOtherColor = false;

		int yOff = y1 * buffer.width;

		for (int y = y1; y < y2; y++) {
			int xStart = yOff + x1;
			int xStop = yOff + x2;
			for (int i = xStart; i < xStop; i++) {
				if (buffer.pixels[i] == bgColor) {
					seenBgColor = true;
				} else {
					seenOtherColor = true;
				}
				if (seenBgColor && seenOtherColor) {
					return 3; // short-circuit
				}
			}
			yOff += buffer.width;
		}

		// last case should never happen
		return seenBgColor ? 1 : seenOtherColor ? 2 : 0;
	}
}
