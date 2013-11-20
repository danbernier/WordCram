/*
This is as basic as it gets.  If you can't get this running, 
something is not quite right.
*/

import wordcram.*;

size(700, 400);
background(255);

// Each Word object has its word, and its weight.  You can use whatever
// numbers you like for their weights, and they can be in any order.
Word[] wordArray = new Word[] {
    new Word("Hello", 100),
    new Word("WordCram", 60)
  };

// Pass in the sketch (the variable "this"), so WordCram can draw to it.
WordCram wordcram = new WordCram(this)

// Pass in the words to draw.
  .fromWords(wordArray);

// Now we've created our WordCram, we can draw it:
wordcram.drawAll();
