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

import java.awt.Font;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import processing.core.PFont;

class WordShaper {
	private FontRenderContext frc = new FontRenderContext(null, true, true);
	
	Shape shapeWord(EngineWord eWord) {

		float fontSize = eWord.size;
		PFont pFont = eWord.font;
		float rotation = eWord.angle;
		
		Font font = pFont.getFont().deriveFont(fontSize);
		char[] chars = eWord.word.word.toCharArray();
		
		// TODO hmm: this doesn't render newlines.  Hrm.  If you're word text is "foo\nbar", you get "foobar".
		GlyphVector gv = font.layoutGlyphVector(frc, chars, 0, chars.length,
				Font.LAYOUT_LEFT_TO_RIGHT);

		Shape shape = gv.getOutline();

		if (rotation != 0.0) {
			shape = AffineTransform.getRotateInstance(rotation)
					.createTransformedShape(shape);
		}
		
		Rectangle2D rect = shape.getBounds2D();
		int minWordRenderedSize = 7; // TODO extract config setting for minWordRenderedSize, and take height into account -- not just width
		if (rect.getWidth() < minWordRenderedSize || rect.getHeight() < minWordRenderedSize) {
			// TODO extend the notion of printSkippedWords into here, to get the first too-small rect's dimensions?
			//System.out.println("skipping " + word + " cause it's too small: " + rect.getWidth() + "x" + rect.getHeight());
			return null;		
		}
		
		shape = AffineTransform.getTranslateInstance(-rect.getX(), -rect.getY()).createTransformedShape(shape);
		
		return shape;
	}
	
}
