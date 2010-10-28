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
This is as basic as it gets.  If you can't get this running, 
something is not quite right.
*/

import wordcram.*;

size(700, 400);
background(0);

// Each Word object has its word, and its weight.  You can use whatever
// numbers you like for their weights, and they can be in any order.
Word[] wordArray = new Word[] {
    new Word("Hello", 100),
    new Word("WordCram", 60)
  };

// Pass in the sketch (the variable "this"), so WordCram can draw to it.
WordCram wordcram = new WordCram(this)

// Pass in the words to draw.
  .forWords(wordArray);

// Now we've created our WordCram, we can draw it:
wordcram.drawAll();
