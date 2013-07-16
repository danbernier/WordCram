import wordcram.*;

void setup() {
  size(800, 500);
  smooth();

  String[] letters = split("Z Y X W V U T S R Q P O N M L K J I H G F E D C B A", ' ');
  Word[] words = new Word[letters.length];
  for (int i = 0; i < letters.length; i++) {
    float weight = map(i, 0, letters.length, 0, 1);
    words[i] = new Word(letters[i], i);
  }

  try {
    new WordCram(this).
      fromWords(words).
      toSvg("letters.svg", width, height).
      sizedByWeight(150, 10).
      withColors(#0033ff, #0055ff, #0088ff, #00bbff, #00ffdd).  // Colors come through, too.
      drawAll();
  }
  catch (java.io.FileNotFoundException x) {
    println(x.getMessage());
  }

  println("done rendering the SVG");

  println("loading the svg...");
  PShape sh = loadShape("letters.svg");

  background(255);
  shape(sh, 0, 0);
}
