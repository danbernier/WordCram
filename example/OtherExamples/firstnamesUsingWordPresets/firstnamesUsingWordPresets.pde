/*
firstnamesUsingWordPresets is a WordCram of the most popular first names from 
the 1990 US Census.  Predictably, males are blue, and females
are pink.  It also shows one way you can use a custom WordColorer.
See firstnamesUsingSubclasses for another.

When you're parsing the names data, you know whether each word
is a male or female name.  To take advantage of this, the sketch
creates a Word object, and pre-sets its color to blue or pink,
via the setColor() method.

Names collected from http://www.census.gov/genealogy/names
Minya Nouvelle font from http://www.1001fonts.com/font_details.html?font_id=59
*/

import wordcram.*;

WordCram wc;
Word[] names;

void setup() {
  colorMode(HSB);
  size(800, 600);
  background(255);
  
  loadNames();
  makeWordCram();
}

void loadNames() { 
  String[] nameData = loadStrings("../../names.txt");
  names = new Word[nameData.length];
  for (int i = 0; i < names.length; i++) {
    names[i] = parseName(nameData[i]);
  }
}

void makeWordCram() {
  wc = new WordCram(this)
    .fromWords(names)
    .withFont(createFont("../../MINYN___.TTF", 1))
    .sizedByWeight(12, 60);
}

void draw() {
  if (wc.hasMore()) {
    wc.drawNext();
  }
  else {
    println("done");
    noLoop();
  }
}

void mouseClicked() {
  background(255);
  makeWordCram();
  loop();
}


// Each row looks like:
// Mary\t1.0\tf
// ...or:
// {name} {tab} {frequency} {tab} {'f' for females, 'm' for males}
Word parseName(String data) {
  String[] parts = split(data, '\t');
  
  String name = parts[0];
  float frequency = float(parts[1]);
  boolean isFemale = "f".equals(parts[2]);
  
  Word word = new Word(name, frequency);
  
  if (isFemale) {
    word.setColor(#f36d91); // pink
  }
  else {
    word.setColor(#476dd5); // blue
  }
  
  return word;
}

