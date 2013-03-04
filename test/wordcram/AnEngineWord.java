package wordcram;

import java.awt.Rectangle;

import org.junit.Assert;
import org.junit.Test;

public class AnEngineWord {

	@Test
	public void willSetItselfOnItsWord() {
		Word aWord = new Word("hello", 42.0f);
		EngineWord engineWord = new EngineWord(aWord, 1, 1000, new BBTreeBuilder());
		
		// To verify that it hooked up to the Word, set EWord's shape, and get it from Word.
		int anySwelling = 0;
		int width = 939;
		int height = 42;
		engineWord.setShape(new Rectangle(0, 0, width, height), anySwelling);
		
		double zeroDelta = 0.0;
		Assert.assertEquals(width, aWord.getRenderedWidth(), zeroDelta);
		Assert.assertEquals(height, aWord.getRenderedHeight(), zeroDelta);
	}
}
