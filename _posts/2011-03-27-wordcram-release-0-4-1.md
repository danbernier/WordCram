---
layout: post
title: WordCram Release 0.4.1
date: 2011-03-27 14:46:11.000000000 -04:00
tags:
- release
status: publish
type: post
published: true
---

WordCram 0.4.1 is a minor bug-fix release, available in [zip](http://code.google.com/p/wordcram/downloads/detail?name=wordcram.0.4.1.zip) and [tar](http://code.google.com/p/wordcram/downloads/detail?name=wordcram.0.4.1.tar.gz).

Danilo Di Cuia [wondered whether French accents worked in WordCram](http://twitter.com/#!/tezzutezzu/status/50176425898156033), which surprised me, because I've <em>seen</em> characters like that in WordCrams before. But as it turns out, it only worked if you pass your own array of Words: the word-counting part was removing all accented characters from the source text.

The bug's fixed now, but for the 0.5 release, I'll probably replace that whole part with [cue.language](https://github.com/vcl/cue.language) instead. It's a small library for natural-language tasks, written by Wordle creator Jonathan Feinberg, and it grew out of Wordle. It's better-tested, and it handles a bunch of languages already. That part of WordCram was always pretty immature, so it'll be nice to rely on something more battle-hardened.

&nbsp;
