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
import java.util.Map;

import org.junit.*;

import wordcram.Word;

public class TestTextSplitter {

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
