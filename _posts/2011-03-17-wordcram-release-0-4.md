---
layout: post
title: WordCram Release 0.4
date: 2011-03-17 22:17:16.000000000 -04:00
tags:
- release
status: publish
type: post
published: true
---

WordCram 0.4 is ready for consumption! Download the <a href="http://code.google.com/p/wordcram/downloads/detail?name=wordcram.0.4.zip" target="_blank">zip</a> or the <a href="http://code.google.com/p/wordcram/downloads/detail?name=wordcram.0.4.tar.gz" target="_blank">tar</a>.

The high notes:

* If you're passing your own words, you can pre-set their color, font, size, angle, and placement: `word.setColor(#f02939).setAngle(QUARTER_PI);` These settings will override the WordColorer, WordAngler, etc. This should make it easier to control how specific words are drawn, but you can even use it for styling all your words.
* If you're not seeing as many words as you expected, you can call `getSkippedWords()` to see which words were skipped, and call `wasSkippedBecause()` on each one to see why it was skipped. If you're troubleshooting the words that <em>do</em> show up, you can call `getRenderedSize()`, `getRenderedColor()`, etc, on each word. And if you want to do some interaction, `getWordAt(x,y)` will tell you which word is at the given [x,y] coordinates.
* All the settings for fine-tuning a WordCram are now at your fingers. If your WordCram is too slow, too crowded, or too sparse, you can probably fix it: you can limit how many words WordCram will try to render, limit the size of the smallest word it'll accept, and limit how many times it'll try to place a word.
* A bunch of new tutorial sketches will walk you through WordCram in (I hope) a sensible order. They cover all the features, both new and old.



See the <a href="http://code.google.com/p/wordcram/source/browse/branches/RB-0.4/RELEASENOTES.txt" target="_blank">Release notes</a>, the tutorial sketches, and the <a href="http://wordcram.googlecode.com/svn/javadoc/index.html" target="_blank">javadoc</a> for the details. If you have any questions or feedback, or just want to show off your word clouds, I'm <a href="http://twitter.com/wordcram">@wordcram</a> on twitter, and wordcram-at-gmail.
