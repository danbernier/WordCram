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
Built with WordCram 0.2, http://code.google.com/p/wordcram/
Names collected from http://www.census.gov/genealogy/names
Minya Nouvelle font from http://www.1001fonts.com/font_details.html?font_id=59
*/

import wordcram.*;
import wordcram.text.*;

class Name extends Word {
  boolean isGirlsName;
  public Name(String name, float weight, boolean _isGirlsName) {
    super(name, weight);
    isGirlsName = _isGirlsName;
  }
}

WordCram wc;
Name[] names;

void setup() {
  colorMode(HSB);
  size(800, 600);
  background(255);
  
  String[] nameData = loadStrings("names.txt");
  names = new Name[nameData.length];
  for (int i = 0; i < names.length; i++) {
    names[i] = parseName(nameData[i]);
  }
  
  initWordCram();
}

void initWordCram() {
  wc = new WordCram(this, names,
   		    Fonters.alwaysUse(createFont("MINYN___.TTF", 1)),
		    Sizers.byWeight(12, 60),
		    colorer(), 
		    Anglers.mostlyHoriz(),
		    Placers.horizLine());
}

WordColorer colorer() {
  return new WordColorer() {
    public int colorFor(Word w) {
      return ((Name)w).isGirlsName ? color(#f36d91) : color(#476dd5);
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
  initWordCram();
  loop();
}


Name parseName(String data) {
  String[] parts = split(data, '\t');
  String name = parts[0];
  float freq = float(parts[1]);
  boolean isGirl = "f".equals(parts[2]);
  return new Name(name, freq, isGirl);
}
