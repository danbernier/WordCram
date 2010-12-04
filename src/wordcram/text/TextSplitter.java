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

import wordcram.Word;
import wordcram.WordCounter; // TODO delete this class, and make WordCounter package-local.
import wordcram.WordScanner; // TODO delete this class, and make WordScanner package-local.

/**
 * <h1>TextSplitter is deprecated.</h1>
 * <p>
 * It'll be removed in WordCram 0.5, if not sooner.
 * <p>
 * Use {@link TextFile} or {@link Text} instead. 
 */
@Deprecated
public class TextSplitter {

	private String stopWordsString;

	public TextSplitter() {
		this(StopWords.ENGLISH);
	}

	public TextSplitter(String stopWordsString) {
		this.stopWordsString = stopWordsString;
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
		String[] words = new WordScanner().scanIntoWords(text.toLowerCase());
		return new WordCounter(stopWordsString.toLowerCase()).count(words);
	}
}
