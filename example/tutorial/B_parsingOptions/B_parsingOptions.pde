import wordcram.*;
import wordcram.text.*;

size(600, 350);
background(255);

/*
 * You can control how WordCram counts up the
 * words from your text.
 * It can include or exclude numbers (42, or 3.14159),
 * it can convert all your words to upper- or lower-case,
 * or leave them as they are,
 * and it can filter out words you don't want to see.
 *
 * Run the sketch as-is to see how it looks,
 * then try out the methods below.
 */

new WordCram(this)
  .fromTextFile("../kari-the-elephant.txt")
  
  //.excludeNumbers()
  //.includeNumbers()  // the default
  
  //.lowerCase()
  //.upperCase()
  //.keepCase()  // the default
  
  /*
   * Tell WordCram which words to skip by calling
   * withStopWords() with a space-delimited string.
   * The case of the words doesn't matter: "Elephant"
   * will filter out "elephant", "ELEPHANT", and
   * "eLePhAnT".
   */
  //.withStopWords("Kari jungle Elephant")
  
  .drawAll();
