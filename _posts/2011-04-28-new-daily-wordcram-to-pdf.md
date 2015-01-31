---
layout: post
title: 'New daily: WordCram to PDF'
date: 2011-04-28 12:10:19.000000000 -04:00
tags:
- examples
- improvements
status: publish
type: post
published: true
---

WordCram now outputs to PDF. It'll be in the 0.5 release, but you can grab the new daily build right now ([zip](http://code.google.com/p/wordcram/downloads/detail?name=wordcram.20110427.zip&can=2&q=) or [tar](http://code.google.com/p/wordcram/downloads/detail?name=wordcram.20110427.tar.gz&can=2&q=)) and try it out. The only new code you'll need is normal Processing PDF stuff, all the WordCram code stays the same. Just `import processing.pdf.*`, and use <code>size(<i>width</i>, <i>height</i>, PDF, "path/to/output.pdf")</code>.

Here's an example, a [PDF word cloud](http://wordcram.files.wordpress.com/2011/04/wordcram.pdf) from the [Wikipedia page on PDF](http://en.wikipedia.org/wiki/Portable_Document_Format). Here's the code to produce it:

{% highlight java linenos %}
import processing.pdf.*;
import wordcram.*;

void setup() {
  size(700, 700, PDF, "wordcram.pdf");

  background(255);

  new WordCram(this)
    .fromWebPage("http://en.wikipedia.org/wiki/Portable_Document_Format")
    .withColors(#000000, #777777, #ff0000)
    .withFonts("LiberationSans")
    .sizedByWeight(1, 200)
    .withWordPadding(1)
    .angledAt(0)
    .withPlacer(new WordPlacer() {
      public PVector place(Word w, int r, int c, int ww, int wh, int fw, int fh) {
        float xScatter = (1-w.weight);
        float x = map(random(-xScatter, xScatter), -1, 1, 30, fw-ww-30);
        float y = (1-pow(w.weight, 0.25)) * (fh - wh) + 30;
        return new PVector(x, y);
      }
    })
    .drawAll();
}
{% endhighlight %}

I'm pretty excited about this. PDF output was one of the first features asked for, and it's been a goal of mine to finish it. But more importantly, we can now print some really nice, high-res word clouds!
