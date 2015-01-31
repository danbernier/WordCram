---
layout: post
title: 2011 State of the Union Address, WordCrammed
date: 2011-01-27 01:56:19.000000000 -05:00
tags:
- examples
status: publish
type: post
published: true
---

<img title="2011 State of the Union Address" src="{{site.baseurl}}/assets/sotu2011/main.png" />

Twelve other runs:

<img title="Twelve wordcrams of the 2011 State of the Union Address" src="{{site.baseurl}}/assets/sotu2011/collage.png" />

The code for all of them:

{% highlight java linenos %}
import wordcram.*;
import wordcram.text.*;

void setup() {
  size(700, 500);
}

void draw() {
  background(255);
  new WordCram(this)
    .fromTextFile("state-of-the-union.txt")
    .withColors(
      color(0), color(230, 0, 0), color(0, 0, 230))
    .withPlacer(Placers.centerClump())
    .sizedByWeight(10, 100)
    .drawAll();

  saveFrame("sotu-##.png");
}
{% endhighlight %}
