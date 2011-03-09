import wordcram.*;

/* TODO not sure why you'd really want properties,
 * now that we have word pre-sets.
 * If you can't find a reason, kill this sketch,
 * and kill the methods (?).
 */


void setup() {
  size(600, 350);
  background(255);
  
  new WordCram(this)
    .fromTextFile("../kari-the-elephant.txt")
    
    .drawAll();
}
