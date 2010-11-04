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
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.*;
import java.util.ArrayList;
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
	private FontRenderContext frc;
	
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
		this.frc = new FontRenderContext(null, true, true);
		
		renderWordsToShapes();
	}	
	
	private void renderWordsToShapes() {
		this.shapes = wordsToShapes(); // ONLY returns shapes for words that are big enough to see
		this.words = Arrays.copyOf(words, shapes.length);  // Trim down the list of words
		this.wordIndex = -1;
	}
	
	/*
	 * TODO question here: you want to eliminate as many words as possible, so FIRST rip through & render all their shapes,
	 * and stop once the shapes are too small.  Then you can shorten the arrays, and loop through less.
	 * This is also good because now, your WordPlacers will have better ranks to go on: if 75% of the words are too small
	 * to render, then the lowest word will have a 25th-percentile rank, and it'll place them in only (eh) 25% of the 
	 * field.  Basically, it's like you're lying to the Placer.  Cutting down the list first will give you a better
	 * answer to "how many words am i drawing here?".
	 * 
	 * BUT: won't that screw with your weights?  Maybe?  Er, maybe not?  Not sure. 
	 */
	private Shape[] wordsToShapes() {
		
		ArrayList<Shape> shapes = new ArrayList<Shape>();
		
		for (int i = 0; i < words.length; i++) {			
			Word word = words[i];
			float size = sizer.sizeFor(word, i, words.length);
			PFont pFont = fonter.fontFor(word);
			float rotation = angler.angleFor(word);		

			timer.start("wordToShape");
			Shape wordShape = wordToShape(word, size, pFont, rotation);
			timer.end("wordToShape");
			if (wordShape == null) break;
			shapes.add(wordShape);
		}
		
		return shapes.toArray(new Shape[0]);
	}

	private Shape wordToShape(Word word, float fontSize, PFont pFont, float rotation) {
		Font font = pFont.getFont().deriveFont(fontSize);
		char[] chars = word.word.toCharArray();
		
		// TODO hmm: this doesn't render newlines.  Hrm.  If you're word text is "foo\nbar", you get "foobar".
		GlyphVector gv = font.layoutGlyphVector(frc, chars, 0, chars.length,
				Font.LAYOUT_LEFT_TO_RIGHT);

		Shape shape = gv.getOutline();

		if (rotation != 0.0) {
			shape = AffineTransform.getRotateInstance(rotation)
					.createTransformedShape(shape);
		}
		
		Rectangle2D rect = shape.getBounds2D();
		int minWordRenderedSize = 7; // TODO extract config setting for minWordRenderedSize
		if (rect.getWidth() < minWordRenderedSize || rect.getHeight() < minWordRenderedSize) {
			return null;		
		}
		
		shape = AffineTransform.getTranslateInstance(-rect.getX(), -rect.getY()).createTransformedShape(shape);
		
		timer.start("bbTreeBuilder.makeTree()");
		word.setBBTree(bbTreeBuilder.makeTree(shape, 7));  // TODO extract config setting for minBoundingBox, and add swelling option
		timer.end("bbTreeBuilder.makeTree()");

		return shape;
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

		Rectangle2D rect = wordShape.getBounds2D();
		timer.start("placeWord");
		PVector wordLocation = placeWord(word, (int)rect.getWidth(), (int)rect.getHeight());
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
	
	private PVector placeWord(Word word, int wordImageWidth, int wordImageHeight) {
		// TODO does it make sense to COMBINE wordplacer & wordnudger, the way you (sort of) orig. had it?  i think it does...
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
		
		GeneralPath polyline = new GeneralPath(wordShape);
		
		boolean useJavaGeom = true;		
		if (useJavaGeom) {
			polyline.transform(AffineTransform.getTranslateInstance(location.x, location.y));
			
			//wordShape = AffineTransform.getTranslateInstance(location.x, location.y).createTransformedShape(wordShape);
			
			boolean drawToParent = false;
			
			//System.out.println(parent.getGraphics().getClass().getName());
			
			Graphics2D g2 = (Graphics2D)(drawToParent ? parent.getGraphics() : destination.image.getGraphics());
			
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setPaint(new Color(colorer.colorFor(word), true));
			g2.fill(polyline);
		
		}
		else {
			Rectangle wordRect = wordShape.getBounds();
			PGraphics wordImage = parent.createGraphics(wordRect.width-wordRect.x, wordRect.height-wordRect.y,
					PApplet.JAVA2D);
			wordImage.beginDraw();
			
				Graphics2D g2 = (Graphics2D)wordImage.image.getGraphics();
				
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setPaint(new Color(colorer.colorFor(word), true));
				g2.fill(polyline);
			
			wordImage.endDraw();
			
			destination.image(wordImage, location.x, location.y);
		}
		
//		destination.pushStyle();
//		destination.stroke(30, 255, 255, 50);
//		destination.noFill();
//		word.getBBTree().draw(destination);
//		destination.rect(location.x, location.y, wordImage.width, wordImage.height);
//		destination.popStyle();
		
		//destination.pushStyle();
		//destination.strokeWeight(PApplet.map(attempt, 0, 700, 1, 30));
		//destination.stroke(0, 255, 255, 50);
		//destination.line(origSpot.x, origSpot.y, location.x, location.y);
		//destination.popStyle();
	}
	

	
	
	public Word currentWord() {
		return hasMore() ? words[wordIndex] : null;
	}
	public int currentWordIndex() {
		return wordIndex;
	}
}
