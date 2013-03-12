package wordcram.text;

import java.util.ArrayList;
import java.util.List;

import wordcram.Word;

/**
 * 
 * This class takes a set of input words and generates text to layout from those words.
 * The usecase for this class is when the created cram shall be rather big, but the number of words is limited.
 * There are methods to control the weighting and the number of words
 * @author simpsus
 *
 */
public class FiniteTextSource {
	
	private List<Word> words = new ArrayList<Word>();
	
	public void addWeight(String word, Integer weight, Integer appearances) {
		for (int i=0;i<appearances;i++) {
			words.add(new Word(word,weight));
		}
	}
	
	public Word[] getWords() {
		Word[] arr = new Word[words.size()];
		System.out.println("returning " + words.size());
		int i =0;
		for (Word w:words) {
			arr[i++] = w;
		}
		return arr;
	}
	
		
}
