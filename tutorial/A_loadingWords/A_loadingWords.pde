import wordcram.*;

size(600, 350);
background(255);

/*
 * There are a few different ways to load text.
 * WordCram will parse out the words, count them 
 * up, and render more-frequent words bigger than
 * less-frequent ones.
 */

new WordCram(this)
  
  //.fromWebPage("http://en.wikipedia.org/wiki/Word_cloud")
  //.fromHtmlFile("../kari-the-elephant.html")
  .fromTextFile("../kari-the-elephant.txt")
  //.fromTextString(loadStrings("../kari-the-elephant.txt"))
  //.fromHtmlString(loadStrings("../kari-the-elephant.html"))
  
  .withColor(#000000)
  .drawAll();
