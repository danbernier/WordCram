---
layout: post
title: WordCram 0.6.0 released
date: 2013-08-24 16:23:44.000000000 -04:00
tags:
- examples
- release
status: publish
type: post
published: true
---

WordCram 0.6.0 is here, and brings with it arbitrary shape-based layouts, SVG export, event callbacks so you can monitor your sketch's progress, and as usual, a few bug fixes. Special thanks to [simpsus](https://github.com/simpsus) for helping out so much!

# Event callbacks

Ever wonder what WordCram's doing in there? Ever have a WordCram just hang, making your fan whir, and you have no idea why it's taking so long? Callbacks can help you figure it out.

Add methods like this to your Processing sketch, the way you do for mouse or keyboard events:

{% highlight java linenos %}
void wordSkipped(Word word) {
  println("Skipped: " + word);
}
void wordDrawn(Word word) {
  println("Just drew: " + word);
}
{% endhighlight %}

As your sketch runs, you'll see the print-outs. You can run any kind of code in there, too.

You can see the full list of callbacks in [the javadoc for the Observer class](http://danbernier.github.io/WordCram/javadoc/wordcram/Observer.html).

# SVG Export

WordCram has been able to [export to PDF]({% post_url 2011-04-28-new-daily-wordcram-to-pdf %}) since the 0.5.0 release, but for a real scalable image, SVG is much better.

Now, it's dirt-simple to export to SVG. Just add this to the WordCram setup:

{% highlight java linenos %}
toSvg("my-wordcram.svg", width, height)
{% endhighlight %}

...as in:

{% highlight java linenos %}
import wordcram.*;

size(800, 600);

try {
  new WordCram(this)
    .toSvg("nytimes.svg", width, height)
    .fromWebPage("http://nytimes.com")
    .drawAll();
}
catch (java.io.FileNotFoundException x) {
  println(x);
}
{% endhighlight %}

(Unfortunately, you have to wrap it in the try-catch. I'll work on smoothing that out for 0.6.1.)

Here's what you get:

<img src="{{site.baseurl}}/assets/nytimes.svg" alt="SVG WordCram from http://nytimes.com" width="500" />

# Shapes

This is the big one. This is the last major feature WordCram lacked, one that I really wanted to add.

Start with an image like this:

<img src="{{site.baseurl}}/assets/batman.gif" alt="batman" />

Load the image, extract any color from it into a Shape (here, I'm using black), and make a ShapeBasedPlacer for it:

{% highlight java linenos %}
PImage image = loadImage("batman.gif");
Shape imageShape = new ImageShaper().shape(image, #000000);
ShapeBasedPlacer placer = new ShapeBasedPlacer(imageShape);
{% endhighlight %}

Use that ShapeBasedPlacer as both the WordPlacer and the WordNudger in your WordCram:

{% highlight java linenos %}
background(#110000);
new WordCram(this).
  fromWebPage("http://en.wikipedia.org/wiki/Batman").
  withPlacer(placer).
  withNudger(placer).
  sizedByWeight(12, 120).
  withFont("Futura-CondensedExtraBold").
  withColor(#DBC900).
  drawAll();
{% endhighlight %}

And voilà:

<img src="{{site.baseurl}}/assets/batman.png" alt="batman" />

It can be slow sometimes, though the event callbacks can help you see where it's spending its time, and tune accordingly. And the API isn't totally there yet - I'll work on smoothing out both for the future. But the capability is there, so give it a try.

# Download it!

[Download WordCram 0.6.0](https://wordcram.s3.amazonaws.com/downloads/wordcram.0.6.0.zip), and get cramming.

If you tweet, and you make some WordCrams and tweet them, let [@wordcram](http://twitter.com/wordcram) know!
