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

import java.util.HashMap;

/**
 * A weighted word, for rendering in the word cloud image.
 * <p>
 * Each Word object has a {@link #word} String, and its associated {@link #weight}, and it's constructed
 * with these two things.
 * <p>
 * A word can also have properties.  If you're creating your own <code>Word[]</code> to pass
 * to the WordCram (rather than using something like {@link WordCram#fromWebPage(String)}),
 * and custom components ({@link WordColorer}, {@link WordAngler}, etc),
 * you might want to send other information along with the word, for the components to use.
 * 
 * 
 * 
 * <p>
 * Besides that, WordCram will set a few properties of its own on your words:
 * 
 * <ul>
 * <li>place: the PVector returned by the {@link WordPlacer}</li>
 * <li>finalPlace: the PVector location where the word is finally rendered, or null if it isn't rendered.x</li>
 * </ul>
 * 
 * @author Dan Bernier
 */
public class Word implements Comparable<Word> {
	public String word;
	public double weight;
	
	private HashMap<String,Object> properties = new HashMap<String,Object>();
		
	public Word(String word, double weight) {
		this.word = word;
		this.weight = weight;
	}

	/**
	 * Get a property value from this Word, for a WordColorer, a WordPlacer, etc.
	 * <p>
	 * This is really for cases when you're weighting your own words, and passing a Word[] to the WordCram.
	 * If you're using <code>fromWebPage</code> or something like that, this won't help you much.
	 * @param propertyName
	 * @return the value of the property, or <code>null</code>, if it's not there.
	 */
	public Object getProperty(String propertyName) {
		return properties.get(propertyName);
	}
	
	/**
	 * Set a property on this Word, to be used by a WordColorer, a WordPlacer, etc, down the line.
	 * <p>
	 * This is really for cases when you're weighting your own words, and passing a Word[] to the WordCram.
	 * If you're using <code>fromWebPage</code> or something like that, this won't help you much.
	 * @param propertyName
	 * @param propertyValue
	 */
	public void setProperty(String propertyName, Object propertyValue) {
		properties.put(propertyName, propertyValue);
	}
	
	/**
	 * Displays the word, and its weight (in parentheses).
	 */
	@Override
	public String toString() {
		return word + " (" + weight + ")";
	}
	
	/**
	 * Compares Words based on weight only. Words with equal weight are arbitrarily sorted.
	 */
	@Override
	public int compareTo(Word w) {
		if (w.weight < weight) {
			return -1;
		}
		else if (w.weight > weight) {
			return 1;
		}
		else return 0;
	}
}
