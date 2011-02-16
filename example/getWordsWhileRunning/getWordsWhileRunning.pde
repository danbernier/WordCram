import wordcram.*;

WordCram wordCram;

void setup() {
  size(600, 400);
  background(0);

  wordCram = new WordCram(this)
    .fromTextFile("../usconst.txt")
      .sizedByWeight(8, 70);
}

void draw() {
  if (wordCram.hasMore()) {
    wordCram.drawNext();
    report();
  }
  else {
    noLoop();
  }
}

void report() {
  Word[] words = wordCram.getWords();
  int tooMany = 0;
  int tooSmall = 0;
  int couldNotPlace = 0;
  int placed = 0;
  int left = 0;

  for (int i = 0; i < words.length; i++) {
    Word word = words[i];
    if (word.wasSkipped()) {

      int skipReason = ((Integer)word.getProperty(WordCram.SKIPPED_BECAUSE)).intValue();

      switch(skipReason) {
      case WordCram.TOO_MANY_WORDS: 
        tooMany++; 
        break;
      case WordCram.TOO_SMALL: 
        tooSmall++; 
        break;
      case WordCram.NO_ROOM: 
        couldNotPlace++; 
        break;
      default: 
        System.out.println("Got a weird skip reason: " + skipReason + ", " + word);
      }
    }
    else if (word.wasPlaced()) {
      placed++;
    }
    else {
      left++;
    }
  }

  print("TooMany " + tooMany + "  ");
  print("TooSmall " + tooSmall + "  ");
  print("CouldNotPlace " + couldNotPlace + "  ");
  print("Placed " + placed + "  ");
  println("Left " + left);
}

