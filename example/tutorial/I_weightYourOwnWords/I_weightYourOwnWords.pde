import wordcram.*;

size(600, 350);
background(255);

/* You can do more than count up words in a 
 * document - you can pass your own weighted
 * words to WordCram, in an array. They can be
 * anything: professional athletes weighted by
 * points scored or minutes of playtime,
 * politicians weighted by how often they vote
 * against party lines, or nations weighted by
 * population, area, or GDP.
 */

Word[] alphabet = new Word[] {
  new Word("A", 26), new Word("B", 25), new Word("C", 24), 
  new Word("D", 23), new Word("E", 22), new Word("F", 21),
  new Word("G", 20), new Word("H", 19), new Word("I", 18),
  new Word("J", 17), new Word("K", 16), new Word("L", 15),
  new Word("M", 14), new Word("N", 13), new Word("O", 12),
  new Word("P", 11), new Word("Q", 10), new Word("R", 9),
  new Word("S", 8),  new Word("T", 7),  new Word("U", 6),
  new Word("V", 5),  new Word("W", 4),  new Word("X", 3),
  new Word("Y", 2),  new Word("Z", 1)
};

new WordCram(this)
  .fromWords(alphabet)
  .drawAll();
