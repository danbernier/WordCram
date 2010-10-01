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

import java.util.Random;

import processing.core.PConstants;

/**
 * Some pre-fab WordAnglers.
 * 
 * <p>If you want a pretty typical WordAngler, it's probably in here; if you want to know how 
 * to build your own WordAngler, you can learn from the source for these.</p> 
 * 
 * @author Dan Bernier
 */
public class Anglers implements PConstants {

	/**
	 * @return a WordAngler that gives a random angle every time it's called.
	 */
	public static WordAngler random() {
		final Random r = new Random();
		return new WordAngler() {
			public float angleFor(Word w) {
				return r.nextFloat() * TWO_PI;
			}
		};
	}

	/**
	 * If you want all your words to be drawn at the same angle, use this.  For example,
	 * <code>horiz()</code> is basically implemented as <code>return alwaysUse(0f);</code>.
	 * 
	 * @see #horiz()
	 * @param angle The angle all words should be rotated at.
	 * @return a WordAngler that always returns the given angle parameter.
	 */
	public static WordAngler alwaysUse(final float angle) {
		return new WordAngler() {
			public float angleFor(Word w) {
				return angle;
			}
		};
	}

	/**
	 * Just like <code>alwaysUse(float)</code>, but it takes multiple angles.
	 * If you want all your words to be drawn at the same N angles, pass those
	 * angles to <code>alwaysUse</code>.  You can pass as many angles as you like.
	 * 
	 * <p>For example, if you want all your words drawn on 45&deg; and 135&deg; angles,
	 * use <code>Anglers.pickFrom(radians(45), radians(135))</code>.  
	 * <code>Anglers.hexes()</code> is a similar example.
	 * 
	 * @see #alwaysUse(float)
	 * @see #hexes()
	 * @param angles The angles all words should be rotated at.
	 * @return A WordAngler that will pick one of the angles, at random, for each word. 
	 */
	public static WordAngler pickFrom(final float... angles) {
		final Random r = new Random();
		return new WordAngler() {
			public float angleFor(Word w) {
				return angles[r.nextInt(angles.length)];
			}
		};
	}

	/**
	 * A WordAngler that draws all words at hexagonal angles, or (if you're a bit more mathy)
	 * 0&pi;/6, 1&pi;/6, 2&pi;/6, 3&pi;/6, 4&pi;/6, and 5&pi;/6.  It gives a vaguely
	 * snow-flake look.
	 * 
	 * <p>In retrospect, this is probably not one you'll use very often, so it might not 
	 * merit a place in Anglers.  But whatever.
	 * 
	 * @see #pickFrom(float...)
	 * @return a WordAngler that draws all words at hexagonal angles.
	 */
	public static WordAngler hexes() {
		float oneSixth = TWO_PI / 6f;
		return pickFrom(0f, oneSixth, 2 * oneSixth, 3 * oneSixth, 4 * oneSixth, 5 * oneSixth);
	}
	
	public static WordAngler horiz() {
		return alwaysUse(0f);
	}
	
	public static WordAngler upAndDown() {
		return pickFrom(HALF_PI, -HALF_PI);
	}
	
	public static WordAngler mostlyHoriz() { 
		return pickFrom(0f, 0f, 0f, 0f, 0f, HALF_PI, -HALF_PI);
	}
}
