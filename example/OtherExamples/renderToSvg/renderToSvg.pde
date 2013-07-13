import wordcram.*;

size(800, 500);

background(255);

try {
  new WordCram(this).
    fromWebPage("../../usconst.txt").
    toSvg("us-const.svg", width, height).
    withColors(#dd0000, #000000, #0000dd).
    drawAll();
}
catch (FileNotFoundException x) {
  println(x.getMessage());
}

println("done rendering the SVG");

PShape sh = loadShape("us-const.svg");
smooth();
shape(sh, 0, 0);

