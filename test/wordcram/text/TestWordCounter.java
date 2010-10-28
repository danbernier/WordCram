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
	public void canRemoveWordsThatAreJustNumbers() {
		WordCounter wc = new WordCounter("");
		wc.shouldRemoveNumbers(true);
		String[] words = split("I saw U2 in 1999 I saw them 10 times");
		Word[] weightedWords = wc.count(words);

		Arrays.sort(weightedWords, alphabetically);

		assertWeightedWordsAre(weightedWords, "I 2", "U2 1", "in 1", "saw 2",
				"them 1", "times 1");
	}
	
	@Test
	public void canRemoveWordsThatHaveDecimalPoints() {
		WordCounter wc = new WordCounter("");
		wc.shouldRemoveNumbers(true);
		String[] words = split("Pi is about 3.1415 I think");
		Word[] weightedWords = wc.count(words);
		
		Arrays.sort(weightedWords, alphabetically);
		
		assertWeightedWordsAre(weightedWords, "I 1", "Pi 1", "about 1", "is 1", "think 1");
	}

	@Test
	public void canLeaveWordsThatAreJustNumbers() {
		WordCounter wc = new WordCounter("");
		wc.shouldRemoveNumbers(false);
		String[] words = split("I saw U2 in 1999 I saw them 10 times");
		Word[] weightedWords = wc.count(words);

		Arrays.sort(weightedWords, alphabetically);

		assertWeightedWordsAre(weightedWords, "10 1", "1999 1", "I 2", "U2 1",
				"in 1", "saw 2", "them 1", "times 1");
	}

	@Test
	public void testPunctuationInStopWords() {
		WordCounter wc = new WordCounter("don't i'll");
		String[] words = split("i don't want any more can't you see i'll be ill");
		Word[] weightedWords = wc.count(words);		

		// Sort them by word, since they're all the same weight.
		Arrays.sort(weightedWords, alphabetically);

		assertWeightedWordsAre(weightedWords, "any 1", "be 1", "can't 1",
				"i 1", "ill 1", "more 1", "see 1", "want 1", "you 1");
	}

	@Test
	public void testCountsWithStopWords() {
		WordCounter wc = new WordCounter("these are stop words");

		String[] words = split("biscuit biscuit cocoa cherry cherry cherry stop words are these these are stop words");
		Word[] weightedWords = wc.count(words);
		Arrays.sort(weightedWords);
		
		assertWeightedWordsAre(weightedWords, "cherry 3", "biscuit 2", "cocoa 1");
	}
	
	@Test
	public void testThatStopWordsAreCaseInsensitive() {
		WordCounter wc = new WordCounter("STOP WORDS");
		
		String[] words = split("these are stop words Stop Words STOP WORDS");
		Word[] weightedWords = wc.count(words);
		Arrays.sort(weightedWords, alphabetically);
		
		assertWeightedWordsAre(weightedWords, "are 1", "these 1");
	}

	@Test
	public void testCountsWithNoStopWords() {
		WordCounter wc = new WordCounter("");

		String[] words = split("a b c a b a");
		Word[] weightedWords = wc.count(words);
		Arrays.sort(weightedWords);
		
		assertWeightedWordsAre(weightedWords, "a 3", "b 2", "c 1");
	}
	
	private void assertWeightedWordsAre(Word[] actualWords, String... expectedCases) {
		
		Assert.assertEquals("Got the wrong number of words", expectedCases.length, actualWords.length);
		
		for (int i = 0; i < expectedCases.length; i++) {
			String[] wordAndWeight = split(expectedCases[i].trim());
			Assert.assertEquals(wordAndWeight[0], actualWords[i].word);
			Assert.assertEquals(Double.parseDouble(wordAndWeight[1]), actualWords[i].weight, 0.0001);
		}
	}
	
	private String[] split(String words) {
		return words.split(" ");
	}
}
