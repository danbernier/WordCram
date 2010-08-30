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

public class Anglers implements PConstants {
	public static WordAngler Horiz = constant(0f);
	public static WordAngler UpAndDown = fromArray(
			HALF_PI, -HALF_PI);
	public static WordAngler MostlyHoriz = fromArray(0f, 0f,
			0f, 0f, 0f, HALF_PI, -HALF_PI);

	public static WordAngler Random() {
		final Random r = new Random();
		return new WordAngler() {
			public float angleFor(Word w) {
				return r.nextFloat() * TWO_PI;
			}
		};
	}

	private static float oneSixth = TWO_PI / 6f;
	public static WordAngler Hexes = fromArray(0f, oneSixth,
			2 * oneSixth, 3 * oneSixth, 4 * oneSixth, 5 * oneSixth);
	
	private static WordAngler constant(final float angle) {
		return new WordAngler() {
			public float angleFor(Word w) {
				return angle;
			}
		};
	}
	
	private static WordAngler fromArray(final float... angles) {
		final Random r = new Random();
		return new WordAngler() {
			public float angleFor(Word w) {
				return angles[r.nextInt(angles.length)];
			}
		};
	}
}
