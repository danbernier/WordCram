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

/*
Built with WordCram 0.3, http://code.google.com/p/wordcram/
 */

import wordcram.*;

WordCram wordCram;

void setup() {
  size(800, 600);
  background(255);
  colorMode(HSB);

  initWordCram();
}

void initWordCram() {
  Word[] words = new Word[266];
  for (int i = 0; i < words.length; i++) {
    words[i] = new Word(i % 2 == 0 ? "word" : "cram", 1);
  }

  wordCram = new WordCram(this).fromWords(words)
    .withSizer(Sizers.byWeight(20, 20))
    .withColors(color(0))
    .withAngler(
      new WordAngler() {
        public float angleFor(Word w) {
          return w.word.equals("word") ? 0 : HALF_PI;
        }
      })
    .withPlacer(new WordPlacer() {
      public PVector place(Word w, int wordIndex, int wordsCount, int wordWidth, int wordHeight, int fieldWidth, int fieldHeight) {
        int columns = 17;
        return new PVector(map(wordIndex % columns, 0, columns, 0, fieldWidth),
                           map(floor(wordIndex / (float)columns), 0, 15, 0, height));
        //return new PVector(random(fieldWidth - wordWidth), random(fieldHeight - wordHeight));
      }
    });
}

int count = 0;
void draw() {
  /*
  if (wordCram.hasMore()) {
    wordCram.drawNext();
    count++;
  }
  else {
    println("done " + count);
    noLoop();
  }
  */
  wordCram.drawAll();
  noLoop();
}

void mouseClicked() {
  background(255);
  initWordCram();
  loop();
}

