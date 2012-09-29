package wordcram;

import java.awt.Shape;
import java.util.ArrayList;

import processing.core.PFont;
import processing.core.PGraphics;

public abstract class AbstractWordCramEngine implements WordCramEngine {

    protected PGraphics destination;

    protected WordFonter fonter;
    protected WordSizer sizer;
    protected WordColorer colorer;
    protected WordAngler angler;
    protected WordPlacer placer;
    protected WordNudger nudger;

    protected Word[] words; // just a safe copy
    protected EngineWord[] eWords;
    protected int eWordIndex = -1;

    protected RenderOptions renderOptions;

    AbstractWordCramEngine(PGraphics destination, Word[] words, WordFonter fonter, WordSizer sizer, WordColorer colorer, WordAngler angler, WordPlacer placer, WordNudger nudger, WordShaper shaper, BBTreeBuilder bbTreeBuilder, RenderOptions renderOptions) {
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

    protected void skipWord(Word word, int reason) {
        // TODO delete these properties when starting a sketch, in case it's a re-run w/ the same words.
        // NOTE: keep these as properties, because they (will be) deleted when the WordCramEngine re-runs.
        word.wasSkippedBecause(reason);
    }

    public Word getWordAt(float x, float y) {
        for (int i = eWords.length-1; i >= 0; i--) {
            if (eWords[i].wasPlaced()) {
                if (eWords[i].containsPoint(x, y)) {
                    return eWords[i].word;
                }
            }
        }
        return null;
    }

    public Word[] getSkippedWords() {
        ArrayList<Word> skippedWords = new ArrayList<Word>();
        for (int i = 0; i < words.length; i++) {
            if (words[i].wasSkipped()) {
                skippedWords.add(words[i]);
            }
        }
        return skippedWords.toArray(new Word[0]);
    }
}
