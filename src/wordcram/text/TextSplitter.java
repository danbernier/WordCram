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

public class TextSplitter {

	private Set<String> stopWords;

	public TextSplitter() {
		this(StopWords.ENGLISH);
	}
	public TextSplitter(String stopWordsString) {
		String[] stopWordsArray = splitIntoWords(stopWordsString);
		stopWords = new HashSet<String>();
		for (String stopWord : stopWordsArray) {
			stopWords.add(stopWord);
		}
	}

	public Word[] split(String[] strings) {
		StringBuffer sb = new StringBuffer();
		for (String s : strings) {
			sb.append(s);
			sb.append(" ");
		}
		return split(sb.toString());
	}

	public Word[] split(String text) {
		return weight(count(splitIntoWords(text)));
	}

	protected String[] splitIntoWords(String text) {
		return text.trim().toLowerCase().replaceAll("--", " ").replaceAll(
				"[^a-z\\s]+", "").split("\\s+");
	}

	protected Map<String, Integer> count(String[] words) {
		HashMap<String, Integer> counts = new HashMap<String, Integer>();

		for (String word : words) {
			if (!stopWords.contains(word)) {
				if (!counts.containsKey(word)) {
					counts.put(word, 1);
				} else {
					counts.put(word, counts.get(word) + 1);
				}
			}
		}

		return counts;
	}

	protected Word[] weight(Map<String, Integer> counts) {
		SortedSet<Word> words = new TreeSet<Word>();

		for (String word : counts.keySet()) {
			int count = counts.get(word);
			words.add(new Word(word, count));
		}

		return words.toArray(new Word[0]);
	}
}
