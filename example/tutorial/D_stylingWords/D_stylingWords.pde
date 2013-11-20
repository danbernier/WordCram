import wordcram.*;

PFont georgia = createFont("Georgia", 1);
PFont georgiaItalic = createFont("Georgia Italic", 1);
PFont minyaNouvelle = createFont("../MINYN___.TTF", 1);

size(600, 350);
background(255);

/* You can control what font(s) your words are drawn in,
 * what color(s) they're drawn in, and what angle(s)
 * they're drawn at.
 * - Use any Processing colors
 * - Angles are in radians
 * - Use any Processing PFonts, or use a font name
 *
 * WordCram has some convenient methods for some common
 * uses.
 */

new WordCram(this)
  .fromTextFile("../kari-the-elephant.txt")
  
  //-----------
  // Coloring words
  .withColor(#0000AA)
  //.withColors(#FF0000, #00CC00, #0000FF)
  //.withColor(color(0, 20, 150, 150)) // alpha works, too
  
  // But this won't work the way you expect it to:
  //.withColors(255, 0, 0) // Not red - invisible!
  // See the FAQ for the details, if you're curious:
  // http://goo.gl/dJrwB (WordCram FAQ at GoogleCode)
  
  //-----------
  // Words at Angles
  .angledAt(radians(30))
  //.angledAt(radians(30), radians(-60))
  
  // Two-thirds of the words will be at 30 degrees, the rest at -60.
  //.angledAt(radians(30), radians(30), radians(-60))
  
  //.angledBetween(-PI/8, PI/8)
  //.angledBetween(0, TWO_PI)
  
  //---------
  // Fonts
  .withFont("Georgia")
  //.withFont(georgia)
  //.withFont(georgiaItalic)
  //.withFont(minyaNouvelle)
  //.withFonts("Georgia", "Minya Nouvelle")
  //.withFonts(georgia, minyaNouvelle)
  
  //---------
  // Padding
  .withWordPadding(2)
  
  .drawAll();
