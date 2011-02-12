package wordcram;

import static org.mockito.Mockito.*;

import java.awt.Rectangle;
import java.awt.Shape;
import java.util.LinkedList;
import java.util.List;

import javax.xml.ws.ServiceMode;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import processing.core.*;

public class AWordCramEngine {
	
	private PGraphics destination;
	private WordFonter fonter;
	private WordSizer sizer;
	private WordColorer colorer;
	private WordAngler angler;
	private WordPlacer placer;
	private WordNudger nudger;
	private RenderOptions renderOptions;
	private WordShaper shaper;
	private BBTreeBuilder bbTreeBuilder;
	
	@Before
	public void SetUp() {
		destination = mock(PGraphics.class);
		fonter = mock(WordFonter.class);
		sizer = mock(WordSizer.class);
		colorer = mock(WordColorer.class);
		angler = mock(WordAngler.class);
		placer = mock(WordPlacer.class);
		nudger = mock(WordNudger.class);
		renderOptions = new RenderOptions();
		shaper = mock(WordShaper.class);
		bbTreeBuilder = mock(BBTreeBuilder.class);
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
		}
		
		WordCramEngine engine = getEngine(words);
		
		for (int i = 0; i < words.length; i++) {
			verify(shaper).getShapeFor(words[i].word, pFont, sizes[i], angles[i]);
		}
	}
	
	@Test
	public void willSkipWordsWhoseShapesAreTooSmall() {
		Word big = new Word("big", 10);
		Word small = new Word("small", 1);
		Shape bigShape = mock(Shape.class);
		
		when(shaper.getShapeFor(eq(big.word), any(PFont.class), anyFloat(), anyFloat())).thenReturn(bigShape);
		when(shaper.getShapeFor(eq(small.word), any(PFont.class), anyFloat(), anyFloat())).thenReturn(null);
		
		WordCramEngine engine = getEngine(big, small);
		Word[] skippedWords = engine.getSkippedWords();
		
		Assert.assertEquals(1, skippedWords.length);
		Assert.assertSame(small, skippedWords[0]);
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
		
		when(shaper.getShapeFor(anyString(), any(PFont.class), anyFloat(), anyFloat())).thenReturn(new Rectangle());
		
		WordCramEngine engine = getEngine(words);
		Word[] skippedWords = engine.getSkippedWords();
		
		Assert.assertEquals(3, skippedWords.length);
		Assert.assertSame(words[2], skippedWords[0]);
		Assert.assertSame(words[3], skippedWords[1]);
		Assert.assertSame(words[4], skippedWords[2]);
	}

	private WordCramEngine getEngine(Word... words) {
		return new WordCramEngine(destination, words, fonter, sizer, colorer, angler, placer, nudger, shaper, bbTreeBuilder, renderOptions);
	}
}
