package wordcram.text;

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

public class TextFile implements TextSource {

    private String path;

    // TODO if we move all .text.* classes into WordCram, we can kill this, and
    // use pkg-local methods for setting the parent...
    private PApplet parent;

    public TextFile(String path, PApplet parent) {
        this.path = path;
        this.parent = parent;
    }

    public String getText() {
        return PApplet.join(parent.loadStrings(path), ' ');
    }

}
