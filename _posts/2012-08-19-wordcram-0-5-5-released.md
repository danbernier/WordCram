---
layout: post
title: WordCram 0.5.5 released
date: 2012-08-19 22:06:47.000000000 -04:00
tags:
- release
status: publish
type: post
published: true
---

This release is mostly a fix for [issue #4](https://github.com/danbernier/WordCram/issues/4). [dbasch](https://github.com/dbasch) couldn't make a word cloud, because [cue.language](https://github.com/jdf/cue.language) couldn't decide what language [his text](https://gist.github.com/3289656) was in. Next time cue gets confused, instead of stopping the whole show, WordCram will gracefully ignore it, and include the word.

I also made public some classes that weren't before: BBTree, BBTreeBuilder, and WordShaper. If you'd like to make Processing sketches that do overlap-checking like WordCram does, but your shapes aren't necessarily words, these might help you; you can see how they're used by looking at [the code in the WordCramEngine class](https://github.com/danbernier/WordCram/blob/master/src/wordcram/WordCramEngine.java). (Making them public also makes it easier for me to [experiment with changing WordCram's layout algorithms]({% post_url 2012-02-22-faster-wordcrams-an-experiment %}).)

As usual, you can [download the new version at github](https://github.com/danbernier/WordCram/downloads). Happy word cramming!
