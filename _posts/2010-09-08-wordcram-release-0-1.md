---
layout: post
title: WordCram Release 0.1
date: 2010-09-08 20:03:19.000000000 -04:00
tags:
- release
status: publish
type: post
published: true
---

The [WordCram 0.1 release](http://code.google.com/p/wordcram/downloads/list) is here.  Don't let the low release number fool you -- it's pretty solid code.  Since the initial release, I've improved performance, and cleaned up the API, making it faster, and easier to use.

Here's the flavor of the API change...from this:

{% highlight java linenos %}
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
{% endhighlight %}

to this:

{% highlight java linenos %}
WordCram wordCram = new WordCram(this,
    new TextSplitter().split(loadStrings("tao-te-ching.txt")),
    Fonters.alwaysUse(createFont("sans", 1)),
    Sizers.byWeight(5, 60),
    Colorers.twoHuesRandomSats(this),
    Anglers.mostlyHoriz(),
    Placers.centerClump());

wordCram.draw();
{% endhighlight %}

A very few further API tweaks are planned, but I wanted to get a first release out for you to play with, and to write some tutorials against.

[Download the bits](http://code.google.com/p/wordcram/downloads/list), give it a test-drive, and let me know what you think.
