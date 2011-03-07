import wordcram.*;

/* If WordCram's helper methods, Colorers, Anglers,
 * and Fonters still don't do what you want, you can
 * write your own WordColorer, WordAngler, WordFonter,
 * or WordSizer.
 * To create one, use this pattern:
 * --TODO--
 */

void setup() {
  size(600, 350);
  background(255);
  
  new WordCram(this)
    .fromTextFile("../kari-the-elephant.txt")
    
    .withColorer(darkerByWeight())
    .withAngler(heavyIsHorizontal())
    
    // TODO need a fonter & sizer
    
    .drawAll();
}

WordColorer darkerByWeight() {
  return new WordColorer() {
    public color colorFor(Word word) {
      return color(255 * (1-word.weight), 0, 0);
    }
  };
}

WordAngler heavyIsHorizontal() {
  return new WordAngler() {
    public float angleFor(Word word) {
      return (1 - word.weight) * QUARTER_PI;
    }
  };
}
