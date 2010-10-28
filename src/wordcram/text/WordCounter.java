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

import java.util.*;

import wordcram.Word;

// TODO if we move all .text.* classes into WordCram, we can make this pkg-local...
public class WordCounter {

	private Set<String> stopWords;

	public WordCounter(String stopWordsString) {
		String[] stopWordsArray = stopWordsString.toLowerCase().split(" ");
		stopWords = new HashSet<String>(Arrays.asList(stopWordsArray));
	}

	public Word[] count(String[] words) {
		return toWords(countWords(words));
	}

	private Map<String, Integer> countWords(String[] words) {
		HashMap<String, Integer> counts = new HashMap<String, Integer>();

		for (String word : words) {
			if (!stopWords.contains(word.toLowerCase())) {
				if (!counts.containsKey(word)) {
					counts.put(word, 1);
				} else {
					counts.put(word, counts.get(word) + 1);
				}
			}
		}

		return counts;
	}

	private Word[] toWords(Map<String, Integer> counts) {
		List<Word> words = new ArrayList<Word>();

		for (String word : counts.keySet()) {
			int count = counts.get(word);
			words.add(new Word(word, count));
		}

		return words.toArray(new Word[0]);
	}

}
