package wordcram;

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

	public Word[] count(String text, RenderOptions renderOptions) {
		if (cueStopWords == null) {
			cueStopWords = StopWords.guess(text);

            if (cueStopWords == StopWords.Arabic ||
                cueStopWords == StopWords.Farsi ||
                cueStopWords == StopWords.Hebrew) {
                renderOptions.rightToLeft = true;
            }

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
