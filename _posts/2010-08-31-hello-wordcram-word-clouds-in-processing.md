---
layout: post
title: 'Hello, WordCram: Word Clouds in Processing'
date: 2010-08-31 14:06:38.000000000 -04:00
tags:
- examples
status: publish
type: post
published: true
---

Today I'm releasing WordCram, an open-source [Processing](http://processing.org) library that lets you generate [word clouds](http://images.google.com/images?q=word+cloud&biw=1366&bih=576).  It does the heavy  lifting -- text analysis, collision detection, bin-packing -- for you,  so you can focus on making your word clouds as beautiful, as revealing, or as silly as you like.

I've been working on WordCram for about a month, and I think it's ready  to be out of the lab. [Download the bits](http://code.google.com/p/wordcram/downloads/list), and try it out.

Here's the flavor of it:

{% highlight java linenos %}
import wordcram.*;
import wordcram.text.*;

void setup() {
  size(600, 400);
  background(0);

  WordCram wordCram = new WordCram(this,
    new TextSplitter().split(loadStrings("tao-te-ching.txt")),
    Fonters.FonterFor(createFont("sans", 1)),
    Sizers.byWeight(5, 60),
    Colorers.TwoHuesRandomSats(this),
    Anglers.MostlyHoriz,
    new CenterClumpWordPlacer(),
    new SpiralWordNudger());

  while (wordCram.hasMore()) {
    wordCram.drawNext();
  }
}
{% endhighlight %}

The result:

![A wordcram of the Tao Te Ching]({{site.baseurl}}/assets/tao-word-cloud.png)

You can see by the code -- Colorers, Anglers, Placers -- that it's pretty pluggable.  But there's still work to do -- better performance, a cleaner API, sensible defaults for some of those components -- so if you like what you see and want to help out, [tweet me](http://twitter.com/wordcram) or email me at wordcram-at-gmail.

In the meantime, I'll be working on smoothing out those components and putting up some tutorials here.

WordCram is inspired by [Wordle](http://wordle.net/), and built for [Processing](http://processing.org/), so a big thank-you to [Jonathan Feinberg](http://mrfeinberg.com/), [Ben Fry](http://benfry.com/), and [Casey Reas](http://reas.com/).
