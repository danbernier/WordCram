---
layout: post
title: WordCram Release 0.2
date: 2010-09-24 17:04:34.000000000 -04:00
tags:
- release
status: publish
type: post
published: true
---

The [0.2 Release](http://code.google.com/p/wordcram/downloads/list) has some nice changes over 0.1: it's easier to use, it's a bit faster, and the layouts should look better.

The details:

* Words should stop running off the edge of the screen.  We can make this better, and we will, and soon, but it's there now.
* WordCram will sort your Words, and scale their weights for you.  If you're  using the TextSplitter, this doesn't affect you.  But if you've been passing in your own array of Words, taking care to sort them, and making sure the biggest word had a weight of 1, you can take a break, because WordCram will take over from here.  Give your Words whatever weights you want, and it'll figure out how big they are, relative to each other.
* Two minor API changes:
  * `WordCram.draw()` is now `WordCram.drawAll()`, to make it clear that it'll draw ALL the words -- which could take some time.
  * The signature for `WordPlacer.place()` has changed.  Where it used to take `int imageSize` and `PGraphics host`, it now takes `int imageWidth, int imageHeight` and `int fieldWidth, int fieldHeight`.  Hopefully this makes it easier to write your own WordPlacers.
* Down in the guts, WordCram is rendering with `java.awt.Shapes`.  This matters because it makes it easier to keep words on-screen, easier to place them where you want, and eventually, easier and faster to re-color and re-layout a WordCram.

Add in a few performance boosts and bug fixes, and you've got a new release.  [Check it out](http://code.google.com/p/wordcram/downloads/list), play with it, and let me know what you think.  There's still [tons to do](http://code.google.com/p/wordcram/wiki/ToDos), if you'd like to help out.
