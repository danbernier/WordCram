import wordcram.*;

size(600, 350);
background(255);

/* How should the words be arranged on the screen?
 * Each word gets a "target" location. WordCram will 
 * place it there, and nudge it around until it 
 * doesn't overlap any other words, or until it 
 * gives up. High-weighted words are placed first.
 *
 * Placing words can be a bit tricky. If you try to
 * place the words too closely, WordCram will spend
 * most of its time trying to place words where
 * there's no room. Making the words smaller can help
 * there. Also, most placers take the screen width &
 * height into account, so those can affect the 
 * outcome.
 */

new WordCram(this)
  .fromTextFile("../kari-the-elephant.txt")
  
  //.withPlacer(Placers.centerClump())
  //.withPlacer(Placers.horizLine())
  //.withPlacer(Placers.horizBandAnchoredLeft())
  //.withPlacer(Placers.wave())
  
  // For this one, try setting the sketch size to 1000x1000.
  //.withPlacer(Placers.swirl())
  //.sizedByWeight(8, 30)
  
  //.withPlacer(Placers.upperLeft())
  //.sizedByWeight(10, 40)
  
  .drawAll();
