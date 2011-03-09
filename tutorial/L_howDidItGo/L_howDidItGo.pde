import wordcram.*;

/* TODO getWordAt(), and check the list for other methods.
 */

void setup() {
  size(600, 350);
  background(255);
  
  WordCram wordCram = new WordCram(this)
    .fromTextFile("../kari-the-elephant.txt");
    
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
