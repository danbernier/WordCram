package wordcram;

import java.awt.Font;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import processing.core.PFont;

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

class WordShaper {
	
	private WordSizer sizer;
	private WordFonter fonter;
	private WordAngler angler;
	private FontRenderContext frc;
	
	public WordShaper(WordSizer sizer, WordFonter fonter, WordAngler angler) {
		this.sizer = sizer;
		this.fonter = fonter;
		this.angler = angler;
		this.frc = new FontRenderContext(null, true, true);
	}
	
	/*
	 * TODO question here: you want to eliminate as many words as possible, so FIRST rip through & render all their shapes,
	 * and stop once the shapes are too small.  Then you can shorten the arrays, and loop through less.
	 * This is also good because now, your WordPlacers will have better ranks to go on: if 75% of the words are too small
	 * to render, then the lowest word will have a 25th-percentile rank, and it'll place them in only (eh) 25% of the 
	 * field.  Basically, it's like you're lying to the Placer.  Cutting down the list first will give you a better
	 * answer to "how many words am i drawing here?".
	 * 
	 * BUT: won't that screw with your weights?  Maybe?  Er, maybe not?  Not sure. 
	 */
	public Shape[] shapeWords(Word[] words) {

		ArrayList<Shape> shapes = new ArrayList<Shape>();
		
		for (int i = 0; i < words.length; i++) {			
			Word word = words[i];
			
			Shape wordShape = shapeWord(word, i, words.length);
			if (wordShape == null) {
				break;
			}
			
			shapes.add(wordShape);
		}
		
		return shapes.toArray(new Shape[0]);
	}
	
	private Shape shapeWord(Word word, int wordRank, int wordCount) {

		float fontSize = sizer.sizeFor(word, wordRank, wordCount); 
		PFont pFont = fonter.fontFor(word);
		float rotation = angler.angleFor(word);
		
		Font font = pFont.getFont().deriveFont(fontSize);
		char[] chars = word.word.toCharArray();
		
		// TODO hmm: this doesn't render newlines.  Hrm.  If you're word text is "foo\nbar", you get "foobar".
		GlyphVector gv = font.layoutGlyphVector(frc, chars, 0, chars.length,
				Font.LAYOUT_LEFT_TO_RIGHT);

		Shape shape = gv.getOutline();

		if (rotation != 0.0) {
			shape = AffineTransform.getRotateInstance(rotation)
					.createTransformedShape(shape);
		}
		
		Rectangle2D rect = shape.getBounds2D();
		int minWordRenderedSize = 7; // TODO extract config setting for minWordRenderedSize
		if (rect.getWidth() < minWordRenderedSize || rect.getHeight() < minWordRenderedSize) {
			return null;		
		}
		
		shape = AffineTransform.getTranslateInstance(-rect.getX(), -rect.getY()).createTransformedShape(shape);
		
		return shape;
	}
	
}
