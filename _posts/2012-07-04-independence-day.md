---
layout: post
title: Independence Day
date: 2012-07-04 14:08:48.000000000 -04:00
tags:
- examples
status: publish
type: post
published: true
---

Happy Fourth of July!

In the spirit of independence, here's a WordCram made from the [U.S. Declaration of Independence](http://www.archives.gov/exhibits/charters/declaration_transcript.html), with open-source software (Processing and WordCram), and open-source fonts ([AveriaSerif-Regular](http://iotic.com/averia/) and [Jane Austen](http://www.dafont.com/jane-austen.font)):

<img alt="July 4" src="{{site.baseurl}}/assets/july4.png" />

You can see it at [OpenProcessing](http://www.openprocessing.org/sketch/65035); here's the source, in its entirety:

{% highlight java linenos %}
import wordcram.*;

size(800, 500);

background(#F5F1E3);

PFont averia = createFont("AveriaSerif-Regular.ttf", 1);
PFont janeAusten = createFont("JaneAust.ttf", 1);

new WordCram(this)
  .fromTextFile("declaration.txt")
  .withFonts(averia, janeAusten)
  .withColors(#B20E0E, #0E12B2)
  .angledAt(0)
  .withWordPadding(1)
  .drawAll();
{% endhighlight %}
