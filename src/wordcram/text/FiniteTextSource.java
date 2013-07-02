package wordcram.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import wordcram.Word;

/**
 * 
 * This class takes a set of input words and generates text to layout from those words.
 * The usecase for this class is when the created cram shall be rather big, but the number of words is limited.
 * @author simpsus
 *
 */
public class FiniteTextSource {
	
	private List<Word> words = new ArrayList<Word>();
	public static double STEP = 0.2;
	
	public void addWeight(String word, Float weight, Integer appearances) {
		for (int i=0;i<appearances;i++) {
			words.add(new Word(word,weight));
		}
	}
	
	public Word[] getWords() {
		Word[] arr = new Word[words.size()];
		// The words are shuffled so that weights of equal weight and text will not be clumped
		// and take space from other words with that weight
		Collections.shuffle(words);
		System.out.println("returning " + words.size());
		int i =0;
		for (Word w:words) {
			arr[i++] = w;
		}
		return arr;
	}
	
	/**
	 * generates a textsource that evenly distributes the strings in text,
	 * both in number and in weight, to a total of up to <code>number</code>
	 * words. A sane lower bound for <code>number</code> would be 
	 * <code>text.length / STEP</code>
	 */
	public static FiniteTextSource fromStrings(String[] text, int number) {
		FiniteTextSource result = new FiniteTextSource();
		for (int i=0;i<text.length;i++) {
			for (float k=1;k<=1/STEP;k++) {
				result.addWeight(text[i], new Float(1/(Math.pow(2,(1/STEP - k)))), (int) ((number/text.length) / Math.pow(2, k)));
			}
		}
		return result;
	}
	
		
}
