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
firstnamesUsingSubclasses is a WordCram of the most popular first names from 
the 1990 US Census.  Predictably, males are blue, and females
are pink.  It also shows one way you can use a custom WordColorer.
See firstnamesUsingWordProperties for another.

When you're parsing the names data, you know whether each word
is a male or female name.  To take advantage of this, the sketch 
creates a Name object, which extends WordCram's Word object with 
a boolean isFemale flag.  (Which makes sense: you could say that 
a Name is a Word, with gender.)

Then, the sketch gives WordCram a custom WordColorer that checks
each Name's isFemale flag, and returns the corresponding pink or 
blue.

Built with WordCram 0.3, http://code.google.com/p/wordcram/
Names collected from http://www.census.gov/genealogy/names
Minya Nouvelle font from http://www.1001fonts.com/font_details.html?font_id=59
*/

import wordcram.*;

// A Name is a Word, with gender.
class Name extends Word {
  boolean isFemale;
  public Name(String name, float weight, boolean _isFemale) {
    super(name, weight);
    isFemale = _isFemale;
  }
}

WordCram wc;
Name[] names;

void setup() {
  colorMode(HSB);
  size(800, 600);
  background(255);
  
  loadNames();
  makeWordCram();
}

void loadNames() { 
  String[] nameData = loadStrings("../names.txt");
  names = new Name[nameData.length];
  for (int i = 0; i < names.length; i++) {
    names[i] = parseName(nameData[i]);
  }
}

void makeWordCram() {
  wc = new WordCram(this)
    .fromWords(names)
    .withFont(createFont("../MINYN___.TTF", 1))
    .sizedByWeight(12, 60)
    .withColorer(colorer())
    .withAngler(Anglers.mostlyHoriz())
    .withPlacer(Placers.horizLine());
}

WordColorer colorer() {
  return new WordColorer() {
    public int colorFor(Word word) {
      // We can assume this Word is a Name.
      Name name = (Name)word;
      
      if (name.isFemale) {
        return color(#f36d91); // pink
      }
      else {
        return color(#476dd5); // blue
      }
    }
  };
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


Name parseName(String data) {
  String[] parts = split(data, '\t');
  String name = parts[0];
  float freq = float(parts[1]);
  boolean isFemale = "f".equals(parts[2]);
  return new Name(name, freq, isFemale);
}
