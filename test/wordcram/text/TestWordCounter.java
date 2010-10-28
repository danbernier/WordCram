package wordcram.text;

import java.util.*;

import org.junit.Assert;
import org.junit.Test;

import wordcram.Word;

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

public class TestWordCounter {
	
	private Comparator<Word> alphabetically = new Comparator<Word>() {
		public int compare(Word word1, Word word2) {
			return word1.word.compareTo(word2.word);
		}
	};

	@Test
	public void testPunctuationInStopWords() {
		WordCounter wc = new WordCounter("don't i'll");
		String[] words = "i don't want any more, can't you see i'll be ill?".split(" ");
		Word[] weightedWords = wc.count(words);		

		// Sort them by word, since they're all the same weight.
		Arrays.sort(weightedWords, alphabetically);

		String[] expectedWords = "any be can't i ill? more, see want you"
				.split(" ");

		Assert.assertEquals(expectedWords.length, weightedWords.length);
		for (int i = 0; i < weightedWords.length; i++) {
			Assert.assertEquals(expectedWords[i], weightedWords[i].word);
			Assert.assertEquals(1.0, weightedWords[i].weight, 0.0f);
		}
	}

	@Test
	public void testCountsWithStopWords() {
		WordCounter wc = new WordCounter("these are stop words");

		String[] words = "biscuit biscuit cocoa cherry cherry cherry stop words are these these are stop words"
				.split(" ");
		Word[] weightedWords = wc.count(words);
		Arrays.sort(weightedWords);

		Assert.assertEquals(3, weightedWords.length);

		Assert.assertEquals("cherry", weightedWords[0].word);
		Assert.assertEquals("biscuit", weightedWords[1].word);
		Assert.assertEquals("cocoa", weightedWords[2].word);

		Assert.assertEquals(3, (int) weightedWords[0].weight);
		Assert.assertEquals(2, (int) weightedWords[1].weight);
		Assert.assertEquals(1, (int) weightedWords[2].weight);
	}
	
	@Test
	public void testThatStopWordsAreCaseInsensitive() {
		WordCounter wc = new WordCounter("STOP WORDS");
		
		String[] words = "these are stop words Stop Words STOP WORDS".split(" ");
		Word[] weightedWords = wc.count(words);
		Arrays.sort(weightedWords, alphabetically);
		
		Assert.assertEquals(2, weightedWords.length);
		Assert.assertEquals("are", weightedWords[0].word);
		Assert.assertEquals("these", weightedWords[1].word);
	}

	@Test
	public void testCountsWithNoStopWords() {
		WordCounter wc = new WordCounter("");

		String[] words = "a b c a b a".split(" ");
		Word[] weightedWords = wc.count(words);
		Arrays.sort(weightedWords);

		Assert.assertEquals(3, weightedWords.length);

		Assert.assertEquals("a", weightedWords[0].word);
		Assert.assertEquals("b", weightedWords[1].word);
		Assert.assertEquals("c", weightedWords[2].word);

		Assert.assertEquals(3, (int) weightedWords[0].weight);
		Assert.assertEquals(2, (int) weightedWords[1].weight);
		Assert.assertEquals(1, (int) weightedWords[2].weight);
	}
}
