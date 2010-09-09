import wordcram.*;

size(700, 400);
background(0);
colorMode(HSB);

WordCram wordcram = new WordCram(this,
  new Word[] {
    new Word("Hello", 1.0),
    new Word("WordCram", 0.6)
  },
  Fonters.alwaysUse(createFont("sans", 1)),
  Sizers.byWeight(4, 60),
  Colorers.twoHuesRandomSats(this),
  Anglers.horiz(),
  Placers.centerClump()
);
 
wordcram.draw();
