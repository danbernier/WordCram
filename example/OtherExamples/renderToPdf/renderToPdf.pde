import processing.pdf.*;
import wordcram.*;

/*
 * After you run this sketch,
 * look in the sketch folder
 * to find the PDF.
 */

void setup() {
  size(700, 700, PDF, "usconst.pdf");
  background(255);
  
  new WordCram(this)
    .fromTextFile("../usconst.txt")
    .withColors(#000000, #0000dd, #ff0000)
    .withFonts("LiberationSans")
    .sizedByWeight(4, 140)
    .minShapeSize(1)
    .withWordPadding(1)
    .drawAll();
  
  exit();
}
