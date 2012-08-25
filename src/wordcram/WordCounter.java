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

import java.util.*;
import java.util.Map.Entry;

import cue.lang.Counter;
import cue.lang.WordIterator;
import cue.lang.stop.StopWords;

class WordCounter {

    private StopWords cueStopWords;
    private Set<String> extraStopWords = new HashSet<String>();
    private boolean excludeNumbers;

    public WordCounter() {
        this(null);
    }
    public WordCounter(StopWords cueStopWords) {
        this.cueStopWords = cueStopWords;
    }

    public WordCounter withExtraStopWords(String extraStopWordsString) {
        String[] stopWordsArray = extraStopWordsString.toLowerCase().split(" ");
        extraStopWords = new HashSet<String>(Arrays.asList(stopWordsArray));
        return this;
    }

    public WordCounter shouldExcludeNumbers(boolean shouldExcludeNumbers) {
        excludeNumbers = shouldExcludeNumbers;
        return this;
    }

	public Word[] count(String text) {
		if (cueStopWords == null) {
			cueStopWords = StopWords.guess(text);
			tellScripterAboutTheGuess(cueStopWords);
		}
		return countWords(text);
	}

	private void tellScripterAboutTheGuess(StopWords stopWords) {
		// TODO Find a better way to do this; it prints out during the tests. =p
		if (stopWords == null) {
			System.out.println("cue.language can't guess what language your text is in.");
		} else {
			System.out.println("cue.language guesses your text is in " + stopWords);
		}
	}

    private Word[] countWords(String text) {
        Counter<String> counter = new Counter<String>();

        for (String word : new WordIterator(text)) {
            if (shouldCountWord(word)) {
                counter.note(word);
            }
        }

        List<Word> words = new ArrayList<Word>();

        for (Entry<String, Integer> entry : counter.entrySet()) {
            words.add(new Word(entry.getKey(), (int)entry.getValue()));
        }

        return words.toArray(new Word[0]);
    }

    private boolean shouldCountWord(String word) {
        return !isStopWord(word) && !(excludeNumbers && isNumeric(word));
    }

    private boolean isNumeric(String word) {
        try {
            Double.parseDouble(word);
            return true;
        }
        catch (NumberFormatException x) {
            return false;
        }
    }

    private boolean isStopWord(String word) {
    	boolean cueSaysStopWord = cueStopWords != null && cueStopWords.isStopWord(word);
    	boolean extraSaysStopWord = extraStopWords.contains(word.toLowerCase());
        return  cueSaysStopWord || extraSaysStopWord;
    }

}
