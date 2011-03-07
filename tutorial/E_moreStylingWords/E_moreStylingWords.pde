import wordcram.*;

PFont georgia = createFont("Georgia", 1);
PFont georgiaItalic = createFont("Georgia Italic", 1);

size(600, 350);
background(255);

/* If WordCram's convenience methods for styling words
 * don't provide what you need, you can use its more
 * general methods instead: withColorer, withAngler,
 * withFonter.
 *
 * See the javadoc for more options:
 * - http://wordcram.googlecode.com/svn/javadoc/wordcram/Colorers.html
 * - http://wordcram.googlecode.com/svn/javadoc/wordcram/Anglers.html
 * - http://wordcram.googlecode.com/svn/javadoc/wordcram/Fonters.html
 */

new WordCram(this)
  .fromTextFile("../kari-the-elephant.txt")
  
  //-----------
  // Colorers
  .withColorer(Colorers.alwaysUse(#0000AA))
  //.withColorer(Colorers.twoHuesRandomSatsOnWhite(this))
  //.withColorer(Colorers.pickFrom(#FF0000, #00CC00, #0000FF))
  //.withColorer(Colorers.alwaysUse(color(0, 20, 150, 150)))
  
  //-----------
  // Anglers
  .withAngler(Anglers.horiz())
  //.withAngler(Anglers.heaped())
  //.withAngler(Anglers.hexes())
  //.withAngler(Anglers.upAndDown())
  //.withAngler(Anglers.randomBetween(0, PI/8))
  
  //---------
  // Fonters
  .withFonter(Fonters.alwaysUse(georgia))
  //.withFonter(Fonters.pickFrom(georgia, georgiaItalic))
  
  .drawAll();
