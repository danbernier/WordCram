---
layout: post
title: Get Acquainted With WordCram
date: 2010-09-09 16:42:11.000000000 -04:00
tags:
- tutorial
status: publish
type: post
published: true
---

<em>(2010-09-27: Updated for WordCram 0.2</em>.)

WordCram is customizable, but you have to know how it works, what each piece does, to get what you're after.

The whole idea of a word cloud is to draw a bunch of words according to their weight.  With WordCram, you hand it the weighted words and your Processing sketch, and it'll generate the word cloud image for you.

WordCram's `drawAll()` method draws all the words at once.  (This can take a while if you feed it a lot of words.)  Or if the WordCram `hasMore()` words to draw, you can use `drawNext()` to draw them one-by-one.  Try calling `drawNext()` once per frame, and you can watch WordCram position each word.

{% highlight java linenos %}
// Draw them all at once (might take a while):
wordcram.drawAll();

// Or, draw them individually:
if (wordcram.hasMore()) {
   wordcram.drawNext();
}
{% endhighlight %}

## Weighting Words

Word clouds often illustrate word frequency in some piece of text: more frequent words are bigger, giving you a rough idea what the text is about.  WordCram comes with a TextSplitter that counts up the words in any String or String array, which works well with Processing's `loadStrings(filename)` method:

{% highlight java linenos %}
Word[] weightedWords = new TextSplitter().split(loadStrings("lotsOfText.txt"));
{% endhighlight %}

But WordCram can accept any array of `Word` objects, so you can make your own array with whatever values you like:

{% highlight java linenos %}
Word[] populationSizes = new Word[] {
    new Word("China", 1339360000),
    new Word("India", 1187340000),
    new Word("USA", 310192000),
    ...
};
{% endhighlight %}

Future plans include loading weighted Words from your delicious tags, a web page, a Twitter stream, and an RSS feed.

## Getting the Look You Want

When creating a word cloud, it'd be nice if you could pick each word's font and color, and say about where in the picture it should go, and what angle it should be at.  WordCram lets you do that.  You give it a WordColorer, and when WordCram is ready to draw "careening" (weight 0.4317), it'll ask the WordColorer what color to draw it in.  You can get WordColorers from the Colorers class:

{% highlight java linenos %}
WordCram wordcram = new WordCram(...
    // garish Christmas colors
    Colorers.pickFrom(color(255,0,0), color(0,255,0)),
    ...);

WordCram otherWordcram = new WordCram(...
    // three grays
    Colorers.pickFrom(color(50), color(125), color(200)),
    ...);
{% endhighlight %}

Besides the WordColorer, a WordCram needs a WordFonter, a WordPlacer, a WordAngler, and a WordSizer.  Each one has a corresponding class with some pre-fabs, to get you started:
<table>
<tbody>
<tr>
<th>pre-fab class</th>
<th>methods</th>
</tr>
<tr>
<td>Colorers</td>
<td>pickFrom(), twoHuesRandomSats()</td>
</tr>
<tr>
<td>Fonters</td>
<td>pickFrom(), alwaysUse()</td>
</tr>
<tr>
<td>Placers</td>
<td>horizLine(), centerClump(), upperLeft()</td>
</tr>
<tr>
<td>Anglers</td>
<td>pickFrom(), alwaysUse(), random(), horiz(), mostlyHoriz(), upAndDown(), hexes()</td>
</tr>
<tr>
<td>Sizers</td>
<td>byWeight(), byRank()</td>
</tr>
</tbody>
</table>

### Colorers and Fonters

As we've seen, WordColorers tell WordCram how to color each word.  `Colorers.pickFrom()` can take any number of Processing colors, and will return one at random.  `Colorers.twoHuesRandomSats()` picks two random hues, and randomly saturates one of them for each word.  You have to pass it the Processing sketch, so it can use the sketch's `color()` method: `Colorers.twoHuesRandomSats(this)`.

WordFonters tell WordCram which PFont to use for each word.  `Fonters.pickFrom()` words like Colorers' version -- it can take any number of PFonts.  `Fonters.alwaysUse()` will always use the PFont you give it.

### Placers

Placers are more complicated, but only a little bit.  They give WordCram the x and y coordinates where each word should appear, as a PVector.  (The word won't appear <em>exactly</em> there, it'll be nudged a bit until it doesn't overlap with other words.)  `Placers.horizLine()` tries to place words along the horizontal axis, `Placers.centerClump()` places them in more of a circle, and `Placers.upperLeft()` puts them all in the corner.

Placers are a weak spot for WordCram right now -- I'm not entirely happy with most of them, and I expect the guts of WordCram that use Placers to change in the future.  That said, the existing Placers, and how you use them, will probably stay the same.  (Hopefully they'll just work better.)

### Anglers

Anglers tell WordCram what angle each word should be drawn at, specified in radians.  Right now there are more pre-fab Anglers than anything else, but they're all pretty simple.

`pickFrom()` and `alwaysUse()` work just like they do for Fonters.  `random()` will return a random angle for each word.  `horiz()` makes them all horizontal, but `mostlyHoriz()` draws a few going up and down, and `upAndDown()` draws them <em>all</em> up and down.  `hexes()` puts them all on increments of 60°, so it looks something like a snowflake or a hex grid.

### Sizers

Sizers tell WordCram what size to draw each word in.  Their answer is passed to Processing's textFont method, along with the PFont returned by the Fonter.  The two built-in Sizers, byWeight and byRank, both take a minimum and maximum size.  (A word's weight is its frequency; when you sort words by their weight, a word's rank is its order in the list.)

Eventually, WordCram will have a "smart sizer" that decides how to size words based on the size of your sketch, the number of words, and the distribution of their weights, but you'll always be able to use a different one if you need to.

### Rolling Your Own

There's nothing special about those built-in components -- if you don't like any of them, you can roll your own.  For instance, here's a WordSizer that makes "careening" huge, and everything else tiny.

{% highlight java linenos %}
class CareeningBigWordSizer implements WordSizer {
   public int sizeFor(Word w, int wordRank, int wordCount) {
      if (w.word.equals("careening")) {
         return 300;
      } else {
         return 3;
      }
   }
}
{% endhighlight %}

(If that looks weird, don't sweat it -- you can always use the built-ins.  But if you're curious, and you really want to use WordCram to the fullest, reading about Java's interfaces or the Strategy pattern should get you started.)

Each one is an interface with only one method:
<table>
<tbody>
<tr>
<th>interface</th>
<th>its only method</th>
</tr>
<tr>
<td>WordColorer</td>
<td>int colorFor(Word w)</td>
</tr>
<tr>
<td>WordFonter</td>
<td>PFont fontFor(Word w)</td>
</tr>
<tr>
<td>WordPlacer</td>
<td>PVector place(Word w, int wordIndex, int wordsCount, int wordImageWidth, int wordImageHeight, int fieldWidth, int fieldHeight)</td>
</tr>
<tr>
<td>WordAngler</td>
<td>float angleFor(Word w)</td>
</tr>
<tr>
<td>WordSizer</td>
<td>float sizeFor(Word w, int wordRank, int wordCount)</td>
</tr>
</tbody>
</table>

## That's It!

There's not much more to WordCram. I might do a tutorial about WordCram's innards (bounding box trees, collision detection, and nudging words), but you don't need to know about all that unless you're in the innards, or rolling your own WordPlacer, and that's another tutorial.

If anything doesn't make sense, let me know, and I'll do my best to clear it up.
