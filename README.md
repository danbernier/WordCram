WordCram lets you generate [word
clouds](http://images.google.com/images?q=word+cloud) in Processing.
It does the heavy lifting -- text analysis, collision detection -- for
you, so you can focus on making your word clouds as beautiful, as
revealing, or as silly as you like.

http://wordcram.googlecode.com/svn/javadoc/wordcram.png ![Word Cloud
by
WordCram](https://github.com/danbernier/WordCram/raw/master/wordcram.png)

Watch WordCram in action, on OpenProcessing: [popular baby
names](http://openprocessing.org/visuals/?visualID=12562), and [the
U.S. Constitution](http://openprocessing.org/visuals/?visualID=12413).

## Make a Word Cloud

```
import wordcram.*;

// Set up the Processing sketch
size(1000, 600);
colorMode(HSB);
background(230);

// Make a wordcram from a random wikipedia page.
new WordCram(this)
  .fromWebPage("http://en.wikipedia.org/wiki/Special:Random")
  .withColors(color(30), color(110),
              color(random(255), 240, 200))
  .sizedByWeight(5, 120)
  .withFont("Copse")
  .drawAll();
```

You can control where words appear, what angle they're at, their font,
their color, and how they're sized.

## Install

[Installing](http://code.google.com/p/wordcram/wiki/Installing)
WordCram is simple, like any standard Processing library.

## Problems?

If you're running into problems with WordCram, see the
[FAQ](http://code.google.com/p/wordcram/wiki/FAQ), or read the
[javadocs](http://wordcram.googlecode.com/svn/javadoc/index.html).

## Contribute!

If you'd like to help out with WordCram, pick something from the [list
of ToDos](http://code.google.com/p/wordcram/wiki/ToDos), and then try
[building WordCram from
source](http://code.google.com/p/wordcram/wiki/BuildingWordCram) (it's
really easy).
