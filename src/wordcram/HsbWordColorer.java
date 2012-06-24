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
import processing.core.PConstants;

abstract class HsbWordColorer implements WordColorer {
    private PApplet host;
    private int range;

    HsbWordColorer(PApplet host) {
        this(host, 255);
    }
    HsbWordColorer(PApplet host, int range) {
        this.host = host;
        this.range = range;
    }

    public int colorFor(Word word) {
        host.pushStyle();
        host.colorMode(PConstants.HSB, range);
        int color = getColorFor(word);
        host.popStyle();
        return color;
    }

    abstract int getColorFor(Word word);
}
