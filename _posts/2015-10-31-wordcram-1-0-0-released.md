---
layout: post
title: WordCram 1.0.0 Released, for Processing 3.0
date: 2015-10-31 16:00:00.000000000 -04:00
tags:
- release
status: publish
type: post
published: true
---

I just released [WordCram 1.0.0](//wordcram.s3.amazonaws.com/downloads/wordcram.1.0.0.zip)!

The big news here is that WordCram now runs on Processing 3.0. As any Processing core- or library-maintainer can tell you, there are [lots](https://github.com/processing/processing/wiki/Changes-in-3.0) of [changes](https://github.com/processing/processing/tree/master/core). WordCram depends pretty heavily on `java.awt` classes, which means it can't run in the browser, or on Android - and Processing 3 is making it clear that `java.awt` classes should go. I was afraid I'd have to re-write WordCram from scratch, but it looks like `java.awt` is still there - so we're safe for now.

We'll see what the future brings, but for now, you can keep on cramming, on the latest Processing. If you haven't seen the changes, [let Dan Shiffman show you around](https://vimeo.com/140600280) - it's pretty nice.
