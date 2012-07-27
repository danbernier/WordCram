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
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
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

    private Word[] words; // just a safe copy
    private EngineWord[] eWords;
    private int eWordIndex = -1;

    private RenderOptions renderOptions;

    WordCramEngine(PGraphics destination, Word[] words, WordFonter fonter, WordSizer sizer, WordColorer colorer, WordAngler angler, WordPlacer placer, WordNudger nudger, WordShaper shaper, BBTreeBuilder bbTreeBuilder, RenderOptions renderOptions) {
        this.destination = destination;

        this.fonter = fonter;
        this.sizer = sizer;
        this.colorer = colorer;
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

            Shape shape = wordShaper.getShapeFor(eWord.word.word, wordFont, wordSize, wordAngle, renderOptions.minShapeSize);
            if (shape == null) {
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

    private void skipWord(Word word, int reason) {
        // TODO delete these properties when starting a sketch, in case it's a re-run w/ the same words.
        // NOTE: keep these as properties, because they (will be) deleted when the WordCramEngine re-runs.
        word.wasSkippedBecause(reason);
    }

    boolean hasMore() {
        return eWordIndex < eWords.length-1;
    }

    void drawAll() {
        while(hasMore()) {
            drawNext();
        }
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
        Word word = eWord.word;
        Rectangle2D rect = eWord.getShape().getBounds2D(); // TODO can we move these into EngineWord.setDesiredLocation? Does that make sense?
        int wordImageWidth = (int)rect.getWidth();
        int wordImageHeight = (int)rect.getHeight();

        eWord.setDesiredLocation(placer, eWords.length, wordImageWidth, wordImageHeight, destination.width, destination.height);

        // Set maximum number of placement trials
        int maxAttemptsToPlace = renderOptions.maxAttemptsToPlaceWord > 0 ?
                                    renderOptions.maxAttemptsToPlaceWord :
                                    calculateMaxAttemptsFromWordWeight(word);

        EngineWord lastCollidedWith = null;
        for (int attempt = 0; attempt < maxAttemptsToPlace; attempt++) {

            eWord.nudge(nudger.nudgeFor(word, attempt));

            PVector loc = eWord.getCurrentLocation();
            if (loc.x < 0 || loc.y < 0 || loc.x + wordImageWidth >= destination.width || loc.y + wordImageHeight >= destination.height) {
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

        skipWord(eWord.word, WordCram.NO_SPACE);
        return false;
    }

    private int calculateMaxAttemptsFromWordWeight(Word word) {
        return (int)((1.0 - word.weight) * 600) + 100;
    }

    private void drawWordImage(EngineWord word) {
        GeneralPath path2d = new GeneralPath(word.getShape());

//        Graphics2D g2 = (Graphics2D)destination.image.getGraphics();
        Graphics2D g2 = ((PGraphicsJava2D)destination).g2;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(new Color(word.word.getColor(colorer), true));
        g2.fill(path2d);
    }

    Word getWordAt(float x, float y) {
        for (int i = eWords.length-1; i >= 0; i--) {
            if (eWords[i].wasPlaced()) {
                if (eWords[i].containsPoint(x, y)) {
                    return eWords[i].word;
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
        return (float)this.eWordIndex / this.eWords.length;
    }
}
