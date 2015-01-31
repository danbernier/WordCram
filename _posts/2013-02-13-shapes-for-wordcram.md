---
layout: post
title: Shapes for WordCram
date: 2013-02-13 02:02:15.000000000 -05:00
tags:
- improvements
status: publish
type: post
published: true
---

This has been a feature request for a long time: making WordCrams in arbitrary shapes, like <a href="http://www.tagxedo.com/">Tagxedo</a> does.

It's currently slow, and a bit limited - you have to provide a `java.awt.Shape`, not (say) an image mask - but it's a first step. I hope to package it up nicely for the next WordCram release.

Below is the guts of the code, but here's the output:

<img alt="wordcram-love" src="{{site.baseurl}}/assets/wordcram-love.png" />

{% highlight java linenos %}
class ShapeBasedPlacer implements WordPlacer, WordNudger {

  Area area;
  float minX;
  float minY;
  float maxX;
  float maxY;

  public ShapeBasedPlacer(Shape shape) {
    this.area = new Area(shape);

    Rectangle2D areaBounds = area.getBounds2D();
    this.minX = (float)areaBounds.getMinX();
    this.minY = (float)areaBounds.getMinY();
    this.maxX = (float)areaBounds.getMaxX();
    this.maxY = (float)areaBounds.getMaxY();
  }

  public PVector place(Word w, int rank, int count, int ww, int wh, int fw, int fh) {

    w.setProperty("width", ww);
    w.setProperty("height", wh);

    for (int i = 0; i < 1000; i++) {
      float newX = randomBetween(minX, maxX);
      float newY = randomBetween(minY, maxY);
      if (area.contains(newX, newY, ww, wh)) {
        return new PVector(newX, newY);
      }
    }

    return new PVector(-1, -1);
  }

  public PVector nudgeFor(Word word, int attempt) {
    PVector target = word.getTargetPlace();
    float wx = target.x;
    float wy = target.y;
    float ww = (Integer)word.getProperty("width");
    float wh = (Integer)word.getProperty("height");

    for (int i = 0; i < 1000; i++) {
      float newX = randomBetween(minX, maxX);
      float newY = randomBetween(minY, maxY);

      if (area.contains(newX, newY, ww, wh)) {
        return new PVector(newX - wx, newY - wy);
      }
    }

    return new PVector(-1, -1);
  }

  float randomBetween(float a, float b) {
    return a + random(b - a);
  }
}

void setup() {
  Shape heart = makeHeart();
  ShapeBasedPlacer shapeBasedPlacer = new ShapeBasedPlacer(heart);

  new WordCram(this)
    .withPlacer(shapeBasedPlacer)
    .withNudger(shapeBasedPlacer)
    .drawAll();
}
{% endhighlight %}
