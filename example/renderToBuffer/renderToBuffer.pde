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
US Constitution text from http://www.usconstitution.net/const.txt
*/

import wordcram.*;

WordCram wc;
PGraphics buffer;

void setup() {
  size(640, 480);
  background(255);
  
  buffer = createGraphics(width, height, JAVA2D);
  buffer.beginDraw();
  buffer.background(255);
  
  wc = new WordCram(this)
    .fromTextFile("../usconst.txt")
    .withCustomCanvas(buffer)
    .withColors(color(255,0,0), color(0), color(0,0,255)) // red, black, and blue
    .sizedByWeight(9, 70);
  
  textAlign(CENTER);
  textFont(createFont("sans", 20));
}

void draw() {
  
  background(255);
  
  if (wc.hasMore()) {  
    
    // draw the progress bar
    float progress = wc.getProgress();
    drawProgressBar(progress);
    drawProgressText(progress);
    
    wc.drawNext();
  }
  else {
    
    buffer.endDraw();
    image(buffer, 0, 0);
    
    println("done.");
    noLoop();
  }
}

void drawProgressBar(float progress) {
  color gray = color(progress * 255);
  
  // Draw the empty box:
  noFill();
  stroke(gray);
  strokeWeight(2);
  rect(100, (height/2)-30, (width-200), 60);
  
  // Fill in the portion that's done:
  fill(gray);
  rect(100, (height/2)-30, (width-200) * progress, 60);
}

void drawProgressText(float progress) {
  text(round(progress * 100) + "%", width/2, (height/2)+50);
}
