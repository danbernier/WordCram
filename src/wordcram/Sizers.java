package wordcram;

import processing.core.PApplet;

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

/**
 * Sizers contains pre-made {@link WordSizer} implementations that you might
 * find useful. Right now, it's only {@link Sizers#byWeight(int, int)} and
 * {@link Sizers#byRank(int, int)}.
 * @see WordSizer
 */
public class Sizers {

    /**
     * Returns a WordSizer that sizes words by their weight, where the
     * "heaviest" word will be sized at <code>maxSize</code>.
     * <p>
     * To be specific, the font size for each word will be calculated with:
     *
     * <pre>
     * PApplet.lerp(minSize, maxSize, (float) word.weight)
     * </pre>
     *
     * @param minSize
     *            the size to draw a Word with weight = 0
     * @param maxSize
     *            the size to draw a Word with weight = 1
     * @return the WordSizer
     */
    public static WordSizer byWeight(final int minSize, final int maxSize) {
        return new WordSizer() {
            public float sizeFor(Word word, int wordRank, int wordCount) {
                return PApplet.lerp(minSize, maxSize, word.weight);
            }
        };
    }

    /**
     * Returns a WordSizer that sizes words by their rank. The first word will
     * be sized at <code>maxSize</code>.
     * <p>
     * To be specific, the font size for each word will be calculated with:
     *
     * <pre>
     * PApplet.map(wordRank, 0, wordCount, maxSize, minSize)
     * </pre>
     *
     * @param minSize
     *            the size to draw the last Word
     * @param maxSize
     *            the size to draw the first Word
     * @return the WordSizer
     */
    public static WordSizer byRank(final int minSize, final int maxSize) {
        return new WordSizer() {
            public float sizeFor(Word word, int wordRank, int wordCount) {
                return PApplet.map(wordRank, 0, wordCount, maxSize, minSize);
            }
        };
    }

    // TODO try exponent scales, rather than linear.
}
