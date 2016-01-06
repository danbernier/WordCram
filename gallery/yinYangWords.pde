import wordcram.*;
import java.awt.*;

void setup() {
  size(600, 600);
  background(255);
  
  PImage image = loadImage("yinyang.png");
  image.resize(width, height);
  
  Shape imageShape = new ImageShaper().shape(image, #000000);
  ShapeBasedPlacer placer = new ShapeBasedPlacer(imageShape);

  new WordCram(this).
    fromWords(repeatWord("flexible", 500)).
    withPlacer(placer).
    withNudger(placer).
    sizedByWeight(4, 40).
    angledAt(0).
    withColor(#F5B502).
    drawAll();
    
  
  imageShape = new ImageShaper().shape(image, #ffffff);
  placer = new ShapeBasedPlacer(imageShape);
  
  new WordCram(this).
    fromWords(repeatWord("usable", 500)).
    withPlacer(placer).
    withNudger(placer).
    sizedByWeight(4, 40).
    angledAt(0).
    withColor(#782CAF).
    drawAll();
}

Word[] repeatWord(String word, int times) {
  Word[] words = new Word[times];
  for (int i = 0; i < words.length; i++) {
    // Give the words a random weight, so they're sized differently.
    words[i] = new Word(word, random(1));
  }
  return words;
}