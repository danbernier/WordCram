import wordcram.*;

/* WordCram can tell you which word (if any) 
 * is at a given [x,y] coordinate. You can use this
 * to make words "clickable", by implementing 
 * Processing's mouseClicked() method.
 */

WordCram wordCram;

void setup() {
  size(600, 350);
  background(255);
  
  wordCram = new WordCram(this).fromTextFile("../kari-the-elephant.txt");
  wordCram.drawAll();
}

void draw() {
  // We just need an empty loop so we can click the mouse.
}

void mouseClicked() {
  Word wordAtMouseClick = wordCram.getWordAt(mouseX, mouseY);
  
  print("[" + mouseX + "," + mouseY + "]: ");
  if (wordAtMouseClick != null) {
    println(wordAtMouseClick);
  }
  else {
    println("{no word}");
  }
}
