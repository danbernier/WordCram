import wordcram.*;

void setup() {
  size(1000, 500);
  smooth();
  background(255);

  new WordCram(this)
    .fromWebPage("http://wordcram.org")
      .drawAll();
}

void wordsCounted(Word[] words) {
  println("counted " + words.length + " words!");
}

void beginDraw() {
  println("beginDraw: drawing the sketch...");
}

int wordsDrawn = 0;
void wordDrawn(Word word) {
  //println("drew this word! " + word.word);
  wordsDrawn++;
}

int wordsSkipped = 0;
void wordSkipped(Word word) {
  //println("skipped " + word.word + " because " + word.wasSkippedBecause());
  wordsSkipped++;
}

void endDraw() {
  println("endDraw!");
  println("- skipped: " + wordsSkipped);
  println("- drawn:   " + wordsDrawn);
}

