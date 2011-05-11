import wordcram.*;

/* If WordCram's helper methods, Colorers, Anglers,
 * and Fonters still don't do what you want, you can
 * write your own WordColorer, WordAngler, WordFonter,
 * or WordSizer.
 * To create one, use this pattern:
 *
 * new Word{Thing}er {
 *     public {type} {thing}For(Word word {& any other parameters}) {
 *         // your logic here
 *         return {some calculated value};
 *     }
 * }
 *
 * For example:
 * new WordColorer {
 *     public color colorFor(Word word) {
 *         return #FF0000;
 *         // (but make yours interesting)
 *     }
 * }
 * 
 * You can see what the method should be called, what 
 * parameters it should take, and what type it should 
 * return, in the javadoc pages.
 */

void setup() {
  size(600, 350);
  background(255);
  
  new WordCram(this)
    .fromTextFile("../kari-the-elephant.txt")
    
    //.withColorer(darkerByWeight())
    //.withAngler(heavyIsHorizontal())
    //.withFonter(shortWordsInLucidaAndLongWordsInGeorgia())
    //.withSizer(onlyTShirtSizes())
    
    .drawAll();
}

WordColorer darkerByWeight() {
  return new WordColorer() {
    public color colorFor(Word word) {
      return color(255 * (1-word.weight), 0, 0);
    }
  }; // Don't forget the semi-colon for the return statement.
}

WordAngler heavyIsHorizontal() {
  return new WordAngler() {
    public float angleFor(Word word) {
      return (1 - word.weight) * QUARTER_PI;
    }
  };
}

WordFonter shortWordsInLucidaAndLongWordsInGeorgia() {
  final PFont lucida = createFont("Lucida Sans", 1);
  final PFont georgia = createFont("Georgia Italic", 1);
  
  return new WordFonter() {
    public PFont fontFor(Word word) {
      if (word.word.length() <= 5) {
        return lucida;
      }
      else {
        return georgia;
      }
    }
  };
}

WordSizer onlyTShirtSizes() {
  return new WordSizer() {
    public float sizeFor(Word word, int rank, int wordCount) {
      if (word.weight > 0.75) {
        return 40;
      }
      else if (word.weight > 0.5) {
        return 20;
      }
      else if (word.weight > 0.075) {
        return 10;
      }
      else {
        return 0; // weed these out (just to speed things up a bit)
      }
    }
  };
}
