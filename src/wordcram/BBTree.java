package wordcram;

import java.util.ArrayList;

import processing.core.PVector;

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
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	private BBTree[] kids;

	private PVector location = new PVector(0, 0);

	BBTree(int _x1, int _y1, int _x2, int _y2) {
		x1 = _x1;
		y1 = _y1;
		x2 = _x2;
		y2 = _y2;
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

	public BBTree[] getKids() {
		return kids;
	}

	public void setLocation(PVector _location) {
		location = _location;
		if (!isLeaf()) {
			for (BBTree kid : kids) {
				kid.setLocation(_location);
			}
		}
	}

	public PVector[] getPoints() {
		return new PVector[] { PVector.add(new PVector(x1, y1), location),
				PVector.add(new PVector(x2, y2), location) };
	}

	public boolean overlaps(BBTree b) {

		if (rectCollide(this, b)) {
			if (this.isLeaf() && b.isLeaf()) {
				return true;
			}

			if (this.isLeaf()) {
				for (BBTree bKid : b.getKids()) {
					if (this.overlaps(bKid)) {
						return true;
					}
				}
				return false; // this is leaf, but doesn't overlap w/ any b.kids
			}

			if (b.isLeaf()) {
				for (BBTree myKid : this.getKids()) {
					if (b.overlaps(myKid)) {
						return true;
					}
				}
				return false; // b is leaf, but doesn't overlap w/ any this.kids
			}

			// now, we know NEITHER this or b are leaves
			// Hmm...just noticed this for-loop is JUST LIKE the if(b.isLeaf())
			// one above. TODO fix that.
			for (BBTree myKid : this.getKids()) {
				if (b.overlaps(myKid)) {
					return true;
				}
			}
		}
		return false;
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
			x1 -= extra;
			x2 += extra;
			y1 -= extra;
			y2 += extra;
		} else {
			for (int i = 0; i < kids.length; i++) {
				kids[i].swellLeaves(extra);
			}
		}
	}
}
