import wordcram.*;
import wordcram.text.*;

size(600, 350);
background(255);

/*
 * Frequent words are drawn bigger than infrequent ones,
 * but you can control exactly how that works.
 * 
 * Each word's frequency becomes its "weight": 
 * the most-frequent word has a weight of 1, and the 
 * less-frequent words are proportionally lower.
 * Each word also has a rank, which is its order
 * in the list, when sorted by weight.
 *
 *    -----------------------------------
 *    | Word     | Freq | Weight | Rank |
 *    -----------------------------------
 *    | Kari     | 1200 | 1.000  |  1   |
 *    | elephant |  600 | 0.500  |  2   |
 *    | jungle   |  400 | 0.333  |  3   |
 *    | ...      |  ..  |   ..   | ..   |
 *    -----------------------------------
 *
 * You can size words either by weight, or by rank,
 * and in both cases, you can specify the size of the
 * smallest and biggest words.
 *
 * Most texts have an uneven distribution of words - 
 * the most-frequent words are MUCH more frequent than 
 * the less-frequent words. The word weights will 
 * reflect this, but the word ranks will be much more
 * even. So if you size by weight, you'll have a few big
 * words, and many tiny ones; if you size by rank, you'll
 * have lots of bigger words, and smaller changes in size
 * between them - which will probably take longer to 
 * render.
 */

new WordCram(this)
  .fromTextFile("../kari-the-elephant.txt")
  
  // Very small: many words are so small, they're skipped.
  //.sizedByWeight(1, 30)
  
  // Much bigger: not all words will fit, and it'll take 
  // longer to place them. Be patient!
  //.sizedByWeight(15, 70)
  
  // Since there will be little change between words in 
  // size, many of them will be pretty large - so make the
  // range small enough to fit them.
  //.sizedByRank(1, 30)
  
  // Warning: this will just about take forever.
  //.sizedByRank(3, 50)
  
  // Hint: if your sketches take too long, try this:
  //.maxNumberOfWordsToDraw(30)
  
  .drawAll();
