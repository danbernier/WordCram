---
layout: post
title: WordCram 0.5.7 released
date: 2013-03-03 17:38:37.000000000 -05:00
tags:
- release
status: publish
type: post
published: true
---

WordCram 0.5.7 is out, and it's about making things just a little easier.

* You can make a WordCram from multiple text sources, by piling them on top of each other. Before, WordCram would forget about your old text source, and only remember the last one you told it about; now, it remembers, and uses, them all.
* If you're loading an HTML document, you can pass a CSS selector to narrow down the content. Handy for pages with navigation, headers, and footers.
* Words now have, by default, 1 pixel of space between them. It used to be zero, which made for some pretty dense images. If you want, you can always go back by calling `withWordPadding(0)`.

This is a quick release, because [simpsus](https://github.com/simpsus) is helping me smooth out making WordCrams from shapes, which will be the next release:

<img alt="north-america-wordcram" src="{{site.baseurl}}/assets/north-america-wordcram.png" />

In the meantime, download the [zip](https://wordcram.s3.amazonaws.com/downloads/wordcram.0.5.7.zip) or the [tar](https://wordcram.s3.amazonaws.com/downloads/wordcram.0.5.7.tar.gz), and have fun cramming!
