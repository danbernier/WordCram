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

		WordColorer colorer = mock(WordColorer.class);
		
		float size = 493.3f;
		float angle = 123.4f;
		PFont font = mock(PFont.class);
		int color = 76;
		
		when(colorer.colorFor(word)).thenReturn(color);
		
		EngineWord eWord = new EngineWord(word, rank, wordCount, colorer, mock(BBTreeBuilder.class));
		
		Assert.assertEquals(word, eWord.word);
		Assert.assertEquals(rank, eWord.rank);
		Assert.assertEquals(color, eWord.getColor());
	}
}
