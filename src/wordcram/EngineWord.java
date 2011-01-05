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
import java.awt.geom.AffineTransform;

import processing.core.PFont;
import processing.core.PVector;

class EngineWord {
	Word word;
	int rank;
	
	/* TODO Encapsulate these. Have the methods a) set a property on the Word, so people using 
	 * getWordAt(x,y) can SEE the Word's final outcome, and b) have them first LOOK for those 
	 * properties, before calling the component, so people can just say 
	 * Word.setProperty("size", 24).setProperty("angle", radians(20)), and it'll come out that way.
	 * See setDesiredLocation(), below.
	 * 
	 * (At that point, might want to move more of this stuff back INTO Word. So they can even
	 * iterate over their original Word[], and see interesting stuff.)
	 */
	private float size;
	private float angle;
	private PFont font;
	private int color;

	private Shape shape;
	private BBTree bbTree;

	private PVector desiredLocation;
	private PVector currentLocation;
	private boolean wasPlaced = false;

	EngineWord(Word word, int rank, int wordCount, WordSizer sizer, WordAngler angler, WordFonter fonter, WordColorer colorer) {
		this.word = word;
		this.rank = rank;
		
		Object size = word.getProperty("size");
		this.size = size != null ? Float.valueOf(size.toString()) : sizer.sizeFor(word, rank, wordCount);
		word.setProperty("size", this.size);
		
		Object angle = this.word.getProperty("angle");
		this.angle = angle != null ? Float.valueOf(angle.toString()) : angler.angleFor(this.word);
		word.setProperty("angle", this.angle);
		
		Object font = this.word.getProperty("font");
		this.font = font != null ? (PFont)font : fonter.fontFor(this.word);
		word.setProperty("font", this.font);
		
		Object color = this.word.getProperty("color");
		this.color = color != null ? (Integer)color : colorer.colorFor(this.word);
		word.setProperty("color", this.color);
	}

	void setShape(Shape shape) {
		this.shape = shape;

		// TODO extract config setting for minBoundingBox, and add swelling
		// option
		// TODO try perf-testing smaller bounding boxes -- if it's not slower,
		// it could make better images
		this.bbTree = new BBTreeBuilder().makeTree(shape, 7);
	}

	Shape getShape() {
		return shape;
	}

	boolean overlaps(EngineWord other) {
		return bbTree.overlaps(other.bbTree);
	}

	void setDesiredLocation(PVector loc) {
		Object placeProperty = word.getProperty("place");
		if (placeProperty != null) {
			loc = (PVector)placeProperty;
		}
		word.setProperty("place", loc.get());
		
		desiredLocation = loc.get();
		currentLocation = loc.get();
	}

	void nudge(PVector nudge) {
		currentLocation = PVector.add(desiredLocation, nudge);
		bbTree.setLocation(currentLocation.get());
	}

	void finalizeLocation() {
		AffineTransform transform = AffineTransform.getTranslateInstance(
				currentLocation.x, currentLocation.y);
		shape = transform.createTransformedShape(shape);
		bbTree.setLocation(currentLocation);
		wasPlaced = true;
		word.setProperty("finalPlace", currentLocation);
	}

	PVector getCurrentLocation() {
		return currentLocation.get();
	}
	
	boolean wasPlaced() {
		return this.wasPlaced;
	}

	
	float getSize() {
		return size;
	}

	float getAngle() {
		return angle;
	}

	PFont getFont() {
		return font;
	}

	int getColor() {
		return color;
	}
}
