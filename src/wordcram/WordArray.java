package wordcram;

/*
 * This is just here so WordCram.fromWords(Word[]) has something to use.
 */

class WordArray implements WordSource {
  Word[] words;

  WordArray(Word[] words) {
    this.words = words;
  }

  public Word[] getWords() {
    return words;
  }
}
