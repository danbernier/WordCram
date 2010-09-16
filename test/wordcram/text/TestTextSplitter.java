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

import java.util.Map;

import org.junit.*;

import wordcram.Word;

public class TestTextSplitter {

	private double epsilon = 0.00001;
	private TextSplitter ts;

	@Before
	public void setup() {
		ts = new TextSplitter("these are stop words");
	}

	@Test
	public void testFrontDoorWithStringArray() {
		String[] stringArray = new String[] { "BISCUIT biscuit, ",
				"mango, mAngO,", " MaNgO! Avocado." };
		Word[] results = ts.split(stringArray);
		Assert.assertEquals("mango", results[0].word);
		Assert.assertEquals("biscuit", results[1].word);
		Assert.assertEquals("avocado", results[2].word);
		Assert.assertEquals(3, results[0].weight, epsilon);
		Assert.assertEquals(2, results[1].weight, epsilon);
		Assert.assertEquals(1, results[2].weight, epsilon);
	}

	@Test
	public void testFrontDoor() {
		Word[] results = ts
				.split("BISCUIT biscuit, mango, mAngO, MaNgO! Avocado.");
		Assert.assertEquals("mango", results[0].word);
		Assert.assertEquals("biscuit", results[1].word);
		Assert.assertEquals("avocado", results[2].word);
		Assert.assertEquals(3, results[0].weight, epsilon);
		Assert.assertEquals(2, results[1].weight, epsilon);
		Assert.assertEquals(1, results[2].weight, epsilon);
	}

	@Test
	public void testCountingAndStopWords() {
		String[] words = ts
				.splitIntoWords("biscuit biscuit cocoa cherry cherry cherry stop words are these these are stop words");
		Map<String, Integer> counts = ts.count(words);
		Word[] weightedWords = ts.toWords(counts);
		
		Assert.assertEquals(3, weightedWords.length);

		Assert.assertEquals("cherry", weightedWords[0].word);
		Assert.assertEquals("biscuit", weightedWords[1].word);
		Assert.assertEquals("cocoa", weightedWords[2].word);

		Assert.assertEquals(3, weightedWords[0].weight, epsilon);
		Assert.assertEquals(2, weightedWords[1].weight, epsilon);
		Assert.assertEquals(1, weightedWords[2].weight, epsilon);
	}

	@Test
	public void testCounts() {
		String[] words = ts.splitIntoWords("a b c a");
		Map<String, Integer> counts = ts.count(words);
		Assert.assertEquals(new Integer(2), (Integer) counts.get("a"));
		Assert.assertEquals(new Integer(1), (Integer) counts.get("b"));
		Assert.assertEquals(new Integer(1), (Integer) counts.get("c"));
		Assert.assertNull(counts.get("d"));
	}

	@Test
	public void testSplit() {
		testSplit("yo baby boo", "yo baby boo", "basic test");
		testSplit("hey now mamma jamma", "hey  now  mamma    jamma",
				"multiple spaces");
		testSplit("a b c", "   a b c    ", "leading and trailing spaces");
		testSplit("a b c", "a\tb \t c", "tabs");
		testSplit("a b c", "a\nb \n c", "new lines");
		testSplit("a b c", "\n\ta\n\tb\t\nc\n\t",
				"mixed, and leading, newlines & tabs");
		testSplit("a b c", "A B C", "downcases");

		testSplit("a b c", "a--b--c", "turns -- into a space");
		testSplit("a b c", "a !@#$#@! b $#@!$#@!# c",
				"removes words that are all punctuation");
		testSplit("a b c", "@a' #b? -c//", "removes punctuation");
		testSplit("a b c", "a 0 b c 1 1948", "removes numbers");

		testSplit("a b c a", "A b C a",
				"preserves duplicate words (dumb test, I know)");
	}

	private void testSplit(String expected, String src, String msg) {
		String[] expectedArray = expected.split(" ");
		String[] actual = ts.splitIntoWords(src);
		Assert.assertArrayEquals(msg, expectedArray, actual);
	}
}
