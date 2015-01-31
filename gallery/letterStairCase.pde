import wordcram.*;

void setup() {
  size(400, 250);
}

void draw() {
  background(255);
  fill(0);

  new WordCram(this).
    fromWords(loadLetters()).
    angledAt(0).
    minShapeSize(0).  // Make sure "I" always shows up.
  drawAll();
}

Word[] loadLetters() {
  Word[] letters = new Word[26];
  for (int i = 0; i < 26; i++) {
    Word letter = new Word(str(char(65+i)), 1);
    float x = map(i, 0, 29, 0, width);
    float y = map(i, 0, 29, 0, height);
    letter.setPlace(new PVector(x, y));
    letter.setSize(35);
    letters[i] = letter;
  }
  return letters;
}

void endDraw() {
  save(new File(sketchPath("")).getName() + ".png");
  exit();
}
