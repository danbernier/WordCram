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

/**
 * A WordAngler tells WordCram what angle to draw a word at, in radians.
 * <p>
 * Some useful implementations are available in {@link Anglers}.
 *
 * @author Dan Bernier
 */
public interface WordAngler {

    /**
     * What angle should this {@link Word} be rotated at?
     *
     * @param word
     *            The Word that WordCram is about to draw, and wants to rotate
     * @return the rotation angle for the Word, in radians
     */
    public float angleFor(Word word);
}
