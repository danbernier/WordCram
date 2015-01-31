---
layout: post
title: PDF Rendering Is Almost Ready
date: 2010-11-30 23:54:44.000000000 -05:00
tags:
- improvements
status: publish
type: post
published: true
---

After poking around, it seems like PDF rendering might not be as far-fetched as I'd first thought.  I've made some pretty good progress:

* WordCram can tell when the sketch is rendering to PDF.  This means the API is unchanged: you just set up your sketch to render to PDF.
* After cleaning up the innards a bit, rendering to PDF is pretty easy.



But I'm stuck at the last mile.  If you know how to use java's [PathIterator](http://download.oracle.com/javase/6/docs/api/java/awt/geom/PathIterator.html), [Path2D](http://download.oracle.com/javase/6/docs/api/java/awt/geom/Path2D.html), and/or [Shape](http://download.oracle.com/javase/6/docs/api/java/awt/Shape.html) classes with Processing, or know [Geomerative's](http://www.ricardmarxer.com/geomerative/) guts well (I think they have some similar problems), and don't mind answering a few questions, I'd love to hear from you: I'm danbernier at Google's famous e-mail domain.

Overall, I'm excited at the thought of real PDF rendering, and at how easy it's been.  If you're interested, check out the [svn branch](http://code.google.com/p/wordcram/source/browse/#svn/branches/DEV_PDF) and let me know what you think.
