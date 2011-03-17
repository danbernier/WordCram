import wordcram.*;

/* After the WordCram has run, you can find out how many
 * words were skipped (and why they were skipped), how many 
 * were placed (and where they were placed), and which word
 * (if any) is at a given [x,y] coordinate.
 */

WordCram wordCram;

void setup() {
  size(600, 350);
  background(255);
  
  wordCram = new WordCram(this).fromTextFile("../kari-the-elephant.txt");
  wordCram.drawAll();
  
  Word[] words  = wordCram.getWords();  
  Word[] skippedWords = wordCram.getSkippedWords();
  //println(skippedWords);  // Probably a long list!
  println("Placed " + (words.length - skippedWords.length) + 
          " words out of " + words.length);

  Word word = words[200];
  
  // This will show either where the word was placed, or why it was skipped.
  println(word);
  
  if (word.wasPlaced()) {
    println(word.word + " was placed!");
  }
  if (word.wasSkipped()) {
    println(word.word + " was skipped!");
    
    if (word.wasSkippedBecause() == WordCram.NO_SPACE) {
      println(word.word + " was skipped because there was no room");
    }
  }
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
