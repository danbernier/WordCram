---
layout: post
title: WordCram Release 0.5.0
date: 2011-05-12 02:10:16.000000000 -04:00
tags:
- release
status: publish
type: post
published: true
---

Today I learned that [WordCram 0.4.1 doesn't work with Processing 1.5](http://code.google.com/p/wordcram/issues/detail?id=11), and the root of the problem is the way it renders word shapes. Then I found that the same [change that enables PDF rendering](http://code.google.com/p/wordcram/source/diff?spec=svn404&r=404&format=side&path=/trunk/src/wordcram/WordCramEngine.java#sc_svn395_175) enables Processing 1.5 support.

So I could bug-fix the Processing 1.5 support, or throw in PDF rendering and call it a release.  Changing the way WordCram renders word shapes is pretty core, so I chose the release. And as a bonus, PDF support is official!

And that's how WordCram 0.5.0 found its way out into the world this afternoon. The big news is PDFs, but you can also control the padding between your words. [Download it now!](http://code.google.com/p/wordcram/downloads/list) You can thank me later. Send me a WordCrammed PDF.

Next up: replacing the text parsing code with [cue.language](https://github.com/vcl/cue.language).
