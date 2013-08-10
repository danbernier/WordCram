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

import java.util.Arrays;
import java.util.Comparator;

import org.junit.Assert;
import org.junit.Test;

import cue.lang.stop.StopWords;

public class AWordCounter {

	private Comparator<Word> alphabetically = new Comparator<Word>() {
		public int compare(Word word1, Word word2) {
			return word1.word.compareTo(word2.word);
		}
	};

	@Test
	public void canExcludeWordsThatAreJustNumbers() {
		WordCounter wc = new WordCounter();
		wc.shouldExcludeNumbers(true);
		Word[] weightedWords = wc.count("I saw U2 in 1999 I saw them 10 times", new RenderOptions());

		Arrays.sort(weightedWords, alphabetically);

		assertWeightedWordsAre(weightedWords, "U2 1", "saw 2", "times 1");
	}

	@Test
	public void canExcludeWordsThatHaveDecimalPoints() {
		WordCounter wc = new WordCounter();
		wc.shouldExcludeNumbers(true);
		Word[] weightedWords = wc.count("Pi is about 3.1415 I think", new RenderOptions());

		Arrays.sort(weightedWords, alphabetically);

		assertWeightedWordsAre(weightedWords, "Pi 1", "think 1");
	}

	@Test
	public void canLeaveWordsThatAreJustNumbers() {
		WordCounter wc = new WordCounter();
		wc.shouldExcludeNumbers(false);
		Word[] weightedWords = wc.count("I saw U2 in 1999 I saw them 10 times", new RenderOptions());

		Arrays.sort(weightedWords, alphabetically);

		assertWeightedWordsAre(weightedWords, "10 1", "1999 1", "U2 1", "saw 2", "times 1");
	}

	@Test
	public void testPunctuationInExtraStopWords() {
		WordCounter wc = new WordCounter().withExtraStopWords("don't i'll");
		Word[] weightedWords = wc.count("i don't want any more can't you see i'll be ill", new RenderOptions());

		Arrays.sort(weightedWords, alphabetically);

		assertWeightedWordsAre(weightedWords, "ill 1", "see 1", "want 1");
	}

	@Test
	public void testCountsWithExtraStopWords() {
		WordCounter wc = new WordCounter().withExtraStopWords("these are stop words");

		Word[] weightedWords = wc.count("biscuit biscuit cocoa cherry cherry cherry stop words are these these are stop words", new RenderOptions());
		Arrays.sort(weightedWords);

		assertWeightedWordsAre(weightedWords, "cherry 3", "biscuit 2", "cocoa 1");
	}

	@Test
	public void testThatExtraStopWordsAreCaseInsensitive() {
		WordCounter wc = new WordCounter().withExtraStopWords("STOP WORDS");

		Word[] weightedWords = wc.count("jelly fish are not stop words Stop Words STOP WORDS", new RenderOptions());
		Arrays.sort(weightedWords, alphabetically);

		assertWeightedWordsAre(weightedWords, "fish 1", "jelly 1");
	}

	@Test
	public void canUseCustomStopWords() {
		WordCounter wc = new WordCounter(StopWords.Custom);

		Word[] weightedWords = wc.count("are am all in she him", new RenderOptions());
		Arrays.sort(weightedWords, alphabetically);

		assertWeightedWordsAre(weightedWords, "all 1", "am 1", "are 1", "him 1", "in 1", "she 1");
	}
	
	@Test
	public void canWorkEvenWhenItCannotGuessTheLanguage() {
		WordCounter wc = new WordCounter();
		
		Word[] weightedWords = wc.count("axonify founding manolo binomscmy", new RenderOptions());

		Arrays.sort(weightedWords, alphabetically);
		assertWeightedWordsAre(weightedWords, "axonify 1", "binomscmy 1", "founding 1", "manolo 1");
	}

	@Test
	public void willSetRightToLeftOnRenderOptionsForHebrewFarsiArabicEtc() {
		String arabic = "الموسوعة الحرة التي يستطيع الجميع تحريرها";
		String farsi = "دانشنامه‌ای آزاد که همه می‌توانند آن را ویرایش کنند؛";
		String hebrew = "למידע נוסף על אפשרויות חיפוש ראו ויקיפדיה:ניווט.";

		assertIsRightToLeft(arabic, "Arabic");
		assertIsRightToLeft(farsi, "Farsi");
		assertIsRightToLeft(hebrew, "Hebrew");
	}

	private void assertIsRightToLeft(String sampleText, String expectedKind) {
		RenderOptions renderOptions = new RenderOptions();
		WordCounter wc = new WordCounter();
		wc.count(sampleText, renderOptions);

		Assert.assertTrue(expectedKind, renderOptions.rightToLeft);
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
