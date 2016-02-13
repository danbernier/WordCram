package wordcram;

public class WordBag implements WordSource {

  int numWords;
  String[] wordStrings;
  double weightDistributionPower = 2;

  public WordBag(int numWords, String... wordStrings) {
    this.numWords = numWords;
    this.wordStrings = wordStrings;
  }

  public WordBag weightDistributionPower(float wdp) {
    this.weightDistributionPower = wdp;
    return this;
  }

  public Word[] getWords() {
    Word[] words = new Word[numWords];
    java.util.Random rand = new java.util.Random();
    
    for (int i = 0, wi = 0; i < words.length; i++, wi = (wi + 1) % wordStrings.length) {
      String word = wordStrings[wi];
      double weight = Math.pow(rand.nextDouble(), weightDistributionPower);
      words[i] = new Word(word, (float)weight);
    }
    return words;
  }
}
