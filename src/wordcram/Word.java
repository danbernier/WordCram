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

import processing.core.PFont;
import processing.core.PVector;

/**
 * A weighted word, for rendering in the word cloud image.
 * <p>
 * Each Word object has a {@link #word} String, and its associated {@link #weight}, and it's constructed
 * with these two things.
 * 
 * 
 * <h3>Hand-crafting Your Words</h3>
 * 
 * If you're creating your own <code>Word[]</code> to pass
 * to the WordCram (rather than using something like {@link WordCram#fromWebPage(String)}),
 * you can specify how a Word should be rendered: set a Word's font, size, etc:
 * 
 * <pre>
 * Word w = new Word("texty", 20);
 * w.setFont(createFont("myFontName", 1));
 * w.setAngle(radians(45));
 * </pre>
 * 
 * Any values set on a Word will override the corresponding component ({@link WordColorer}, 
 * {@link WordAngler}, etc) - it won't even be called for that word.
 * 
 * <h3>Word Properties</h3>
 * A word can also have properties. If you're creating custom components,
 * you might want to send other information along with the word, for the components to use:
 * 
 * <pre>
 * Word strawberry = new Word("strawberry", 10);
 * strawberry.setProperty("isFruit", true);
 * 
 * Word pea = new Word("pea", 10);
 * pea.setProperty("isFruit", false);
 * 
 * new WordCram(this)
 *   .fromWords(new Word[] { strawberry, pea })
 *   .withColorer(new WordColorer() {
 *      public int colorFor(Word w) {
 *        if (w.getProperty("isFruit") == true) {
 *          return color(255, 0, 0);
 *        }
 *        else {
 *          return color(0, 200, 0);
 *        }
 *      }
 *    });
 * </pre>
 * 
 * @author Dan Bernier
 */
public class Word implements Comparable<Word> {
	
	public String word;
	public double weight;

	private Float size;
	private Float angle;
	private PFont font;
	private Integer color;
	private PVector place;
	
	private float renderedSize;
	private float renderedAngle;
	private PFont renderedFont;
	private int renderedColor;
	private PVector targetPlace;
	private PVector renderedPlace;
	
	private HashMap<String,Object> properties = new HashMap<String,Object>();
	
	public Word(String word, double weight) {
		this.word = word;
		this.weight = weight;
	}
	
	/**
	 * Set the size this Word should be rendered at - WordCram won't even call the WordSizer.
	 */
	public void setSize(float size) {
		this.size = size;
	}
	
	/**
	 * Set the angle this Word should be rendered at - WordCram won't even call the WordAngler.
	 */
	public void setAngle(float angle) {
		this.angle = angle;
	}
	
	/**
	 * Set the font this Word should be rendered in - WordCram won't call the WordFonter.
	 */
	public void setFont(PFont font) {  // TODO provide a string overload? Will need the PApplet...
		this.font = font;
	}
	
	/**
	 * Set the color this Word should be rendered in - WordCram won't call the WordColorer.
	 */
	public void setColor(int color) {  // TODO provide a 3-float overload? 4-float? 2-float? Will need the PApplet...
		this.color = color;
	}
	
	/**
	 * Set the place this Word should be rendered at - WordCram won't call the WordPlacer.
	 */
	public void setPlace(PVector place) {
		this.place = place.get();
	}
	
	/**
	 * Set the place this Word should be rendered at - WordCram won't call the WordPlacer.
	 */
	public void setPlace(float x, float y) {
		this.place = new PVector(x, y);
	}

	/*
	 * These methods are called by EngineWord: they return (for instance)
	 * either the color the user set via setColor(), or the value returned
	 * by the WordColorer. They're package-local, so they can't be called by the sketch.
	 */
	
	Float getSize(WordSizer sizer, int rank, int wordCount) {
		renderedSize = size != null ? size : sizer.sizeFor(this, rank, wordCount);
		return renderedSize;
	}
	
	Float getAngle(WordAngler angler) {
		renderedAngle = angle != null ? angle : angler.angleFor(this);
		return renderedAngle;
	}
	
	PFont getFont(WordFonter fonter) {
		renderedFont = font != null ? font : fonter.fontFor(this);
		return renderedFont;
	}
	
	Integer getColor(WordColorer colorer) {
		renderedColor = color != null ? color : colorer.colorFor(this);
		return renderedColor;
	}
	
	PVector getTargetPlace(WordPlacer placer, int rank, int count, int wordImageWidth, int wordImageHeight, int fieldWidth, int fieldHeight) {
		targetPlace = place != null ? place : placer.place(this, rank, count, wordImageWidth, wordImageHeight, fieldWidth, fieldHeight);
		return targetPlace;
	}
	
	void setRenderedPlace(PVector place) {
		renderedPlace = place.get();
	}

	/**
	 * Get the size the Word was rendered at: either the value passed to setSize(), or the value returned from the WordSizer. 
	 * @return the rendered size
	 */
	public float getRenderedSize() {
		return renderedSize;
	}

	/**
	 * Get the angle the Word was rendered at: either the value passed to setAngle(), or the value returned from the WordAngler. 
	 * @return the rendered angle
	 */
	public float getRenderedAngle() {
		return renderedAngle;
	}

	/**
	 * Get the font the Word was rendered in: either the value passed to setFont(), or the value returned from the WordFonter. 
	 * @return the rendered font
	 */
	public PFont getRenderedFont() {
		return renderedFont;
	}

	/**
	 * Get the color the Word was rendered in: either the value passed to setColor(), or the value returned from the WordColorer. 
	 * @return the rendered color
	 */
	public int getRenderedColor() {
		return renderedColor;
	}

	/**
	 * Get the place the Word was supposed to be rendered at: either the value passed to setPlace(), 
	 * or the value returned from the WordPlacer.
	 */
	public PVector getTargetPlace() {
		return targetPlace;
	}

	/**
	 * Get the final place the Word was rendered at, or null if it couldn't be placed.
	 * It returns the original target location (which is either the value passed to setPlace(), 
	 * or the value returned from the WordPlacer), plus the nudge vector returned by the WordNudger.
	 * @return If word was placed, it's the (x,y) coordinates of the word's final location; else it's null.
	 */
	public PVector getRenderedPlace() {
		return renderedPlace;
	}
	
	/**
	 * Indicates whether the Word was placed successfully. It's the same as calling word.getRenderedPlace() != null.
	 * @return true only if the word was placed.
	 */
	public boolean wasPlaced() {
		return renderedPlace != null;
	}

	/**
	 * Get a property value from this Word, for a WordColorer, a WordPlacer, etc.
	 * @param propertyName
	 * @return the value of the property, or <code>null</code>, if it's not there.
	 */
	public Object getProperty(String propertyName) {
		return properties.get(propertyName);
	}
	
	/**
	 * Set a property on this Word, to be used by a WordColorer, a WordPlacer, etc, down the line.
	 * @param propertyName
	 * @param propertyValue
	 */
	public void setProperty(String propertyName, Object propertyValue) {
		properties.put(propertyName, propertyValue);
	}
	
	/**
	 * Displays the word, and its weight (in parentheses).
	 * <code>new Word("hello", 1.3).toString()</code> will return "hello (0.3)".
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
