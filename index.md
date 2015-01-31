---
layout: default
---

WordCram lets you generate [word
clouds](http://images.google.com/images?q=word+cloud) in Processing.
It does the heavy lifting -- text analysis, collision detection -- for
you, so you can focus on making your word clouds as beautiful, as
revealing, or as silly as you like.

![Word Cloud by
WordCram](https://github.com/danbernier/WordCram/raw/master/wordcram.png)

## Make a Word Cloud

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

You can control where words appear, what angle they're at, their font,
their color, and how they're sized.

## Install

[Installing](https://github.com/danbernier/WordCram/wiki/Install)
WordCram is simple, like any standard Processing library.

## How do I use this thing? Show me examples!

You can check out the
[tutorials](http://wordcram.org/category/tutorial/) and
[examples](http://wordcram.org/category/examples/) at
http://wordcram.org. You can watch WordCram in action, on
OpenProcessing: [popular baby
names](http://openprocessing.org/visuals/?visualID=12562), and [the
U.S. Constitution](http://openprocessing.org/visuals/?visualID=12413).

But the best way to see WordCram in action is to install it, and look
at the examples under File > Examples > Contributed Libraries > WordCram.

## Problems?

If you're running into problems, see the
[FAQ](https://github.com/danbernier/WordCram/wiki/FAQ), or read the
[javadocs](http://danbernier.github.com/WordCram/javadoc/).

If a question has you stumped, and the
[FAQ](https://github.com/danbernier/WordCram/wiki/FAQ) is no help,
send me a note. My email account is 'wordcram', and I use gmail.

## Want a better WordCram?

WordCram is open-source under the Apache 2 license. That means you can
help make it better! I try to keep the source clean so it's easy to
find your way around. There's a [laundry list of things to
do](https://github.com/danbernier/WordCram/wiki/ToDos), and it's easy to
[build WordCram from
source](https://github.com/danbernier/WordCram/wiki/Build).
