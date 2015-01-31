---
layout: post
title: Why Don't All My Words Show Up?
date: 2013-03-02 20:51:30.000000000 -05:00
tags:
- tutorial
status: publish
type: post
published: true
---

You installed WordCram, gave it a list of words, and ran it - and some of your words are missing. What's up?

WordCram will try really hard to place all your words, but if it has a hard time placing one, it'll eventually move on to the next word. (If it didn't, you might still be waiting now for a WordCram you started to render a year-and-a-half ago.) Normally, this is ok, because WordCram isn't rendering mission-critically important data - it's for *fun.*

But if it's not rendering some words that you really want to see, what can you do?

The first thing is to understand why it's skipping your words. Paste this code into your sketch, right after `yourWordCram` renders:

{% highlight java linenos %}
 for (Word word : yourWordCram.getSkippedWords()) {
    switch (word.wasSkippedBecause()) {
    case WordSkipReason.SHAPE_WAS_TOO_SMALL:
      println(word.word + ": shape was too small");
      break;
    case WordSkipReason.WAS_OVER_MAX_NUMBER_OF_WORDS:
      println(word.word + ": was over max # of words");
      break;
    case WordSkipReason.NO_SPACE:
      println(word.word + ": no room to place it");
      break;
    }
  }
{% endhighlight %}

*2014-10-09: Updated that code sample to reflect the new WordSkipReason enum.*

Let's look at each of these cases:

* *It's too small.* The word was rendered too small, based on its weight, and the WordSizer. Try using bigger fonts: see `sizedByWeight` or `sizedByRank`.
* *It's over the limit.* You called `maxNumberOfWordsToDraw`, telling WordCram to only render so many words, and this word was over the limit. You should only use this if your corpus has tons of distinct words, and you need to keep WordCram from chugging for ever. If this is you, try setting a higher limit. But this probably isn't your problem - why would you be worried about a word that's so low in weight?
* *There's no room.* This last one is the most likely culprit: there wasn't enough room to place the word where you wanted it to go. Try sizing your words smaller, or using a different strategy for placing your words.

Here's a bit more about that last case, not having enough room. Suppose your sketch only has 3 words, and you placed them all spread out, like below. WordCram should have no problem placing them where you want them to go.

<style>
img.explain { border:1px solid black; }
</style>

<img class='explain' src="{{site.baseurl}}/assets/reasons/spread-out.png" alt="spread-out" />

But now suppose you put them all in the same corner. On the same spot, they'll overlap:

<img class='explain' src="{{site.baseurl}}/assets/reasons/overlap.png" alt="overlap" />

To fix this, WordCram nudges them around, so they're *close* to where you want:

<img class='explain' src="{{site.baseurl}}/assets/reasons/no-overlap.png" alt="no-overlap" />

With only 3 words, this is pretty easy. With 30 words, it's still easy, if they're small enough.

But with too many words, WordCram eventually will throw up its hands and move on to the next word.

<img class='explain' src="{{site.baseurl}}/assets/reasons/bubblegum.png" alt="bubblegum collides!" />

(It's ok, no one should eat bubblegum ice cream anyway.)
