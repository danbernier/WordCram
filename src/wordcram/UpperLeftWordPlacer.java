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
import processing.core.PVector;

public class UpperLeftWordPlacer implements WordPlacer {

    private Random r = new Random();

    public PVector place(Word word, int wordIndex, int wordsCount, int wordImageWidth, int wordImageHeight, int fieldWidth, int fieldHeight) {
        int x = getOneUnder(fieldWidth - wordImageWidth);
        int y = getOneUnder(fieldHeight - wordImageHeight);
        return new PVector(x, y);
    }

    private int getOneUnder(int limit) {
        return PApplet.round(PApplet.map(random(random(random(random(random(1.0f))))), 0, 1, 0, limit));
    }

    private float random(float limit) {
        return r.nextFloat() * limit;
    }

}
