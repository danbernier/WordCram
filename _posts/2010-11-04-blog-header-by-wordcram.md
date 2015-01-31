---
layout: post
title: Blog Header by WordCram
date: 2010-11-04 01:56:41.000000000 -04:00
tags:
- examples
status: publish
type: post
published: true
---

Here is, more-or-less, the sketch I used to generate the new WordCram blog header. About half of the sketch is Processing setup and interaction, so I could keep running it until I found one I liked.

When I get around to it, I want to make it easier to load a directory of source code files, but for now, `cat `find -name *.java` > src.txt` is good enough.

{% highlight java linenos %}
import wordcram.*;
import wordcram.text.*;

WordCram wc;

void setup() {
  colorMode(HSB);
  size(940, 198);
  background(0);

  initWordCram();
}

void initWordCram() {
  String license = join(loadStrings("LICENSE"), " ").toLowerCase();

  wc = new WordCram(this)
      .fromTextFile("src.txt")
      .withStopWords(StopWords.ENGLISH + StopWords.JAVA + license)
      .withFont("Droid Sans Mono")
      .sizedByWeight(12, 60)
      .withPlacer(new WordPlacer() {
          public PVector place(Word w, int wordCount, int numWords,
                               int wordWidth, int wordHeight,
                               int fieldWidth, int fieldHeight) {

            // x = weight, heavies on the left
            float x = (fieldWidth - wordWidth) * (1 - (float)w.weight);
            x *= random(0.6, 1); // fade them back a bit -- don't clump on the right

            // y = random around center horiz. line. Heavier words are less random.
            float y = random(1) *
                      (1 - (float)w.weight) *
                      (fieldHeight - wordHeight) / 2;
            y += (fieldHeight - wordHeight) / 2;

            return new PVector(x, y);
          }
      })
      .withAngler(new WordAngler() {
          public float angleFor(Word w) {

            // swing between -30 and 30 degrees -- heavy words swing less
            return (1 - (float)w.weight) * random(radians(-30), radians(30));
          }
      });
}

void draw() {
  if (wc.hasMore()) {
    wc.drawNext();
  }
  else {
    save("wordcram.png");
    println("done");
    noLoop();
  }
}

void mouseClicked() {
  background(0);
  initWordCram();
  loop();
}
{% endhighlight %}

***Update 2015-02-03:** I moved the WordCram blog to github pages, and changed the layout. Here's the original blog header I was talking about; it graced wordcram.org for a good 5 years.*

![]({{site.baseurl}}/assets/wordpress-blog-header.png)
