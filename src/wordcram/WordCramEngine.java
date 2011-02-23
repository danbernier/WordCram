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
import java.util.ArrayList;

import processing.core.*;

class WordCramEngine {

	private PGraphics destination;
	
	private WordFonter fonter;
	private WordSizer sizer;
	private WordColorer colorer;
	private WordAngler angler;
	private WordPlacer placer;
	private WordNudger nudger;
	private WordShaper wordShaper;
	
	private EngineWord[] words;
	private int wordIndex = -1;
	
	private RenderOptions renderOptions;
	
	private Timer timer = Timer.getInstance();

	/**
	 * Contains all words that could not be placed
	 */
	private ArrayList<Word> skippedWords = new ArrayList<Word>();
	
	WordCramEngine(PGraphics destination, Word[] words, WordFonter fonter, WordSizer sizer, WordColorer colorer, WordAngler angler, WordPlacer placer, WordNudger nudger, WordShaper shaper, BBTreeBuilder bbTreeBuilder, RenderOptions renderOptions) {
		
		if (destination.getClass().equals(PGraphics2D.class)) {
			throw new Error("WordCram can't work with P2D buffers, sorry - try using JAVA2D.");
		}
		
		this.destination = destination;
		
		this.fonter = fonter;
		this.sizer = sizer;
		this.colorer = colorer;
		this.angler = angler;
		this.placer = placer;
		this.nudger = nudger;
		
		this.renderOptions = renderOptions;
		
		this.wordShaper = shaper;
		
		timer.start("making shapes");
		this.words = wordsIntoEngineWords(words, bbTreeBuilder);
		timer.end("making shapes");
	}
	
	private EngineWord[] wordsIntoEngineWords(Word[] words, BBTreeBuilder bbTreeBuilder) {
		ArrayList<EngineWord> engineWords = new ArrayList<EngineWord>();
		
		int maxNumberOfWords = renderOptions.maxNumberOfWordsToDraw >= 0 ?
								renderOptions.maxNumberOfWordsToDraw :
								words.length;
		for (int i = 0; i < maxNumberOfWords; i++) {
			
			Word word = words[i];
			EngineWord eWord = new EngineWord(word, i, words.length, bbTreeBuilder);
			
			PFont wordFont = word.getFont(fonter);
			float wordSize = word.getSize(sizer, i, words.length);
			float wordAngle = word.getAngle(angler);
			
			timer.start("making a shape");
			Shape shape = wordShaper.getShapeFor(eWord.word.word, wordFont, wordSize, wordAngle, renderOptions.minShapeSize);
			timer.end("making a shape");
			
			if (shape == null) {
				skipWord(word, WordCram.SHAPE_WAS_TOO_SMALL);
			}
			else {
				eWord.setShape(shape);
				engineWords.add(eWord);  // DON'T add eWords with no shape.
			}
		}
		
		for (int i = maxNumberOfWords; i < words.length; i++) {
			skipWord(words[i], WordCram.WAS_OVER_MAX_NUMBER_OF_WORDS);
		}
		
		return engineWords.toArray(new EngineWord[0]);
	}
	
	private void skipWord(Word word, int reason) {
		/* TODO 0.4: remove this, & calculate it inside getSkippedWords() ?
		 * If we do, we'll have to cache the orig. Word[] somewhere, because EngineWord[]
		 * DOESN'T have words that a) were over the limit, or b) had shapes too small. 
		 */
		skippedWords.add(word);
		
		// TODO 0.4: delete these properties when starting a sketch, in case it's a re-run w/ the same words.
		// NOTE: keep these as properties, because they (will be) deleted when the WordCramEngine re-runs.
		word.wasSkippedBecause(reason);
	}
	
	boolean hasMore() {
		return wordIndex < words.length-1;
	}
	
	void drawAll() {
		timer.start("drawAll");
		while(hasMore()) {
			drawNext();
		}
		timer.end("drawAll");
		//System.out.println(timer.report());
	}
	
	void drawNext() {
		if (!hasMore()) return;
		
		EngineWord eWord = words[++wordIndex];
		
		timer.start("placeWord");
		boolean wasPlaced = placeWord(eWord);
		timer.end("placeWord");
					
		if (wasPlaced) { // TODO unit test (somehow)
			timer.start("drawWordImage");
			drawWordImage(eWord);
			timer.end("drawWordImage");
		}
	}	
	
	private boolean placeWord(EngineWord eWord) {
		Word word = eWord.word;
		Rectangle2D rect = eWord.getShape().getBounds2D(); // TODO can we move these into EngineWord.setDesiredLocation? Does that make sense?		
		int wordImageWidth = (int)rect.getWidth();
		int wordImageHeight = (int)rect.getHeight();
		
		eWord.setDesiredLocation(placer, words.length, wordImageWidth, wordImageHeight, destination.width, destination.height);
		
		// Set maximum number of placement trials
		int maxAttemptsToPlace = renderOptions.maxAttemptsForPlacement > 0 ?
									renderOptions.maxAttemptsForPlacement :
									calculateMaxAttemptsFromWordWeight(word);
		
		EngineWord lastCollidedWith = null;
		for (int attempt = 0; attempt < maxAttemptsToPlace; attempt++) {
			
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
		
		skipWord(eWord.word, WordCram.NO_ROOM);
		timer.count("couldn't place a word");
		return false;
	}

	private int calculateMaxAttemptsFromWordWeight(Word word) {
		return (int)((1.0 - word.weight) * 600) + 100;
	}
	
	private void drawWordImage(EngineWord word) {
		GeneralPath path2d = new GeneralPath(word.getShape());
		
		Graphics2D g2 = (Graphics2D)destination.image.getGraphics();
			
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setPaint(new Color(word.word.getColor(colorer), true));
		g2.fill(path2d);
	}
	
	Word getWordAt(float x, float y) {
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

	Word[] getSkippedWords() {
		return skippedWords.toArray(new Word[0]);
	}
	
	float getProgress() {
		return (float)this.wordIndex / this.words.length;
	}
}
