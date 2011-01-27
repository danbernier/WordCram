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
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

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

	private WordShaper wordShaper = new WordShaper();
	
	private EngineWord[] words;
	private int wordIndex = -1;
	
	private int maxAttemptsForPlacement = -1;
	
	private boolean printWhenSkippingWords = false;
	private boolean registerSkippedWords = false;
	private boolean usesCustomDestination = false;
	
	private Timer timer = Timer.getInstance();

	/**
	 * Contains all words that could not be placed
	 */
	private ArrayList<Word> unplacedWords = null;
	
	public WordCramEngine(PApplet parent, PGraphics destination, Word[] words, WordFonter fonter, WordSizer sizer, WordColorer colorer, WordAngler angler, WordPlacer placer, WordNudger nudger, boolean printWhenSkippingWords) {
		this.parent = parent;
		this.destination = destination;
		
		this.fonter = fonter;
		this.sizer = sizer;
		this.colorer = colorer;
		this.angler = angler;
		this.placer = placer;
		this.nudger = nudger;
		
		this.printWhenSkippingWords = printWhenSkippingWords;
		
		this.unplacedWords = new ArrayList<Word>();
		
		timer.start("making shapes");
		this.words = wordsIntoEngineWords(words);
		timer.end("making shapes");
	}
	
	private EngineWord[] wordsIntoEngineWords(Word[] words) {
		ArrayList<EngineWord> engineWords = new ArrayList<EngineWord>();
		
		for (int i = 0; i < words.length; i++) {
			
			Word word = words[i];
			EngineWord eWord = new EngineWord(word, i, words.length, sizer, angler, fonter, colorer);
						
			timer.start("making a shape");
			Shape shape = wordShaper.getShapeFor(eWord);
			timer.end("making a shape");
			
			if (shape == null) {
				if (printWhenSkippingWords) {
					System.out.println("Too small: " + word);	
				}
				if(this.registerSkippedWords) this.unplacedWords.add(eWord.word);
			}
			else {
				eWord.setShape(shape);
				engineWords.add(eWord);  // DON'T add eWords with no shape.
			}
		}
		
		return engineWords.toArray(new EngineWord[0]);
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
		
		EngineWord eWord = words[++wordIndex];
		
		timer.start("placeWord");
		boolean wasPlaced = placeWord(eWord);
		timer.end("placeWord");
					
		if (wasPlaced) {
			timer.start("drawWordImage");
			drawWordImage(eWord);
			timer.end("drawWordImage");
		}
	}	
	
	private boolean placeWord(EngineWord eWord) {
		Word word = eWord.word;
		Rectangle2D rect = eWord.getShape().getBounds2D();		
		int wordImageWidth = (int)rect.getWidth();
		int wordImageHeight = (int)rect.getHeight();
		
		eWord.setDesiredLocation(placer.place(word, eWord.rank, words.length, wordImageWidth, wordImageHeight, destination.width, destination.height));
		
		// Set maximum number of placement trials
		int max = (int)((1.0-word.weight) * 600) + 100;
		if(this.maxAttemptsForPlacement > -1) max = this.maxAttemptsForPlacement;
		
		EngineWord lastCollidedWith = null;
		for (int attempt = 0; attempt < max; attempt++) {
			
			eWord.nudge(nudger.nudgeFor(word, attempt));
			
			PVector loc = eWord.getCurrentLocation();
			if (loc.x < 0 || loc.y < 0 || loc.x + wordImageWidth >= destination.width || loc.y + wordImageHeight >= destination.height) {
				timer.count("OUT OF BOUNDS");
				continue;
			}
			
			if (lastCollidedWith != null && eWord.overlaps(lastCollidedWith)) {
				timer.count("CACHE COLLISION");
				continue;
			}
			
			boolean foundOverlap = false;
			for (int i = 0; !foundOverlap && i < wordIndex; i++) {
				EngineWord otherWord = words[i];
				if (eWord.overlaps(otherWord)) {
					foundOverlap = true;
					lastCollidedWith = otherWord;
				}
			}
			
			if (!foundOverlap) {
				timer.count("placed a word");
				eWord.finalizeLocation();
				return true;
			}
		}
		
		if (printWhenSkippingWords) {
			System.out.println("Couldn't fit: " + word);
		}
		if(this.registerSkippedWords) this.unplacedWords.add(eWord.word);
		timer.count("couldn't place a word");
		return false;
	}
	
	private void drawWordImage(EngineWord word) {
		if(this.usesCustomDestination) destination.beginDraw();
		
		Path2D.Float path2d = new Path2D.Float(word.getShape());
		
		boolean drawToParent = false;
		Graphics2D g2 = (Graphics2D)(drawToParent ? parent.getGraphics() : destination.image.getGraphics());
			
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setPaint(new Color(word.getColor(), true));
		g2.fill(path2d);
		
//		destination.pushStyle();
//		destination.stroke(30, 255, 255, 50);
//		destination.noFill();
//		word.getBBTree().draw(destination);
//		destination.rect(location.x, location.y, wordImage.width, wordImage.height);
//		destination.popStyle();
		
		if(this.usesCustomDestination) destination.endDraw();
	}
	
	
	public Word getWordAt(float x, float y) {
		for (int i = 0; i < words.length; i++) {
			if (words[i].wasPlaced()) {
				Shape shape = words[i].getShape();
				if (shape.contains(x, y)) {
					return words[i].word;
				}
			}
		}
		return null;
	}

	/**
	 * Gets a list of all words that were not placed on the canvas.
	 * This requires that {@link registerSkippedWords(boolean)} has been set.
	 * @author FEZ (Felix Kratzer)
	 * @return
	 */
	protected ArrayList<Word> getSkippedWords() {
		if(this.registerSkippedWords) return unplacedWords;
		else return null;
	}

	/**
	 * Should skipped words be registered in a certain place so they can be 
	 * used after drawing?
	 * @author FEZ (Felix Kratzer)
	 * @param doRegister
	 */
	protected void registerSkippedWords(boolean doRegister){
		this.registerSkippedWords = doRegister;
	}
	
	/**
	 * Sets the maximum number of attempts that are used for placing a word.
	 * If nothing is set, a default value is calculated in {@link #placeWord(EngineWord)}
	 * @author FEZ (Felix Kratzer)
	 * @param maxAttempts
	 */
	protected void setMaxAttemptsForPlacement(int maxAttempts) {
		this.maxAttemptsForPlacement = maxAttempts;
	}
}
 