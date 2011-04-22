package wordcram;

import java.awt.Rectangle;
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
	private Rectangle rect;
	private BBTree[] kids;

	BBTree(int x, int y, int width, int height) {
		rect = new Rectangle(x, y, width, height);
	}

	void addKids(BBTree... _kids) {
		ArrayList<BBTree> kidList = new ArrayList<BBTree>();
		for (BBTree kid : _kids) {
			if (kid != null) {
				kidList.add(kid);
			}
		}

		kids = kidList.toArray(new BBTree[0]);
	}

	void setLocation(PVector location) {
		int dx = (int)location.x - rect.x;
		int dy = (int)location.y - rect.y;
		translate(dx, dy);
	}
	
	private void translate(int dx, int dy) {
		rect.translate(dx, dy);
		if (!isLeaf()) {
			for (BBTree kid : kids) {
				kid.translate(dx, dy);
			}
		}
	}

	boolean overlaps(BBTree other) {
		if (this.rect.intersects(other.rect)) {
			if (this.isLeaf() && other.isLeaf()) {
				return true;
			}
			else if (this.isLeaf()) {
				for (BBTree otherKid : other.kids) {
					if (this.overlaps(otherKid)) {
						return true;
					}
				}
			}
			else {
				for (BBTree myKid : this.kids) {
					if (other.overlaps(myKid)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	boolean isLeaf() {
		return kids == null;
	}

	void swell(int extra) {
		rect.grow(extra, extra);
		if (!isLeaf()) {
			for (BBTree kid : kids) {
				kid.swell(extra);
			}
		}
	}

	void draw(PGraphics g) {
		g.pushStyle();
		g.noFill();
	
		g.stroke(30, 255, 255, 50);
		drawLeaves(g);
			
		g.popStyle();
	}

	private void drawLeaves(PGraphics g) {
		if (this.isLeaf()) {
			g.rect(rect.x, rect.y, rect.width, rect.height);
		} else {
			for (BBTree kid : kids) {
				kid.drawLeaves(g);
			}
		}
	}
}
