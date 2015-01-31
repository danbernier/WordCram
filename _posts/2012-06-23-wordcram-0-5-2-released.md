---
layout: post
title: WordCram 0.5.2 released
date: 2012-06-23 02:57:16.000000000 -04:00
tags:
- examples
- release
status: publish
type: post
published: true
---

WordCram 0.5.2 makes it easier for your users to click on words. You can download the .zip or the .tar.gz at the new [github download page](https://github.com/danbernier/WordCram/downloads).

It's common to let people interact with a word cloud by clicking on the words. But WordCram has always been very particular about where you click: you have to click directly on the part of the word that's colored in. If you click in the middle of an O, or between the arms of an X, it won't register. This is pretty frustrating, because people aren't that precise, and no one expects it to work that way.

It does it this way because words can appear inside other words, or intertwined with them. So it can't just count it as a click, if you click inside the word's box. (That's <em>why</em> it was so fussy, at first - being fussy side-stepped this problem.)

WordCram 0.5.2 solves this twist by giving you the <em>smallest</em> word whose box contains your click, on the theory that, if you'd wanted that bigger word, you probably wouldn't have clicked so close to the smaller word.

There's an [example sketch on OpenProcessing](http://www.openprocessing.org/sketch/64559). Try it out, and see whether it doesn't feel natural. Or [download it](https://github.com/danbernier/WordCram/downloads), and try it out in your own sketch!
