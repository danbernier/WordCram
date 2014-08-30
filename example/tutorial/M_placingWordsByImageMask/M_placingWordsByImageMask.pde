import wordcram.*;
import java.awt.*;

void setup() {
  PImage image = loadImage("../heart.png");
  size(image.width, image.height);
  
  background(128);
  
  Shape imageShape = new ImageShaper().shape(image, #000000);
  ShapeBasedPlacer placer = new ShapeBasedPlacer(imageShape);

  new WordCram(this).
    fromTextFile("../kari-the-elephant.txt").
    withPlacer(placer).
    withNudger(placer).
    sizedByWeight(7, 40).
    withColor(#ffffff).
    drawAll();
    
  
  imageShape = new ImageShaper().shape(image, #ffffff);
  placer = new ShapeBasedPlacer(imageShape);
  
  new WordCram(this).
    fromTextFile("../kari-the-elephant.txt").
    withPlacer(placer).
    withNudger(placer).
    sizedByWeight(7, 40).
    withColor(#000000).
    drawAll();
}

