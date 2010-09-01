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

import processing.core.PVector;

public class WordPlacement {
	private Word word;
	private BBTree bbTree;

	// TODO move the BBTreeBuilder into here? We'll need to pass the Word, and
	// the bgColor, which would be weird...
	public WordPlacement(Word _word, BBTree _bbTree) {
		word = _word;
		bbTree = _bbTree;
	}

	public Word getWord() {
		return word;
	}

	public boolean overlaps(WordPlacement other) {
		bbTree.setLocation(word.getLocation());
		other.bbTree.setLocation(other.getWord().getLocation());
		return overlaps(bbTree, other.bbTree);
	}

	private boolean overlaps(BBTree a, BBTree b) {

		if (rectCollide(a, b)) {
			if (a.isLeaf() && b.isLeaf()) {
				return true;
			}

			if (a.isLeaf()) {
				for (BBTree bKid : b.getKids()) {
					if (overlaps(a, bKid)) {
						return true;
					}
				}
				return false; // a is leaf, but doesn't overlap w/ any b.kids
			}

			if (b.isLeaf()) {
				for (BBTree aKid : a.getKids()) {
					if (overlaps(b, aKid)) {
						return true;
					}
				}
				return false; // b is leaf, but doesn't overlap w/ any a.kids
			}

			// now, we know NEITHER a & b are leaves
			// Hmm...just noticed this for-loop is JUST LIKE the if(b.isLeaf())
			// one above. TODO fix that.
			for (BBTree aKid : a.getKids()) {
				if (overlaps(b, aKid)) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean rectCollide(BBTree a, BBTree b) {
		PVector[] aPoints = a.getPoints();
		PVector[] bPoints = b.getPoints();
		PVector aTopLeft = aPoints[0];
		PVector aBottomRight = aPoints[1];
		PVector bTopLeft = bPoints[0];
		PVector bBottomRight = bPoints[1];

		return aBottomRight.y > bTopLeft.y && aTopLeft.y < bBottomRight.y
				&& aBottomRight.x > bTopLeft.x && aTopLeft.x < bBottomRight.x;
	}
}
