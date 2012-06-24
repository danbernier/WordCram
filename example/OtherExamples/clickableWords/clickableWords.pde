import wordcram.*;

/*
How to make a wordcram that pops up extra information
when you click on a word.

Each frame, we want to draw the wordcram, and some info
about the word that's been clicked on (or hovered over).
Each frame, you need to clean up what the last frame drew.

Since laying out a wordcram is expensive, and there's
(currently) no way to quickly re-render a wordcram, 
we don't want to make the wordcram in draw().
(Besides, each frame would probably come out different.)
Instead, render the wordcram in the setup() method, and 
cache it as a PImage. Then, in draw(), render that image
first, which will overlay the last frame's image.



*/

WordCram wc; // We'll ask the wordcram which word is at the mouse coordinates
PImage cachedImage; // Cache the rendered wordcram, so drawing is fast
Word lastClickedWord; // The word that was under the user's last click

void setup() {
  size(700, 400);
  background(255);
  
  // Make the wordcram
  wc = new WordCram(this).fromWebPage("http://wikipedia.org");
  wc.drawAll();
  
  // Save the image of the wordcram
  cachedImage = get();
  
  // Set up styles for when we draw stuff to the screen (later)
  textFont(createFont("sans", 150));
  textAlign(CENTER, CENTER);
}

void draw() {
  // First, wipe out the last frame: re-draw the cached image
  image(cachedImage, 0, 0);
  
  // If the user's last click was on a word, render it big and blue:
  if (lastClickedWord != null) {
    noStroke();
    fill(255, 190);
    rect(0, height/2 - textAscent()/2, width, textAscent() + textDescent());

    fill(30, 144, 13, 150);
    text(lastClickedWord.word, width/2, height/2);
  }
}

void mouseClicked() {
  lastClickedWord = wc.getWordAt(mouseX, mouseY);
}
