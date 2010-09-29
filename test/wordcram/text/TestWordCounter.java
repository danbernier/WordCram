package wordcram.text;

import java.util.Arrays;
import java.util.Map;

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
