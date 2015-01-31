---
layout: post
title: Spring Cleaning for Smoother WordCram Builds
date: 2012-06-17 21:17:52.000000000 -04:00
tags:
- improvements
status: publish
type: post
published: true
---

I've been frustrated with WordCram's automated build for a long time. It was a giant tangle of [ant](http://ant.apache.org/), [JUnit](http://www.junit.org), [SVN](http://subversion.apache.org/), [javadoc](http://en.wikipedia.org/wiki/Javadoc), and [googlecode file-uploads](http://code.google.com/p/ant-googlecode/), and it was always a struggle to change it, or even remember what it did for me.

[Moving to github]({% post_url 2011-11-06-wordcrams-moving-to-github %}) aggravated this, because it meant the automated build had to change. Release downloads go to a different place. I'd like to use Git's more flexible branching. And where should I host the javadocs?

So this weekend, I re-wrote the build in rake, which has already paid off: WordCram's [downloads](https://github.com/danbernier/WordCram/downloads) & [javadoc](http://danbernier.github.com/WordCram/javadoc/) are now officially hosted on github, and I've started using a more effective branching strategy (based on [git flow](http://nvie.com/posts/a-successful-git-branching-model/)). And new ideas, like generating simpler HTML documentation, or automatically [tweeting](http://twitter.com/wordcram) or blog-posting releases, will be easy to implement.

But the biggest benefit is that it'll make WordCram <em>so</em> much easier to work on. Small bug fixes can be quickly released, and I can make progress on bigger features and improvements on a separate branch, easily merging between them. I'm looking forward to getting back into it!
