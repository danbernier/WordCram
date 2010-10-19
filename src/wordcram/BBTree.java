package wordcram;

import java.util.ArrayList;

import processing.core.*;

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

class BBTree {
	private int left;
	private int top;
	private int right;
	private int bottom;
	private BBTree[] kids;

	private PVector location = new PVector(0, 0);

	BBTree(int _left, int _top, int _right, int _bottom) {
		left = _left;
		top = _top;
		right = _right;
		bottom = _bottom;
	}

	public void addKids(BBTree... _kids) {
		ArrayList<BBTree> kidList = new ArrayList<BBTree>();
		for (BBTree kid : _kids) {
			if (kid != null) {
				kidList.add(kid);
			}
		}

		kids = kidList.toArray(new BBTree[0]);
	}

	public void setLocation(PVector _location) {
		location = _location;
		if (!isLeaf()) {
			for (BBTree kid : kids) {
				kid.setLocation(_location);
			}
		}
	}

	private BBTree[] getKids() {
		return kids;
	}

	public boolean overlaps(BBTree otherTree) {

		if (rectCollide(this, otherTree)) {
			if (this.isLeaf() && otherTree.isLeaf()) {
				return true;
			}

			if (this.isLeaf()) {  // Then otherTree isn't a leaf.
				for (BBTree otherKid : otherTree.getKids()) {
					if (this.overlaps(otherKid)) {
						return true;
					}
				}
				return false; // This isLeaf, but doesn't overlap w/ any otherTree's kids.
			}
			
			// Now we know that neither this nor otherTree are leaves.
			for (BBTree myKid : this.getKids()) {
				if (otherTree.overlaps(myKid)) {
					return true;
				}
			}
		}
		return false;
	}

	private PVector[] getPoints() {
		return new PVector[] { PVector.add(new PVector(left, top), location),
				PVector.add(new PVector(right, bottom), location) };
	}

	private boolean rectCollide(BBTree a, BBTree b) {
		PVector[] aPoints = a.getPoints();
		PVector[] bPoints = b.getPoints();
		PVector aTopLeft = aPoints[0];
		PVector aBottomRight = aPoints[1];
		PVector bTopLeft = bPoints[0];
		PVector bBottomRight = bPoints[1];

		return aBottomRight.y > bTopLeft.y && aTopLeft.y < bBottomRight.y
				&& aBottomRight.x > bTopLeft.x && aTopLeft.x < bBottomRight.x;
	}

	public boolean isLeaf() {
		return kids == null;
	}

	void swellLeaves(int extra) {
		if (isLeaf()) {
			left -= extra;
			right += extra;
			top -= extra;
			bottom += extra;
		} else {
			for (int i = 0; i < kids.length; i++) {
				kids[i].swellLeaves(extra);
			}
		}
	}

	void draw(PGraphics g) {
		g.pushStyle();
		g.rectMode(PConstants.CORNERS);
		g.noFill();
	
		g.stroke(30, 255, 255, 50);
		drawLeaves(g);
			
		g.popStyle();
	}

	private void drawLeaves(PGraphics g) {
		if (this.isLeaf()) {
			drawBounds(g, getPoints());
		} else {
			for (int i = 0; i < kids.length; i++) {
				kids[i].drawLeaves(g);
			}
		}
	}

	private void drawBounds(PGraphics g, PVector[] rect) {
		g.rect(rect[0].x, rect[0].y, rect[1].x, rect[1].y);
	}
}
