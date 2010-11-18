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

import java.awt.*;
import java.awt.geom.*;
import java.util.Arrays;

import processing.core.*;

class WordCramEngine {

	// PApplet parent is only for 2 things: to get its PGraphics g (aka destination), and 
	// for createGraphics, for drawing the words.  host should be used for nothing else.
	private PApplet parent;
	private PGraphics destination;
	
	private WordFonter fonter;
	private WordSizer sizer;
	private WordColorer colorer;
	private WordAngler angler;
	private WordPlacer placer;
	private WordNudger nudger;

	private BBTreeBuilder bbTreeBuilder;
	private WordShaper wordShaper;
	
	private Word[] words;
	private Shape[] shapes;
	private int wordIndex;
	
	private Timer timer = new Timer();

	public WordCramEngine(PApplet parent, Word[] words, WordFonter fonter, WordSizer sizer, WordColorer colorer, WordAngler angler, WordPlacer placer, WordNudger nudger) {
		this.parent = parent;
		this.destination = parent.g;
		
		this.words = words;
		this.fonter = fonter;
		this.sizer = sizer;
		this.colorer = colorer;
		this.angler = angler;
		this.placer = placer;
		this.nudger = nudger;
		
		this.bbTreeBuilder = new BBTreeBuilder();
		this.wordShaper = new WordShaper(this.sizer, this.fonter, this.angler);
		
		renderWordsToShapes();
		makeBBTreesFromShapes();
	}
	
	private void renderWordsToShapes() {
		this.shapes = wordShaper.shapeWords(this.words); // ONLY returns shapes for words that are big enough to see
		this.words = Arrays.copyOf(words, shapes.length);  // Trim down the list of words
		this.wordIndex = -1;
	}
	
	private void makeBBTreesFromShapes() {
		for (int i = 0; i < this.shapes.length; i++) {
			Word word = this.words[i];
			Shape shape = this.shapes[i];
			word.setBBTree(bbTreeBuilder.makeTree(shape, 7));  // TODO extract config setting for minBoundingBox, and add swelling option
		}
	}
	
	public boolean hasMore() {
		return wordIndex < words.length-1;
	}
	
	public void drawAll() {
		timer.start("drawAll");
		while(hasMore()) {
			drawNext();
		}
		timer.end("drawAll");
		//System.out.println(timer.report());
	}
	
	public void drawNext() {
		if (!hasMore()) return;
		
		Word word = words[++wordIndex];
		Shape wordShape = shapes[wordIndex];

		timer.start("placeWord");
		PVector wordLocation = placeWord(word, wordShape);
		timer.end("placeWord");
			
		if (wordLocation != null) {
			timer.start("drawWordImage");
			drawWordImage(word, wordShape, wordLocation);
			timer.end("drawWordImage");
		}
		else {
			//System.out.println("couldn't place: " + word.word + ", " + word.weight);
		}	
	}	
	
	private PVector placeWord(Word word, Shape wordShape) {
		// TODO does it make sense to COMBINE wordplacer & wordnudger, the way you (sort of) orig. had it?  i think it does...
		Rectangle2D rect = wordShape.getBounds2D();		
		int wordImageWidth = (int)rect.getWidth();
		int wordImageHeight = (int)rect.getHeight();
		
		word.setDesiredLocation(placer.place(word, wordIndex, words.length, wordImageWidth, wordImageHeight, destination.width, destination.height));
		
		// TODO just make this 10000
		// TODO make this a config!!!  that'll help people write their own nudgers, if they know how many times it'll try -- also, it'll help tweak performance
		int maxAttempts = (int)((1.0-word.weight) * 600) + 100;
		Word lastCollidedWith = null;
		for (int attempt = 0; attempt < maxAttempts; attempt++) {

			word.nudge(nudger.nudgeFor(word, attempt));
			
			if (lastCollidedWith != null && word.overlaps(lastCollidedWith)) {
				timer.count("CACHE COLLISION");
				continue;
			}
			
			PVector loc = word.getLocation();
			if (loc.x < 0 || loc.y < 0 || loc.x + wordImageWidth >= destination.width || loc.y + wordImageHeight >= destination.height) {
				timer.count("OUT OF BOUNDS");
				continue;
			}
			
			boolean foundOverlap = false;
			for (int i = 0; !foundOverlap && i < wordIndex; i++) {
				Word otherWord = words[i];
				if (word.overlaps(otherWord)) {
					foundOverlap = true;
					lastCollidedWith = otherWord;
				}
			}
			
			if (!foundOverlap) {
				timer.count("placed a word");
				return word.getLocation();
			}
		}
		
		timer.count("couldn't place a word");
		return null;
	}
	
	private void drawWordImage(Word word, Shape wordShape, PVector location) {
		
		Path2D.Float path2d = new Path2D.Float(wordShape, AffineTransform.getTranslateInstance(location.x, location.y));
		//wordShape = AffineTransform.getTranslateInstance(location.x, location.y).createTransformedShape(wordShape);
			
		boolean drawToParent = false;
			
		Graphics2D g2 = (Graphics2D)(drawToParent ? parent.getGraphics() : destination.image.getGraphics());
			
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setPaint(new Color(colorer.colorFor(word), true));
		g2.fill(path2d);
		
//		destination.pushStyle();
//		destination.stroke(30, 255, 255, 50);
//		destination.noFill();
//		word.getBBTree().draw(destination);
//		destination.rect(location.x, location.y, wordImage.width, wordImage.height);
//		destination.popStyle();
	}
	

	
	
	public Word currentWord() {
		return hasMore() ? words[wordIndex] : null;
	}
	public int currentWordIndex() {
		return wordIndex;
	}
}
