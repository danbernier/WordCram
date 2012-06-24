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

import processing.core.PApplet;
import processing.core.PConstants;

/**
 * Some pre-fab WordAnglers.
 * <p>
 * If you want a pretty typical WordAngler, it's probably in here; if you want
 * to know how to build your own WordAngler, you can learn from the source for
 * these.
 *
 * @author Dan Bernier
 */
public class Anglers {

    /**
     * @return a WordAngler that gives a random angle every time it's called.
     */
    public static WordAngler random() {
        final Random r = new Random();
        return new WordAngler() {
            public float angleFor(Word w) {
                return r.nextFloat() * PConstants.TWO_PI;
            }
        };
    }

    /**
     * @param min
     *            the lower-bound of the angle range
     * @param max
     *            the upper-bound of the angle range
     * @return a WordAngler that gives a random angle between min and max, every
     *         time it's called.
     */
    public static WordAngler randomBetween(final float min, final float max) {
        final Random r = new Random();
        final float difference = max - min;
        return new WordAngler() {
            public float angleFor(Word w) {
                return (r.nextFloat() * difference) + min;
            }
        };
    }

    /**
     * @return a WordAngler that angles all words between -7 degrees and 7
     *         degrees, for a "heaped" effect.
     */
    public static WordAngler heaped() {
        final float angle = PApplet.radians(7);
        return randomBetween(-angle, angle);
    }

    /**
     * If you want all your words to be drawn at the same angle, use this. For
     * example, {@link #horiz()} is basically implemented as
     * <code>return alwaysUse(0f);</code>.
     *
     * @see #horiz()
     * @param angle
     *            The angle all words should be rotated at.
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
     * Just like {@link #alwaysUse(float)}, but it takes multiple angles. If you
     * want all your words to be drawn at the same N angles, pass those angles
     * to {@link #alwaysUse(float)}. You can pass as many angles as you like.
     * <p>
     * For example, if you want all your words drawn on 45&deg; and 135&deg;
     * angles, use <code>Anglers.pickFrom(radians(45), radians(135))</code>.
     * {@link #hexes()} is a similar example.
     *
     * @see #alwaysUse(float)
     * @see #hexes()
     * @param angles
     *            The angles all words should be rotated at.
     * @return A WordAngler that will pick one of the angles, at random, for
     *         each word.
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
     * A WordAngler that draws all words at hexagonal angles, or (if you're a
     * bit more mathy) 0&pi;/6, 1&pi;/6, 2&pi;/6, 3&pi;/6, 4&pi;/6, and 5&pi;/6.
     * It gives a vaguely snow-flake look.
     * <p>
     * It's implemented with {@link #pickFrom(float...)}.
     * <p>
     * (In retrospect, this is probably not one you'll use very often, so it
     * might not merit a place in Anglers. But whatever.)
     *
     * @see #pickFrom(float...)
     * @return a WordAngler that draws all words at hexagonal angles.
     */
    public static WordAngler hexes() {
        float oneSixth = PConstants.TWO_PI / 6f;
        return pickFrom(0f, oneSixth, 2 * oneSixth, 3 * oneSixth, 4 * oneSixth,
                5 * oneSixth);
    }

    /**
     * A WordAngler that draws all words horizontally. It uses
     * {@link #alwaysUse(float)}.
     *
     * @return a WordAngler that draws all words horizontally.
     */
    public static WordAngler horiz() {
        return alwaysUse(0f);
    }

    /**
     * A WordAngler that draws all words vertically, pointing both up and down.
     * It uses {@link #pickFrom(float...)}.
     *
     * @return a WordAngler that draws all words vertically, pointing both up
     *         and down.
     */
    public static WordAngler upAndDown() {
        return pickFrom(PConstants.HALF_PI, -PConstants.HALF_PI);
    }

    /**
     * A WordAngler that draws 5/7 words horizontally, and the rest going up and
     * down. It makes for a pretty nice effect. A WordAngler that draws all
     * words vertically, pointing both up and down. It uses
     * {@link #pickFrom(float...)}.
     *
     * @return a WordAngler that draws most of the words horizontally, and the
     *         rest vertically.
     */
    public static WordAngler mostlyHoriz() {
        return pickFrom(0f, 0f, 0f, 0f, 0f, PConstants.HALF_PI, -PConstants.HALF_PI);
    }
}
