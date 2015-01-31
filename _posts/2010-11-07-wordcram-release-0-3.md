---
layout: post
title: WordCram Release 0.3
date: 2010-11-07 21:55:21.000000000 -05:00
tags:
- release
status: publish
type: post
published: true
---

[WordCram 0.3 is here](http://code.google.com/p/wordcram/downloads/list), and it has some nice improvements.

## Simpler to Use

It's easier to create a WordCram: instead of that long list of parameters, you instantiate it with your sketch, and then call methods on your WordCram to customize it.

From this:
{% highlight java linenos %}
new WordCram(this,
  new TextSplitter(StopWords.ENGLISH + " shall")
    .split(loadStrings(myFile)),
  Fonters.pickFrom(createFont("serif", 1)),
  Sizers.byWeight(100, 1200),
  Colorers.twoHuesRandomSats(this),
  Anglers.mostlyHoriz(),
  Placers.swirl())
.drawAll();
{% endhighlight %}

To this:
{% highlight java linenos %}
new WordCram(this)
  .withStopWords(StopWords.ENGLISH + " shall")
  .fromTextFile(myFile)
  .withFont("serif")
  .sizedByWeight(100, 1200)
  .withPlacer(Placers.swirl())
  .drawAll();
{% endhighlight %}

With the new way, you don't have to remember which order the parts go in.  WordCram can provide defaults (notice the new version didn't pick a Colorer or Angler?), and good overloads (for example, instead of sizedByWeight, you can use sizedByRank).  The method names follow a convention, so they should be easy to remember.

Hopefully, this should make it easier to create WordCrams.  If you have tons of critical-path legacy enterprise application layers written against the old stuff, don't worry, you can still upgrade -- the old methods are there, just deprecated.

## New Text Sources

Another big plus is that you can make WordCrams from different text sources, like a web page:
{% highlight java linenos %}
new WordCram(this)
  .fromWebPage("http://wordcram.wordpress.com")
  .drawAll();
{% endhighlight %}

With the new stuff in place, it'll become easier to add more text sources: delicious tags, twitter streams, RSS, all that.

## Documentation

The last big news is documentation.  There's [actual javadoc](http://wordcram.googlecode.com/svn/javadoc/index.html), and examples included.  Once WordCram is installed into Processing, go to <i>File > Sketchbook > libraries > WordCram > examples</i> to see them.

## Miscellany, Et Cetera

Throw in some new Anglers, Colorers, and Placers, Java stop-words (so you can [WordCram java source code](http://wordcram.wordpress.com/2010/11/04/blog-header-by-wordcram/)), bug fixes, and performance tweaks, and you've got a release.  For all the messy details, see the [release notes](http://code.google.com/p/wordcram/source/browse/tags/REL_0.3/RELEASENOTES.txt?r=199).

## Coming Up

My next immediate goals are more tutorials, and a bunch of examples to show off the different ways you can control WordCram.  After that, we'll see -- there's still [lots to do](http://code.google.com/p/wordcram/wiki/ToDos).  If you have ideas for making WordCram better, I'd love to hear from you: wordcram at gmail dot com.

Go [upgrade already](http://code.google.com/p/wordcram/downloads/list)!
