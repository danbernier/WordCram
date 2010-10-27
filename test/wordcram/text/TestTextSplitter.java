package wordcram.text;

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

import org.junit.*;

import wordcram.Word;

public class TestTextSplitter {

	private TextSplitter ts;

	@Before
	public void setup() {
		ts = new TextSplitter("these are stop words");
	}

	@Test
	public void testFrontDoorWithPunctuationInStopWords() {
		ts = new TextSplitter("don't i'll");
		Word[] words = ts
				.split("I don't want any more, can't you see I'll be ill?");

		// Sort them by word, since they're all the same weight.
		Arrays.sort(words, new Comparator<Word>() {
			public int compare(Word word1, Word word2) {
				return word1.word.compareTo(word2.word);
			}
		});

		String[] expectedWords = "any be can't i ill more see want you"
				.split(" ");

		Assert.assertEquals(expectedWords.length, words.length);
		for (int i = 0; i < words.length; i++) {
			Assert.assertEquals(expectedWords[i], words[i].word);
			Assert.assertEquals(1.0, words[i].weight, 0.0f);
		}
	}

	@Test
	public void testFrontDoorWithStringArray() {
		String[] stringArray = new String[] { "BISCUIT biscuit, ",
				"mango, mAngO,", " MaNgO! Avocado." };
		Word[] results = ts.split(stringArray);
		Arrays.sort(results);
		Assert.assertEquals("mango", results[0].word);
		Assert.assertEquals("biscuit", results[1].word);
		Assert.assertEquals("avocado", results[2].word);
		Assert.assertEquals(3, (int) results[0].weight);
		Assert.assertEquals(2, (int) results[1].weight);
		Assert.assertEquals(1, (int) results[2].weight);
	}

	@Test
	public void testFrontDoor() {
		Word[] results = ts
				.split("BISCUIT biscuit, mango, mAngO, MaNgO! Avocado.");
		Arrays.sort(results);
		Assert.assertEquals("mango", results[0].word);
		Assert.assertEquals("biscuit", results[1].word);
		Assert.assertEquals("avocado", results[2].word);
		Assert.assertEquals(3, (int) results[0].weight);
		Assert.assertEquals(2, (int) results[1].weight);
		Assert.assertEquals(1, (int) results[2].weight);
	}

	@Test
	public void testSplitLeftoverCases() {
		testSplit("a b c", "a b c", "basic test");
		testSplit("a b c", "A B C", "downcases");
		testSplit("a b c a", "A b C a",
				"preserves duplicate words (dumb test, I know)");
	}

	@Test
	public void removesWhiteSpace() {
		testSplit("a b c d", "a  b  c    d", "multiple spaces");
		testSplit("a b c", "   a b c    ", "leading and trailing spaces");
		testSplit("a b c", "a\tb \t c", "tabs");
		testSplit("a b c", "a\nb \n c", "new lines");
		testSplit("a b c", "\n\ta\n\tb\t\nc\n\t",
				"mixed, and leading, newlines & tabs");
	}

	@Test
	public void removesPunctuationOnTheEndsOfWords() {
		testSplit("a b c", "@a' #b? -c/", "removes punctuation");
		testSplit("i know i said", "\"I know,\" I said.",
				"removes punctuation from beginnigs & ends of words");
	}

	@Test
	public void removesMultiplePunctuationOnTheEndsOfWords() {
		testSplit("a b c", "@?a~' #@!&&$^b?@@ -@#$#@%c$#@//",
				"removes punctuation");
		testSplit("hey you there", "Hey--you there!!",
				"removes punctuation from beginnigs & ends of words");
	}

	@Test
	public void leavesPunctuationInsideWords() {
		testSplit("i'll say you're silly", "I'll say you're silly!",
				"leaves punctuation inside words");
	}

	@Test
	public void removesDashesBetweenWords() {
		testSplit("a b c", "a--b--c", "turns -- into a space");
	}

	@Test
	@Ignore("This could be an option later")
	public void removesNumbers() {
		testSplit("a b c", "a 0 b c 1 1948", "removes numbers");
	}

	private void testSplit(String expected, String src, String msg) {
		String[] expectedArray = expected.split(" ");
		String[] actual = ts.splitIntoWords(src);
		
		for (int i = 0; i < expectedArray.length; i++) {
			Assert.assertEquals(msg, expectedArray[i], actual[i]);
		}
		Assert.assertEquals(msg, expectedArray.length, actual.length);
	}
}
