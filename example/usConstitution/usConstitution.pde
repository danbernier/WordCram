/*
Built with WordCram 0.2, http://code.google.com/p/wordcram/
US Constitution text from http://www.usconstitution.net/const.txt
Liberation Serif font from RedHat: https://www.redhat.com/promo/fonts/
*/

import wordcram.*;
import wordcram.text.*;

WordCram wordCram;
Word[] words;

void setup() {
  size(800, 600);
  background(255);
  colorMode(HSB);
  
  String[] usConst = loadStrings("usconst.txt");
  words = new TextSplitter(StopWords.ENGLISH + " shall").split(usConst);
  initWordCram();
}

void initWordCram() {
  wordCram = new WordCram(this,
      words,
      Fonters.alwaysUse(createFont("LiberationSerif-Regular.ttf", 1)),
      Sizers.byWeight(10, 90),
      Colorers.pickFrom(color(0, 250, 200), color(30), color(170, 230, 200)),
      Anglers.mostlyHoriz(),
      Placers.horizLine());
}

void draw() {
  if (wordCram.hasMore()) {
    wordCram.drawNext();
  }
}

void mouseClicked() {
  background(255);
  initWordCram();
}
