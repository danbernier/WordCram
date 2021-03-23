package wordcram;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Rectangle;
import java.awt.Shape;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import processing.core.PFont;

public class AWordCramEngine {

	private WordRenderer renderer;
	private WordFonter fonter;
	private WordSizer sizer;
	private WordColorer colorer;
	private WordAngler angler;
	private WordPlacer placer;
	private WordNudger nudger;
	private RenderOptions renderOptions;
	private WordShaper shaper;
	private BBTreeBuilder bbTreeBuilder;
	private Observer observer;

	@Before
	public void SetUp() {
		renderer = mock(WordRenderer.class);
		fonter = mock(WordFonter.class);
		sizer = mock(WordSizer.class);
		colorer = mock(WordColorer.class);
		angler = mock(WordAngler.class);
		placer = mock(WordPlacer.class);
		nudger = mock(WordNudger.class);
		renderOptions = new RenderOptions();
		shaper = mock(WordShaper.class);
		bbTreeBuilder = mock(BBTreeBuilder.class);
		observer = mock(Observer.class);
	}

	// http://docs.mockito.googlecode.com/hg/org/mockito/Mockito.html

	@Test
	public void canBeCreatedAndWillShapeWords() {
		Word[] words = new Word[] {
				new Word("ten", 10),
				new Word("nine", 9),
				new Word("eight", 8) };

		float[] sizes = new float[] { 100, 80, 60 };
		float[] angles = new float[] { 0, 1, 2 };

		PFont pFont = mock(PFont.class);

		for (int i = 0; i < words.length; i++) {
			when(fonter.fontFor(words[i])).thenReturn(pFont);
			when(sizer.sizeFor(eq(words[i]), anyInt(), anyInt())).thenReturn(sizes[i]);
			when(angler.angleFor(words[i])).thenReturn(angles[i]);
			when(shaper.getShapeFor(words[i].word, pFont, sizes[i], angles[i])).thenReturn(new Rectangle(0, 0, 20, 20));
		}

		renderOptions.minFontSize = 100;
		WordCramEngine engine = getEngine(words);

		for (int i = 0; i < words.length; i++) {
			verify(shaper).getShapeFor(words[i].word, pFont, sizes[i], angles[i]);
		}
	}

	@Test
	public void willSkipWordsWhoseShapesAreTooSmall() {
		Word big = new Word("big", 10);
		Word small = new Word("small", 1);
		Shape bigShape = new Rectangle(0, 0, 20, 20);
		Shape smallShape = new Rectangle(0, 0, 1, 1);

		when(shaper.getShapeFor(eq(big.word), any(PFont.class), anyFloat(), anyFloat())).thenReturn(bigShape);
		when(shaper.getShapeFor(eq(small.word), any(PFont.class), anyFloat(), anyFloat())).thenReturn(smallShape);

		WordCramEngine engine = getEngine(big, small);
		Word[] skippedWords = engine.getSkippedWords();

		Assert.assertEquals(1, skippedWords.length);
		Assert.assertSame(small, skippedWords[0]);

		Assert.assertEquals(WordSkipReason.SHAPE_WAS_TOO_SMALL, small.wasSkippedBecause());
		Assert.assertNull(big.wasSkippedBecause());
	}

	@Test
	public void willSkipWordsWhoseShapesAreTooSmallEvenWhenMinShapeSizeIsZero() {
		Word big = new Word("big", 10);
		Word small = new Word("small", 1);
		Shape bigShape = new Rectangle(0, 0, 20, 20);
		Shape smallShape = new Rectangle(0, 0, 0, 1);
		renderOptions.minShapeSize = 0;

		when(shaper.getShapeFor(eq(big.word), any(PFont.class), anyFloat(), anyFloat())).thenReturn(bigShape);
		when(shaper.getShapeFor(eq(small.word), any(PFont.class), anyFloat(), anyFloat())).thenReturn(smallShape);

		WordCramEngine engine = getEngine(big, small);
		Word[] skippedWords = engine.getSkippedWords();

		Assert.assertEquals(1, skippedWords.length);
		Assert.assertSame(small, skippedWords[0]);

		Assert.assertEquals(WordSkipReason.SHAPE_WAS_TOO_SMALL, small.wasSkippedBecause());
		Assert.assertNull(big.wasSkippedBecause());
	}

	@Test
	public void willNotGoPastRenderOptionsMaxNumberOfWords() {
		Word[] words = new Word[] {
				new Word("ten", 10),
				new Word("nine", 9),
				new Word("eight", 8),
				new Word("seven", 7),
				new Word("six", 6)
		};

		renderOptions.maxNumberOfWordsToDraw = 2;

		when(shaper.getShapeFor(anyString(), any(PFont.class), anyFloat(), anyFloat())).thenReturn(new Rectangle(0, 0, 20, 20));

		WordCramEngine engine = getEngine(words);
		Word[] skippedWords = engine.getSkippedWords();

		Assert.assertEquals(3, skippedWords.length);
		Assert.assertSame(words[2], skippedWords[0]);
		Assert.assertSame(words[3], skippedWords[1]);
		Assert.assertSame(words[4], skippedWords[2]);

		for (Word skippedWord : skippedWords) {
			Assert.assertEquals(WordSkipReason.WAS_OVER_MAX_NUMBER_OF_WORDS, skippedWord.wasSkippedBecause());
		}
	}

	private WordCramEngine getEngine(Word... words) {
		return new WordCramEngine(renderer, words, fonter, sizer, colorer, angler, placer, nudger, shaper, bbTreeBuilder, renderOptions, observer);
	}
}
