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

import processing.core.PFont;
import processing.core.PVector;

class EngineWord {
	Word word;
	int rank;
	float size;
	float angle;
	PFont font;
	int color;
	Shape shape;
	
	private BBTree bbTree;
	private PVector desiredLocation;
	private PVector currentLocation;
	
	EngineWord(Word word) {
		this.word = word;
	}
	
	void setBBTree(BBTree bbTree) {
		this.bbTree = bbTree;
	}
	BBTree getBBTree() {
		return bbTree;
	}

	boolean overlaps(EngineWord other) {
		return bbTree.overlaps(other.bbTree);
	}
	
	void setDesiredLocation(PVector loc) {
		word.setProperty("_desiredLocation", loc); // TODO resolve. This is only there for PlottingWordNudger.
		desiredLocation = new PVector(loc.x, loc.y);
		currentLocation = new PVector(loc.x, loc.y);
	}
	void nudge(PVector nudge) {
		currentLocation = PVector.add(desiredLocation, nudge);
		bbTree.setLocation(currentLocation.get());
	}
	PVector getLocation() {
		return currentLocation.get();
	}
}
