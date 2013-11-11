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

import processing.core.PApplet;
import processing.core.PVector;

/**
 * If you're using a custom WordPlacer, and having difficulty seeing
 * how well it works, try wrapping it in a PlottingWordPlacer. As your
 * WordCram is drawn, it'll render tiny dots at each word's target
 * location, so you can sort-of see how far off they are when they're
 * finally rendered.
 */
public class PlottingWordPlacer implements WordPlacer {

    private PApplet parent;
    private WordPlacer wrappedPlacer;

    public PlottingWordPlacer(PApplet _parent, WordPlacer _wrappedPlacer) {
        parent = _parent;
        wrappedPlacer = _wrappedPlacer;
    }

    public PVector place(Word word, int wordIndex, int wordsCount, int wordImageWidth, int wordImageHeight, int fieldWidth, int fieldHeight) {
        PVector v = wrappedPlacer.place(word, wordIndex, wordsCount, wordImageWidth, wordImageHeight, fieldWidth, fieldHeight);
        parent.pushStyle();
        parent.noFill();

        parent.stroke(15, 255, 255, 200);

        parent.ellipse(v.x, v.y, 10, 10);
        parent.popStyle();
        return v;
    }

}
