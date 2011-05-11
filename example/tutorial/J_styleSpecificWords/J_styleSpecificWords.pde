import wordcram.*;

size(600, 350);
background(255);

/* If you're passing your own weighted words to
 * WordCram, you can control how specific words
 * are styled. The regular methods will still work,
 * but if a Word has style pre-sets, they'll
 * override whatever style rules are in place.
 * 
 * In the example below, the letters are colored
 * black, in the default font, up-side down, and 
 * placed on a wave - except for A and Z.
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

Word a = alphabet[0];
a.setColor(#FF0000);
a.setAngle(radians(20));
a.setFont(createFont("../MINYN___.TTF", 1));
a.setPlace(340, 200);
a.setSize(160);

// You can chain your calls, like you do with WordCram.
Word z = alphabet[25];
z.setColor(#0000FF)
   .setAngle(radians(-20))
   .setFont(createFont("Georgia", 1))
   .setPlace(180, 40);

z.setSize(130);

new WordCram(this)
  .fromWords(alphabet)
  .angledAt(-PI)
  .withPlacer(Placers.wave())
  .drawAll();
