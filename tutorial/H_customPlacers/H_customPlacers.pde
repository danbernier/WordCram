import wordcram.*;

/* You can place words according to your own rules, too.
 * It's a bit more complicated, and there are a bunch of
 * extra parameters to your method, but that means you
 * have more control over how words are placed.
 */


void setup() {
  size(600, 350);
  background(255);
  
  new WordCram(this)
    .fromTextFile("../kari-the-elephant.txt")
    
    //.withPlacer(onDiagonal())
    //.withPlacer(alphabeticallyByWeight())
    
    .drawAll();
}

WordPlacer onDiagonal() {
  return new WordPlacer() {
    public PVector place(Word word, int rank, int wordCount,
                         int wordWidth, int wordHeight, 
                         int fieldWidth, int fieldHeight) {
      
      float x = word.weight * (fieldWidth-wordWidth);
      float y = word.weight * (fieldHeight-wordHeight);
      return new PVector(x, y);
    }
  };
}

WordPlacer alphabeticallyByWeight() {
  return new WordPlacer() {
    public PVector place(Word word, int rank, int wordCount,
                         int wordWidth, int wordHeight, 
                         int fieldWidth, int fieldHeight) {
      
      char firstLetter = word.word.toUpperCase().charAt(0); // A = 65, Z = 90
      float x = map(firstLetter, 65, 90, 0, fieldWidth - wordWidth);
      float y = (fieldHeight - wordHeight) * (1 - word.weight);
      return new PVector(x, y);
    }
  };
}
