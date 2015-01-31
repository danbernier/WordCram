---
layout: post
title: Faster WordCrams? An Experiment
date: 2012-02-22 04:10:27.000000000 -05:00
tags:
- improvements
status: publish
type: post
published: true
---

[Simulated annealing](http://en.wikipedia.org/wiki/Simulated_annealing) is a programming trick for quickly packing a bunch of things together. It's based on the idea of [annealing](http://en.wikipedia.org/wiki/Annealing_(metallurgy)), where (as I understand it) a metal is heated, which gives the molecules enough energy to wiggle, then cooled slowly, which lets the molecules settle into a denser arrangement.

I've been wondering for a while whether this could make WordCram faster. It seems well-suited to the problem - the heart of WordCram is packing non-overlapping shapes together.

So I made [this Processing experiment sketch](http://openprocessing.org/visuals/?visualID=53214), which uses simulated annealing to arrange circles.

<a href="http://openprocessing.org/visuals/?visualID=53214"><img src="{{site.baseurl}}/assets/simulated-annealing-circles.png" title="Arranging circles by simulated annealing" /></a>

I think it works pretty well - it can arrange 200 circles of different sizes along a line in under a second. (My [first experiment](http://openprocessing.org/visuals/?visualID=53004) was a lot slower, which gives you an idea how powerful this trick can be - once you get it right.)
