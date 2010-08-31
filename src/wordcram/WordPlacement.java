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
	
	// TODO move the BBTreeBuilder into here?  We'll need to pass the Word, and the bgColor, which would be weird...
	public WordPlacement(Word _word, BBTree _bbTree) {
		word = _word;
		bbTree = _bbTree;
	}
	
	public Word getWord() {
		return word;
	}
	
	public boolean overlaps(WordPlacement other) {
		return overlaps(bbTree, word.getLocation(), other.bbTree, other.getWord().getLocation());
	}
	

	
	private boolean overlaps(BBTree a, PVector av, BBTree b, PVector bv) {
		  
		  if (rectCollide(a.x1+av.x, a.y1+av.y, a.x2+av.x, a.y2+av.y, 
		                  b.x1+bv.x, b.y1+bv.y, b.x2+bv.x, b.y2+bv.y))
		  {
		    if (a.isLeaf() && b.isLeaf()) { return true; }
		    
		    if (a.isLeaf()) {
		      for (int bi = 0; bi < b.kids.length; bi++) {
		        if (overlaps(a, av, b.kids[bi], bv)) {
		          return true;
		        }
		      }
		      return false;  // a is leaf, but doesn't overlap w/ any b.kids
		    }
		    
		    if (b.isLeaf()) {
		      for (int ai = 0; ai < a.kids.length; ai++) {
		        if (overlaps(b, bv, a.kids[ai], av)) {
		          return true;
		        }
		      }
		      return false;  // b is leaf, but doesn't overlap w/ any a.kids
		    }
		    
		    // now, we know NEITHER a & b are leaves
		    for (int ai = 0; ai < a.kids.length; ai++) {
		      if (overlaps(b, bv, a.kids[ai], av)) {
		        return true;
		      }
		    }
		  }
		  return false;
		}

		private static boolean rectCollide(float ax1, float ay1, float ax2, float ay2,
		                    float bx1, float by1, float bx2, float by2) {
		  return ay2 > by1 &&
		         ay1 < by2 &&
		         ax2 > bx1 &&
		         ax1 < bx2;
		}
}
