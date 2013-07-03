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
import java.awt.geom.Rectangle2D;
import java.io.PrintStream;
import java.util.ArrayList;

import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PVector;

class WordCramEngine {

    private WordFonter fonter;
    private WordSizer sizer;
    private WordAngler angler;
    private WordPlacer placer;
    private WordNudger nudger;

    private Word[] words; // just a safe copy
    private EngineWord[] eWords;
    private int eWordIndex = -1;

    private RenderOptions renderOptions;
    private Renderer renderer;

    WordCramEngine(Word[] words, WordFonter fonter, WordSizer sizer, WordAngler angler, WordPlacer placer, WordNudger nudger, WordShaper shaper, BBTreeBuilder bbTreeBuilder, RenderOptions renderOptions) {
        this.fonter = fonter;
        this.sizer = sizer;
        this.angler = angler;
        this.placer = placer;
        this.nudger = nudger;

        this.renderOptions = renderOptions;
        this.words = words;
        this.eWords = wordsIntoEngineWords(words, shaper, bbTreeBuilder);
    }

    private EngineWord[] wordsIntoEngineWords(Word[] words, WordShaper wordShaper, BBTreeBuilder bbTreeBuilder) {
        ArrayList<EngineWord> engineWords = new ArrayList<EngineWord>();

        int maxNumberOfWords = words.length;
        if (renderOptions.maxNumberOfWordsToDraw >= 0) {
            maxNumberOfWords = Math.min(maxNumberOfWords, renderOptions.maxNumberOfWordsToDraw);
        }

        for (int i = 0; i < maxNumberOfWords; i++) {
            Word word = words[i];
            EngineWord eWord = new EngineWord(word, i, words.length, bbTreeBuilder);

            PFont wordFont = word.getFont(fonter);
            float wordSize = word.getSize(sizer, i, words.length);
            float wordAngle = word.getAngle(angler);

            Shape shape = wordShaper.getShapeFor(eWord.getWord().word, wordFont, wordSize, wordAngle);
            if (isTooSmall(shape, renderOptions.minShapeSize)) {
                skipWord(word, WordCram.SHAPE_WAS_TOO_SMALL);
            }
            else {
                eWord.setShape(shape, renderOptions.wordPadding);
                engineWords.add(eWord);  // DON'T add eWords with no shape.
            }
        }

        for (int i = maxNumberOfWords; i < words.length; i++) {
            skipWord(words[i], WordCram.WAS_OVER_MAX_NUMBER_OF_WORDS);
        }

        return engineWords.toArray(new EngineWord[0]);
    }
    
    public void setRenderer(Renderer renderer) {
    	this.renderer = renderer;
    }

    private boolean isTooSmall(Shape shape, int minShapeSize) {
        Rectangle2D r = shape.getBounds2D();

        // Most words will be wider than tall, so this basically boils down to height.
        // For the odd word like "I", we check width, too.
        return r.getHeight() < minShapeSize || r.getWidth() < minShapeSize;
    }

    private void skipWord(Word word, int reason) {
        // TODO delete these properties when starting a sketch, in case it's a re-run w/ the same words.
        // NOTE: keep these as properties, because they (will be) deleted when the WordCramEngine re-runs.
        word.wasSkippedBecause(reason);
    }

    boolean hasMore() {
        return eWordIndex < eWords.length-1;
    }

    void drawAllVerbose() {
    	drawAllVerbose(System.out);
    }

    void drawAllVerbose(PrintStream debugStream) {
    	debugStream.println("Start drawing words.");
    	while (hasMore()) {
    		drawNext();
    		debugStream.println("Drew a word. Progress: " + (eWordIndex + 1) +
    				"/" + eWords.length + "(" + ((int) (getProgress() * 100)) + "%)");
    	}
    	debugStream.println("Finished drawing words. Results:");
    	printResult(debugStream);
    }

