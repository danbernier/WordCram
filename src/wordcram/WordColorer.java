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
 * A WordColorer tells WordCram what color to render a word in.
 * <p>
 * <b>Note:</b> if you implement your own WordColorer, you should be familiar
 * with how <a href="http://processing.org/reference/color_datatype.html"
 * target="blank">Processing represents colors</a> -- or just make sure it uses
 * Processing's <a href="http://processing.org/reference/color_.html"
 * target="blank">color</a> method.
 * <p>
 * Some useful implementations are available in {@link Colorers}.
 *
 * @author Dan Bernier
 */
public interface WordColorer {

    /**
     * What color should this {@link Word} be?
     *
     * @param word the word to pick the color for
     * @return the color for the word
     */
    public int colorFor(Word word);
}
