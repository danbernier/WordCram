package wordcram;

import static org.mockito.Mockito.*;
import junit.framework.Assert;

import org.junit.Test;

import processing.core.PFont;

public class AnEngineWord {
	
	@Test
	public void callsComponentsForTheWordAndCachesTheirValues() {
		Word word = new Word("word", 349);
		int rank = 942;
		int wordCount = 3949;
		
		WordSizer sizer = mock(WordSizer.class);
		WordAngler angler = mock(WordAngler.class);
		WordFonter fonter = mock(WordFonter.class);
		WordColorer colorer = mock(WordColorer.class);
		
		float size = 493.3f;
		float angle = 123.4f;
		PFont font = mock(PFont.class);
		int color = 76;
		
		when(sizer.sizeFor(word, rank, wordCount)).thenReturn(size);
		when(angler.angleFor(word)).thenReturn(angle);
		when(fonter.fontFor(word)).thenReturn(font);
		when(colorer.colorFor(word)).thenReturn(color);
		
		EngineWord eWord = new EngineWord(word, rank, wordCount, sizer, angler, fonter, colorer, mock(BBTreeBuilder.class));
		
		Assert.assertEquals(word, eWord.word);
		Assert.assertEquals(rank, eWord.rank);
		
		Assert.assertEquals(size, eWord.getSize());
		Assert.assertEquals(angle, eWord.getAngle());
		Assert.assertEquals(font, eWord.getFont());
		Assert.assertEquals(color, eWord.getColor());
	}
}