    void printResult(PrintStream debugStream) {
    	Word[] skippedWords = getSkippedWords();
    	debugStream.println("Total Words: " + words.length);
    	debugStream.println("Placed % (of those tried): " + ((int) (getProgress()*100)));
    	int overNumber = 0;
    	int tooSmall = 0;
    	int noSpace = 0;
    	for (Word w: skippedWords) {
    		if (w.wasSkippedBecause() == WordCram.NO_SPACE) {
    			noSpace++;
    		} else if (w.wasSkippedBecause() == WordCram.SHAPE_WAS_TOO_SMALL) {
    			tooSmall++;
    		} else if (w.wasSkippedBecause() == WordCram.WAS_OVER_MAX_NUMBER_OF_WORDS) {
    			overNumber++;
    		} else {
    			//That should not happen
    			throw new RuntimeException("Word skip reason not present in WordCram: " + w.wasSkippedBecause());
    		}
    	}
    	debugStream.println("Skipped because no Space: " + noSpace);
    	debugStream.println("Skipped because too Small: " + tooSmall);
    	debugStream.println("Skipped because max Number reached: " + overNumber);
    }

    void drawAll() {
        while(hasMore()) {
            drawNext();
        }
        renderer.close();
    }

    void drawNext() {
        if (!hasMore()) return;

        EngineWord eWord = eWords[++eWordIndex];

        boolean wasPlaced = placeWord(eWord);
        if (wasPlaced) { // TODO unit test (somehow)
            drawWordImage(eWord);
        }
    }

    private boolean placeWord(EngineWord eWord) {
        Word word = eWord.getWord();
        Rectangle2D rect = eWord.getShape().getBounds2D(); // TODO can we move these into EngineWord.setDesiredLocation? Does that make sense?
        int wordImageWidth = (int)rect.getWidth();
        int wordImageHeight = (int)rect.getHeight();

        eWord.setDesiredLocation(placer, eWords.length, wordImageWidth, wordImageHeight, renderer.getWidth(), renderer.getHeight());

        // Set maximum number of placement trials
        int maxAttemptsToPlace = renderOptions.maxAttemptsToPlaceWord > 0 ?
                                    renderOptions.maxAttemptsToPlaceWord :
                                    calculateMaxAttemptsFromWordWeight(word);

        EngineWord lastCollidedWith = null;
        for (int attempt = 0; attempt < maxAttemptsToPlace; attempt++) {

            eWord.nudge(nudger.nudgeFor(word, attempt));

            PVector loc = eWord.getCurrentLocation();
            if (loc.x < 0 || loc.y < 0 || loc.x + wordImageWidth >= renderer.getWidth() || loc.y + wordImageHeight >= renderer.getHeight()) {
                continue;
            }

            if (lastCollidedWith != null && eWord.overlaps(lastCollidedWith)) {
                continue;
            }

            boolean foundOverlap = false;
            for (int i = 0; !foundOverlap && i < eWordIndex; i++) {
                EngineWord otherWord = eWords[i];
                if (otherWord.wasSkipped()) continue; //can't overlap with skipped word

                if (eWord.overlaps(otherWord)) {
                    foundOverlap = true;
                    lastCollidedWith = otherWord;
                }
            }

            if (!foundOverlap) {
                eWord.finalizeLocation();
                return true;
            }
        }

        skipWord(eWord.getWord(), WordCram.NO_SPACE);
        return false;
    }

    private int calculateMaxAttemptsFromWordWeight(Word word) {
        return (int)((1.0 - word.weight) * 600) + 100;
    }

    private void drawWordImage(EngineWord word) {
        renderer.drawEngineWord(word);
    }

    Word getWordAt(float x, float y) {
        for (int i = eWords.length-1; i >= 0; i--) {
            if (eWords[i].wasPlaced()) {
                if (eWords[i].containsPoint(x, y)) {
                    return eWords[i].getWord();
                }
            }
        }
        return null;
    }

    Word[] getSkippedWords() {
        ArrayList<Word> skippedWords = new ArrayList<Word>();
        for (int i = 0; i < words.length; i++) {
            if (words[i].wasSkipped()) {
                skippedWords.add(words[i]);
            }
        }
        return skippedWords.toArray(new Word[0]);
    }

    float getProgress() {
        return (float) (this.eWordIndex+1) / this.eWords.length;
    }
}
